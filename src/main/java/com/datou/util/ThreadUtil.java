package com.datou.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class ThreadUtil {

    // 模拟下载返回结果
    public static List<String> download() {
        sleep(8);
        return Arrays.asList("aaa,bb,c,ddd,e");
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
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

    public static void wait(Object lock, long l) {
        try {
            lock.wait(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void await(Condition lock) {
        try {
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void await(Condition lock, int secends) {
        try {
            lock.await(secends, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void join(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
