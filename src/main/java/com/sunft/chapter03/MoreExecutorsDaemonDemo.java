package com.sunft.chapter03;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MoreExecutorsDaemonDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        MoreExecutors.getExitingExecutorService(executor);
        executor.execute(() -> System.out.println("I am running in " +
                Thread.currentThread().getName()));
    }

}
