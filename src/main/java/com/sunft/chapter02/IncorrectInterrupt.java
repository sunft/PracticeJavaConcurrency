package com.sunft.chapter02;

/**
 * 在这里，虽然对t1进行了中断，但是在t1中并没有中断处理的逻辑，
 * 因此，即使t1线程被置上了中断状态，但是这个中断不会发生任何作用。
 */
public class IncorrectInterrupt {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                Thread.yield();
            }
        });
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();//中断线程t1
    }

}
