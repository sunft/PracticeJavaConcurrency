package com.sunft.chapter02;

/**
 * 创建线程
 * 一个线程如果调用两次start()方法，会有一次执行成功，
 * 另一次抛出异常java.lang.IllegalThreadStateException
 */
public class CreateThread3 implements Runnable {

    public static void main(String[] args) {
        Thread t1 = new Thread(new CreateThread3());
        t1.start();
        //t1.start();
    }

    @Override
    public void run() {
        System.out.println("Oh, I am Runnable.");
    }
}
