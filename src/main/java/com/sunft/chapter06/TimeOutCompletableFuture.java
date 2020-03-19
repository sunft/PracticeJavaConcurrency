package com.sunft.chapter06;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 带有过时时间的CompletableFuture
 * orTimeout只在JDK 9以及9以上的版本有效
 */
public class TimeOutCompletableFuture {

    public static Integer calc(Integer para) {
        return para / 2;
    }

    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return calc(50);
        })/*.orTimeout(1, TimeUnit.SECONDS).exceptionally(e ->
        {
            System.out.println(e);
            return 0;
        })*/.thenAccept(System.out::println);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
