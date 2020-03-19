package com.sunft.chapter06;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture示例
 */
public class CompletableFutureDemo {

    public static class AskThread implements Runnable {

        CompletableFuture<Integer> re = null;

        public AskThread(CompletableFuture<Integer> re) {
            this.re = re;
        }

        @Override
        public void run() {
            int myRe = 0;
            try {
                myRe = re.get() * re.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(myRe);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new AskThread(future)).start();
        //模拟长时间的计算过程
        Thread.sleep(1000);
        //告知完成结果
        future.complete(60);
    }

}
