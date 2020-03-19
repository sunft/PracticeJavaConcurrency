package com.sunft.chapter08;

import java.util.ArrayList;

/**
 * 实验样本，在多线程环境下访问ArrayList
 * IDEA多线程调试技巧
 */
public class UnsafeArrayList {

    private static ArrayList<Object> al = new ArrayList<>();

    public static class AddTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            for (int i = 0; i < 1000000; i++) {
                al.add(new Object());
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new AddTask(), "t1");
        Thread t2 = new Thread(new AddTask(), "t2");
        t1.start();
        t2.start();
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "t3");
        t3.start();
    }

}
