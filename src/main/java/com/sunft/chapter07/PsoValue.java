package com.sunft.chapter07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PosValue主要包括两个信息，
 * 第一是表示投资规划的方案，即每一年分别需要投资多少钱；
 * 第二是这个投资方案的总收益。
 */
public final class PsoValue {

    /**
     * 表示这组投资的收益值
     */
    private final double value;
    /**
     * 保存每一年的投资额度，这里忽略了x[0]，只用后四个元素
     */
    private final List<Double> x;

    public PsoValue(double v, List<Double> x) {
        value = v;
        List<Double> b = new ArrayList<>(5);
        b.addAll(x);
        this.x = Collections.unmodifiableList(b);
    }

    public double getValue() {
        return value;
    }

    public List<Double> getX() {
        return x;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("value: ").append(value).append("\n")
                .append(x.toString());
        return sb.toString();
    }
}
