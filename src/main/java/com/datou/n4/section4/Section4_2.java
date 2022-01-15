package com.datou.n4.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 */
@Slf4j
public class Section4_2 {
    private static ReentrantLock reentrantLock = new ReentrantLock();
    public static void main(String[] args) {
        try {
            reentrantLock.lock();
            log.debug("main --- ");
            m1();
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void m1() {
        reentrantLock.lock();
        log.debug("m1 --- ");
        m2();
    }

    public static void m2() {
        reentrantLock.lock();
        log.debug("m2 --- ");
    }
}
