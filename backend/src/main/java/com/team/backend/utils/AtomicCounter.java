package com.team.backend.utils;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @ClassName AtomicCounter
 * @Description 原子计数
 * @Author Colin
 * @Date 2023/10/23 10:53
 * @Version 1.0
 */
public class AtomicCounter {

    private static final AtomicCounter atomicCounter = new AtomicCounter();

    /**
     * 单例，不允许外界主动实例化
     */
    private AtomicCounter() {

    }

    public static AtomicCounter getInstance() {
        return atomicCounter;
    }

    private static AtomicInteger counter = new AtomicInteger();

    public int getValue() {
        return counter.get();
    }

    public int increase() {
        return counter.incrementAndGet();
    }

    public int decrease() {
        return counter.decrementAndGet();
    }

    // 清零
    public void toZero(){
        counter.set(0);
    }

}