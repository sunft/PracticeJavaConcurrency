package com.sunft.chapter06;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 测试Java 8中对字符串进行排序的方法
 */
public class ComparatorDemo {

    public static final Comparator<String> comparator = Comparator.comparingInt(String::length)
            .thenComparing(String.CASE_INSENSITIVE_ORDER);

    public static final String[] array = {"ABA", "aba", "ABA", "CC", "CC", "cc", "CCC"};

    public static void testComparator() {
        Arrays.sort(array, comparator);
        Arrays.stream(array).forEach(e -> System.out.print(e + " "));
        System.out.println();
    }

    public static void main(String[] args) {
        testComparator();
    }

}
