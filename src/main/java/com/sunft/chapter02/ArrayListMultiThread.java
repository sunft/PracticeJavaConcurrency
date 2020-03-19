package com.sunft.chapter02;

import java.util.ArrayList;

/**
 * 多线程同时设置值导致ArrayList出现错误
 * ArrayList在扩容的过程中，内部一致性被破坏，但由于没有锁的保护，另外一个线程
 * 访问到了不一致的内部状态，导致出现越界问题。
 */
public class ArrayListMultiThread {

    static ArrayList<Integer> al = new ArrayList<>(10);

    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i ++) {
                al.add(i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread());
        Thread t2 = new Thread(new AddThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(al.size());
    }

}
