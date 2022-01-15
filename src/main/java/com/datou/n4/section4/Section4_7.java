package com.datou.n4.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁
 */
@Slf4j
public class Section4_7 {
    public static void main(String[] args) {
//        m1();
        m2();
    }

    /**
     * 公平锁
     *  强行插入，总是在最后输出
     *
     * 公平锁一般没有必要，会降低并发度
     */
    private static void m2() {
        ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                } finally {
                    lock.unlock();
                }
            }, "t" + i).start();
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start...");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " running...");
            } finally {
                lock.unlock();
            }
        }, "强行插入").start();
        lock.unlock();
    }

    /**
     * 非公平锁
     *  强行插入，有机会在中间输出
     *
     * 注意:该实验不一定总能复现
     */
    private static void m1() {
        // ReentrantLock 默认是不公平的
        ReentrantLock lock = new ReentrantLock(false);   //new ReentrantLock();

        lock.lock();
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                } finally {
                    lock.unlock();
                }
            }, "t" + i).start();
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start...");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " running...");
            } finally {
                lock.unlock();
            }
        }, "强行插入").start();
        lock.unlock();
    }

}
