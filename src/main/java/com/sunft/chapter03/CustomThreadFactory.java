package com.sunft.chapter03;

import java.util.concurrent.*;

/**
 * 自定义ThreadFactory
 * 该例子使用自定义的ThreadFactory，一方面记录了线程的创建，另一方面将
 * 所有的线程都设置为守护线程，这样，当主线程退出后，将会强制销毁线程池。
 */
public class CustomThreadFactory {

    public static class MyTask implements Runnable {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":Thread ID:"
                    + Thread.currentThread().getId());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        ExecutorService es = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    System.out.println("create " + t);
                    return t;
                });

        for (int i = 0; i < 5; i++) {
            es.submit(task);
        }

        Thread.sleep(2000);
    }

}
