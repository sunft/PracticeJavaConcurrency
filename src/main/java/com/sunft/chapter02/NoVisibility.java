package com.sunft.chapter02;

/**
 * 不添加volatile的情况(注：64为虚拟机只支持Server模式)：
 * 在虚拟机的Client模式下，由于JIT并没有做足够的优化，在主线程修改ready变量的状态后，
 * ReaderThread可以发现这个改动，并退出程序。但是在Server模式下，由于系统优化的结果，
 * ReaderThread线程无法“看到”主线程中的修改，导致ReaderThread永远无法退出。这个问题就是一个典型的可见性问题。
 */
public class NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                System.out.println(number);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        Thread.sleep(1000);
        number = 42;
        ready = true;
        Thread.sleep(10000);
    }

}
