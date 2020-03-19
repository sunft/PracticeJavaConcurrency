package com.sunft.chapter06;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Reduce操作示例，用于并行计算ConcurrentHashMap中所有value的总和。
 * 第一个参数parallelismThreshold表示并行度，表示一个并行任务可以处理的的元素个数(估算值)。
 * 如果设置为Long.MAX_VALUE，则表示完全禁用并行，设置为1则表示使用最大并行可能。
 */
public class ConcurrentHashMapReduceDemo {

    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        for (int i = 1; i <= 100; i++) {
            map.put(Integer.toString(i), i);
        }
        int count = map.reduceValues(2, (i, j) -> i + j);
        System.out.println(count);
    }

}
