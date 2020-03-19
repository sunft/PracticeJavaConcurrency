package com.sunft.chapter03;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 测试CopyOnWriteArrayList和ConcurrentLinkedQueue并发性能
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class ListTest {

    CopyOnWriteArrayList<Object> smallCopyOnWriteList = new CopyOnWriteArrayList<>();
    ConcurrentLinkedQueue<Object> smallConcurrentList = new ConcurrentLinkedQueue();
    CopyOnWriteArrayList<Object> bigCopyOnWriteList = new CopyOnWriteArrayList<>();
    ConcurrentLinkedQueue<Object> bigConcurrentList = new ConcurrentLinkedQueue();

    @Setup
    public void setup() {
        for (int i = 0; i < 10; i++) {
            smallCopyOnWriteList.add(new Object());
            smallConcurrentList.add(new Object());
        }

        for (int i = 0; i < 1000; i++) {
            bigCopyOnWriteList.add(new Object());
            bigConcurrentList.add(new Object());
        }
    }

    @Benchmark
    public void copyOnwriteGet() {
        smallCopyOnWriteList.get(0);
    }

    @Benchmark
    public void copyOnWriteSize() {
        smallCopyOnWriteList.size();
    }

    @Benchmark
    public void concurrentListGet() {
        smallConcurrentList.peek();
    }

    @Benchmark
    public void concurrentListSize() {
        smallConcurrentList.size();
    }

    @Benchmark
    public void smallCopyOnWriteWrite() {
        smallCopyOnWriteList.add(new Object());
        smallCopyOnWriteList.remove(0);
    }

    @Benchmark
    public void smallConcurrentListWrite() {
        smallConcurrentList.add(new Object());
        smallConcurrentList.remove(0);
    }

    @Benchmark
    public void bigCopyOnWriteWrite() {
        bigCopyOnWriteList.add(new Object());
        bigCopyOnWriteList.remove(0);
    }

}
