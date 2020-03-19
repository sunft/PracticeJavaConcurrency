package com.sunft.chapter04;

import java.util.AbstractList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 变量buckets存放所有的内部元素。从定义上看，它是一个保存着数组的数组，也就是通常的二维数组。
 * 特别之处在于这些数组都是使用CAS的原子数组。为什么使用二维数组去实现一个一维的Vector呢？
 * 这是为了将来Vector进行动态扩展时可以更加方便。我们知道，AtomicReferenceArray内部使用Object[]
 * 来进行实际数据的存储，这使得动态空间增加特别的麻烦，因此使用二维数组的好处就是为了将来可以方
 * 便地增加新的元素。
 *
 * @param <E>
 */
public class LockFreeVector<E> extends AbstractList<E> {

    private static final boolean debug = false;

    /**
     * 第一个桶的大小。后一个桶的大小是前一个桶大小的两倍
     */
    private static final int FIRST_BUCKET_SIZE = 8;

    /**
     * 桶的数量，第30个可以保存8*(2^30-1)个元素
     */
    private static final int N_BUCKET = 30;

    /**
     * 我们最多拥有N_BUCKET个桶。第i个桶的大小是第一个桶的(i+1)倍数
     */
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;

    static class WriteDescriptor<E> {
        public E oldV;
        public E newV;
        public AtomicReferenceArray<E> addr;
        public int addr_ind;

        /**
         * 写入描述符
         *
         * @param addr     表示要修改的原子数组
         * @param addr_ind 要写入的数组索引位置
         * @param oldV     期望值
         * @param newV     需要写入的值
         */
        public WriteDescriptor(AtomicReferenceArray<E> addr, int addr_ind,
                               E oldV, E newV) {
            this.addr = addr;
            this.addr_ind = addr_ind;
            this.oldV = oldV;
            this.newV = newV;
        }

        /**
         * 设置新值
         */
        public void doIt() {
            addr.compareAndSet(addr_ind, oldV, newV);
        }
    }

    /**
     * 作用：使用CAS操作写入新数据
     *
     * @param <E>
     */
    static class Descriptor<E> {
        public int size;
        volatile WriteDescriptor<E> writeop;

        /**
         * 创建一个新的descriptor
         *
         * @param size    整个Vector的长度
         * @param writeop 写入器
         */
        public Descriptor(int size, WriteDescriptor<E> writeop) {
            this.size = size;
            this.writeop = writeop;
        }

        /**
         * 负责写入数据
         */
        public void completeWrite() {
            WriteDescriptor<E> tmpOp = writeop;
            if (tmpOp != null) {
                tmpOp.doIt();
                //这是安全的，因为所有对writeop的写都使用null作为r_value。
                writeop = null;
            }
        }
    }

    private AtomicReference<Descriptor<E>> descriptor;
    private static final int zeroNumFirst = Integer
            .numberOfLeadingZeros(FIRST_BUCKET_SIZE);

    public LockFreeVector() {
        buckets = new AtomicReferenceArray<>(N_BUCKET);
        buckets.set(0, new AtomicReferenceArray<>(FIRST_BUCKET_SIZE));
        descriptor = new AtomicReference<>(new Descriptor<>(0,
                null));
    }

    /**
     * 在Vector末尾添加一个元素
     *
     * @param e
     */
    public void push_back(E e) {
        Descriptor<E> desc;
        Descriptor<E> newd;

        do {
            desc = descriptor.get();
            desc.completeWrite();

            int pos = desc.size + FIRST_BUCKET_SIZE;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            int bucketInd = zeroNumFirst - zeroNumPos;
            if (buckets.get(bucketInd) == null) {
                int newLen = 2 * buckets.get(bucketInd - 1).length();
                if (debug) {
                    System.out.println("New Length is:" + newLen);
                }
                buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<>(newLen));
            }
            int idx = (0x80000000 >>> zeroNumPos) ^ pos;
            newd = new Descriptor<E>(desc.size + 1, new WriteDescriptor<>(
                    buckets.get(bucketInd), idx, null, e));
        } while (!descriptor.compareAndSet(desc, newd));
        descriptor.get().completeWrite();
    }

    /**
     * 移除vector中的最后一个元素
     *
     * @return
     */
    public E pop_back() {
        Descriptor<E> desc;
        Descriptor<E> newd;
        E elem;

        do {
            desc = descriptor.get();
            desc.completeWrite();

            int pos = desc.size + FIRST_BUCKET_SIZE - 1;
            int bucketInd = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
                    - Integer.numberOfLeadingZeros(pos);
            int idx = Integer.highestOneBit(pos) ^ pos;
            elem = buckets.get(bucketInd).get(idx);
            newd = new Descriptor<>(desc.size - 1, null);
        } while (!descriptor.compareAndSet(desc, newd));
        return elem;
    }

    /**
     * 获取指定索引的元素
     *
     * @param index 索引
     * @return 指定索引位置的元素
     */
    @Override
    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketInd = zeroNumFirst - zeroNumPos;
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        return buckets.get(bucketInd).get(idx);
    }

    /**
     * 设置index位置的元素为E
     *
     * @param index 索引
     * @param e     待设置的元素
     * @return 返回index位置的旧值
     */
    @Override
    public E set(int index, E e) {
        int pos = index + FIRST_BUCKET_SIZE;
        int bucketInd = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
                - Integer.numberOfLeadingZeros(pos);
        int idx = Integer.highestOneBit(pos) ^ pos;
        AtomicReferenceArray<E> bucket = buckets.get(bucketInd);
        while (true) {
            E oldV = bucket.get(idx);
            if (bucket.compareAndSet(idx, oldV, e)) {
                return oldV;
            }
        }
    }

    /**
     * 预留更多的控件
     *
     * @param newSize
     */
    public void reserve(int newSize) {
        int size = descriptor.get().size;
        int pos = size + FIRST_BUCKET_SIZE - 1;
        int i = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
                - Integer.numberOfLeadingZeros(pos);
        if (i < 1) {
            i = 1;
        }
        int initialSize = buckets.get(i - 1).length();
        while (i < Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE)
                - Integer.numberOfLeadingZeros(newSize + FIRST_BUCKET_SIZE - 1)) {
            i++;
            initialSize *= FIRST_BUCKET_SIZE;
            buckets.compareAndSet(i, null, new AtomicReferenceArray<>(initialSize));
        }
    }

    @Override
    public int size() {
        return descriptor.get().size;
    }

    @Override
    public boolean add(E object) {
        push_back(object);
        return true;
    }
}
