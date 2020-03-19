package com.sunft.chapter03;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁测试
 */
public class FairLock implements Runnable {
    public static ReentrantLock fairLock = new ReentrantLock(true);

    @Override
    public void run() {
        while (true) {
            try {
                fairLock.lock();
                System.out.println(Thread.currentThread().getName() + " 获得锁。");
            } finally {
                fairLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        FairLock fl = new FairLock();
        Thread t1 = new Thread(fl, "Thread_t1");
        Thread t2 = new Thread(fl, "Thread_t2");
        t1.start();
        t2.start();
    }
}
