package com.sunft.chapter02;

/**
 * Java中线程的六种状态，该枚举位于Thread类中
 */
public enum State {
    NEW,//表示刚刚创建的线程，这种线程还没开始执行
    RUNNABLE,//当线程开始执行时，线程处于Runnable状态
    BLOCKED,//遇到synchronized块或者未竞争到锁，进入该状态
    WAITING,//进入一个无时间限制的等待
    TIMED_WAITING,//进入一个有时限的等待
    TERMINATED;//线程执行完毕后，则进入该状态
}
