package com.sunft.chapter07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一个典型的不可变对象
 */
public final class ImmutableMessage {

    private final int sequenceNumber;

    private final List<String> values;

    public ImmutableMessage(int sequenceNumber, List<String> values) {
        this.sequenceNumber = sequenceNumber;
        //为了实现彻底地不可变性，将集合也实现不可变性
        this.values = Collections.unmodifiableList(new ArrayList<>(values));
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public List<String> getValues() {
        return values;
    }

}
