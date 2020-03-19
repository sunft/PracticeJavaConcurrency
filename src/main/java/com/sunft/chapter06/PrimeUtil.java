package com.sunft.chapter06;

import java.util.stream.IntStream;

/**
 * 一个判断质数的函数
 */
public class PrimeUtil {

    public static boolean isPrime(int number) {
        int tmp = number;
        if (tmp < 2) {
            return false;
        }

        for (int i = 2; Math.sqrt(tmp) >= i; i++) {
            if (tmp % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        //统计给定范围内的所有质数
        long num = IntStream.range(1, 1000000).filter(PrimeUtil::isPrime).count();
        System.out.println("The number of prime is:" + num);

        //使用并行流统计给定范围内的所有质数
        long numParallel = IntStream.range(1, 1000000).parallel().filter(PrimeUtil::isPrime).count();
        System.out.println(numParallel);
    }

}
