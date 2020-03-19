package com.sunft.chapter06;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 简单地对LongAdder、原子类，以及同步锁进行性能测试
 */
public class LongAdderDemo {

    private static final int MAX_THREADS = 3;         //线程数
    private static final int TASK_COUNT = 3;          //任务数
    private static final int TARGET_COUNT = 10000000; //目标总数

    private AtomicLong account = new AtomicLong(0L);  //无锁的原子操作
    private LongAdder laccount = new LongAdder();
    private long count = 0;

    static CountDownLatch cdlsync = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cdlatomic = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cdladdr = new CountDownLatch(TASK_COUNT);

    protected synchronized long inc() {     //有锁的加法
        return ++count;
    }

    protected synchronized long getCount() { //
        return count;
    }

    public class SyncThread implements Runnable {

        protected String name;
        protected long startTime;
        LongAdderDemo out;

        public SyncThread(LongAdderDemo o, long startTime) {
            this.out = o;
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long v = out.getCount();
            while (v < TARGET_COUNT) {//在到达目标值前，不停循环
                v = out.inc();
            }
            long endTime = System.currentTimeMillis();
            //401ms
            System.out.println("SyncThread spend:" + (endTime - startTime) + "ms" + " v = " + v);
            cdlsync.countDown();
        }
    }

    @Test
    public void testSync() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
        long startTime = System.currentTimeMillis();
        SyncThread sync = new SyncThread(this, startTime);
        for (int i = 0; i < TASK_COUNT; i++) {
            exe.submit(sync);                   //提交线程开始计算
        }
        cdlsync.await();
        exe.shutdown();
    }

    public class AtomicThread implements Runnable {

        protected String name;
        protected long startTime;

        public AtomicThread(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long v = account.get();
            while (v < TARGET_COUNT) { //在到达目标值之前，不停循环
                v = account.incrementAndGet(); //无锁的加法
            }
            long endTime = System.currentTimeMillis();
            //260ms
            System.out.println("AtomicThread spend: " + (endTime - startTime) + "ms" + " v = " + v);
            cdlatomic.countDown();
        }
    }

    @Test
    public void testAtomic() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
        long startTime = System.currentTimeMillis();
        AtomicThread atomicThread = new AtomicThread(startTime);
        for (int i = 0; i < TASK_COUNT; i ++) {
            exe.submit(atomicThread);      //提交线程开始计算
        }
        cdlatomic.await();
        exe.shutdown();

    }
}
