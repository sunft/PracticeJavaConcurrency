package com.sunft.chapter06;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture异步方法调用
 */
public class CompletableFutureAsync {

    public static Integer calc(Integer para) {
        try {
            // 模拟一个长时间的执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para * para;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> calc(50));
        System.out.println(future.get());
    }

}
