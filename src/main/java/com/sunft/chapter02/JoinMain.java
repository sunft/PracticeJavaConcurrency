package com.sunft.chapter02;

/**
 * join()的使用示例
 */
public class JoinMain {

    public volatile static int i = 0;

    public static class AddThread extends Thread {
        @Override
        public void run() {
            for (i = 0; i < 10000000; i ++);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread at = new AddThread();
        at.start();
        //主线程愿意等待AddThread执行完毕，跟着AddThread一起往前走，故在join()返回时，
        //AddThread已经执行完毕，故i总是10000000。
        at.join();
        System.out.println(i);
    }

}
