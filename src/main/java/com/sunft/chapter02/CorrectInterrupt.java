package com.sunft.chapter02;

/**
 * 增加处理中断的逻辑，如果当前线程已经中断，则终止循环
 */
public class CorrectInterrupt {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted!");
                    break;
                }
                Thread.yield();
            }
        });

        t1.start();
        Thread.sleep(2000);
        t1.interrupt();//中断线程t1
    }

}
