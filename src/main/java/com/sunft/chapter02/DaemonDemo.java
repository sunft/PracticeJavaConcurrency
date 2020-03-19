package com.sunft.chapter02;

/**
 * 守护线程的设置
 * 设置守护线程必须在线程start()之前设置，否则会抛出一个异常
 */
public class DaemonDemo {

    public static class DaemonT extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("I am alive");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new DaemonT();
        t.setDaemon(true);
        t.start();

        Thread.sleep(200);
    }

}
