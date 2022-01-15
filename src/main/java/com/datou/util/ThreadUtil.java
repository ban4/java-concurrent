package com.datou.util;

public class ThreadUtil {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000L * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object lock) {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object lock, int second) {
        try {
            lock.wait(1000L * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
