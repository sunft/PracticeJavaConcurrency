package com.sunft.chapter05;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;

/**
 * 在Guava中，增强了Future模式，增加了对Future模式完成时的回调接口，
 * 使得Future完成时可以自动通知应用程序进行后续处理。
 */
public class FutureDemo {

    public static void main(String[] args) {
        ListeningExecutorService service =
                MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        //ListenableFuture<String> task = service.submit()
        // TODO: 2020/2/24 代码在公司
    }

}
