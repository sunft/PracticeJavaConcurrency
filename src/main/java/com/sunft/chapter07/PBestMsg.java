package com.sunft.chapter07;

/**
 * 个体最优解
 */
public class PBestMsg {

    private PsoValue value;

    public PBestMsg(PsoValue v) {
        value = v;
    }

    public PsoValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
