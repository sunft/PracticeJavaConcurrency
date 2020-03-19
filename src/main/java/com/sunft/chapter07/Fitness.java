package com.sunft.chapter07;

import java.util.List;

/**
 * 根据需求x与value之间的关系代码如下所示。
 * 计算收益率
 */
public class Fitness {

    public static double fitness(List<Double> x) {
        double sum = 0;
        for (int i = 1; i < x.size(); i++) {
            sum += Math.sqrt(x.get(i));
        }
        return sum;
    }

}
