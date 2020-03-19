package com.sunft.chapter03;

import com.google.common.util.concurrent.RateLimiter;

/**
 * RateLimiter令牌桶算法使用举例
 */
public class RateLimiterDemo {

    static RateLimiter limiter = RateLimiter.create(2);

    public static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
//            limiter.acquire();
            if (!limiter.tryAcquire()) {
                continue;
            }
            new Thread(new Task()).start();
        }
    }

}
