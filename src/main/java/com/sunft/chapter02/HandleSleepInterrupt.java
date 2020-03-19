package com.sunft.chapter02;

/**
 * Thread.sleep()方法由于中断而抛出异常，此时，它会清除中断标记，如果不加
 * 处理，那么在下一次循环开始时，就无法捕获这个中断，故在异常处理中，再次
 * 设置中断标记位。
 */
public class HandleSleepInterrupt {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interruted!");
                    break;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted When Sleep");
                    //设置中断状态
                    Thread.currentThread().interrupt();
                }
                Thread.yield();
            }
        });

        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
    }

}
