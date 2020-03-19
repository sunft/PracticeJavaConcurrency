package com.sunft.chapter02;

/**
 * 验证volatile无法保证原子性
 * 最终的值应该是100000(10个线程各加10000次)。
 * 但实际上，上述代码的输出总是会小于100000。
 */
public class BadVolatile {

    static volatile int i = 0;

    public static class PlusTask implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k ++) {
                i ++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i ++) {
            threads[i] = new Thread(new PlusTask());
            threads[i].start();
        }

        for (int i = 0; i < 10; i ++) {
            threads[i].join();
        }

        System.out.println(i);
    }

}
