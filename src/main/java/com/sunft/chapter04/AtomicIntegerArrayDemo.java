package com.sunft.chapter04;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * AtomicIntegerArray使用示例
 */
public class AtomicIntegerArrayDemo {

    static AtomicIntegerArray array = new AtomicIntegerArray(10);

    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k ++) {
                array.getAndIncrement(k % array.length());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];
        for (int k = 0; k < 10; k ++) {
            ts[k] = new Thread(new AddThread());
        }

        for (int k = 0; k < 10; k ++) {
            ts[k].start();
            ts[k].join();
        }

        System.out.println(array);
    }

}
