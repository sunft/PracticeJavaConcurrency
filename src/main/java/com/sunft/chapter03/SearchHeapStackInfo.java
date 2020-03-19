package com.sunft.chapter03;

import java.util.concurrent.*;

/**
 * 多线程导致的堆栈信息消失解决
 */
public class SearchHeapStackInfo {

    public static class DivTask implements Runnable {

        int a, b;

        public DivTask(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            double re = a / b;
            System.out.println(re);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pools = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                0L, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        for (int i = 0; i < 5; i++) {
            //下面这句将吞掉异常信息
            //pools.submit(new DivTask(100, i));
            //改成如下信息即可
            //pools.execute(new DivTask(100, i));
            //或者改成如下方法也可以
            Future re = pools.submit(new DivTask(100, i));
            re.get();
        }
    }

}
