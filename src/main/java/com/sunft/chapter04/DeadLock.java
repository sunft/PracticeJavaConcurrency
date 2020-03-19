package com.sunft.chapter04;

/**
 * 死锁举例：哲学家进餐问题
 */
public class DeadLock extends Thread {

    protected Object tool;
    private static final Object fork1 = new Object();
    private static final Object fork2 = new Object();

    public DeadLock(Object obj) {
        this.tool = obj;
        if (tool == fork1) {
            this.setName("哲学家A");
        }
        if (tool == fork2) {
            this.setName("哲学家B");
        }
    }

    @Override
    public void run() {
        if (tool == fork1) {
            synchronized (fork1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (fork2) {
                    System.out.println("哲学家A开始吃饭了");
                }
            }
        }

        if (tool == fork2) {
            synchronized (fork2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (fork1) {
                    System.out.println("哲学家B开始吃饭了");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadLock philosopherA = new DeadLock(fork1);
        DeadLock philosopherB = new DeadLock(fork2);
        philosopherA.start();
        philosopherB.start();
        Thread.sleep(1000);
    }

}
