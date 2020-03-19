package com.sunft.chapter06;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * 从集合中得到并行流
 */
public class GetStreamFromCollection {

    public static void main(String[] args) {
        List<Student> ss = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            ss.add(new Student(i));
        }

        //使用串行流
        double aveUseSerialStream = ss.stream().mapToInt(s -> s.score).average().orElse(0);
        System.out.println("The average score use serial stream is:" + aveUseSerialStream);

        //使用并行流
        double aveUseParallelStream = ss.parallelStream().mapToInt(s -> s.score).average().orElse(0);
        System.out.println("The average score use parallel stream is:" + aveUseParallelStream);
    }

}
