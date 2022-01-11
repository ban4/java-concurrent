package com.datou.n3.section2;

import lombok.extern.slf4j.Slf4j;

/**
 * synchronized 作用于对象
 *
 * synchronized(){
 *
 * }
 *
 * synchronized 作用于方法
 *
 * class Test {
 *     public synchronized void test() {
 *     }
 * }
 * 等价于
 * class Test {
 *     public void test() {
 *         synchronized (this) {
 *         }
 *     }
 * }
 *
 * class Test {
 *     public synchronized static void test() {
 *     }
 * }
 * 等价于
 * class Test {
 *     public static void test() {
 *         synchronized (Test.class) {
 *         }
 *     }
 * }
 */
@Slf4j(topic = "log")
public class Section2 {

    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                room.increment();
            }
        }, "t1 ++");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                room.decrement();
            }
        }, "t2 --");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.err.println(room.getCounter());
    }
}

class Room {
    static int counter = 0;

    public synchronized void increment() { counter++; }

    public synchronized void decrement() { counter--; }

    public synchronized int getCounter() { return counter; }
//    public int getCounter() {
//        synchronized (this) {
//            return counter;
//        }
//    }
}
