package com.sunft.chapter05;

/**
 * CPU Cache Line伪共享问题
 */
public class FalseSharing implements Runnable {

    public final static int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    public final static long ITERATIONS = 500L * 100L * 1000L;
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }

    public static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads) {
            t.start();
            t.join();
        }

    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }

    public final static class VolatileLong {
        public volatile long value = 0L;
        /**
         * 用于将数组中第一个VolatileLong.value和第二个VolatileLong.value分开，防止它们进入同一个缓存行
         */
        public long p1, p2, p3, p4, p5, p6, p7;
    }

}
