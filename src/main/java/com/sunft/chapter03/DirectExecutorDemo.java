package com.sunft.chapter03;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;

/**
 * 特殊的DirectExecutor线程池使用示例
 */
public class DirectExecutorDemo {

    public static void main(String[] args) {
        Executor executor = MoreExecutors.directExecutor();
        executor.execute(() -> System.out.println("I am running in " +
                Thread.currentThread().getName()));
    }

}
