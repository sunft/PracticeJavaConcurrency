package com.sunft.chapter05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并行搜索思想：
 * 将数组分成多个部分，每个线程进行搜索
 */
public class ConcurrentSearch {

    static int[] arr;

    static ExecutorService pool = Executors.newCachedThreadPool();
    static final int Thread_Num = Runtime.getRuntime().availableProcessors();
    static AtomicInteger result = new AtomicInteger(-1);

    static {
        arr = new int[1000000];
        for (int i = 0; i < 1000000; i++) {
            arr[i] = i;
        }
    }

    public static int search(int searchValue, int beginPos, int endPos) {
        int i = 0;
        for (i = beginPos; i < endPos; i++) {
            if (result.get() > 0) {
                return result.get();
            }
            if (arr[i] == searchValue) {
                //如果设置失败，则表示其他线程已经先找到了
                if (!result.compareAndSet(-1, i)) {
                    return result.get();
                }
                return i;
            }
        }
        return -1;
    }

    public static int pSearch(int searchValue) throws ExecutionException, InterruptedException {
        int subArrSize = arr.length / (Thread_Num + 1);
        List<Future<Integer>> re = new ArrayList<>();
        for (int i = 0; i < arr.length; i += subArrSize) {
            int end = i + subArrSize;
            if (end > arr.length) {
                end = arr.length;
            }
            re.add(pool.submit(new SearchTask(searchValue, i, end)));
        }

        for (Future<Integer> fu : re) {
            if (fu.get() > 0) {
                return fu.get();
            }
        }
        return -1;
    }

    public static class SearchTask implements Callable<Integer> {

        int begin, end, searchValue;

        public SearchTask(int searchValue, int begin, int end) {
            this.begin = begin;
            this.end = end;
            this.searchValue = searchValue;
        }

        @Override
        public Integer call() throws Exception {
            int re = search(searchValue, begin, end);
            return re;
        }
    }

    /**
     * 并行算法的优化
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(pSearch(19));
        pool.shutdown();
    }

}
