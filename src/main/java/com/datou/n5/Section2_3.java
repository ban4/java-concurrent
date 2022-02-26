package com.datou.n5;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步模式之 Balking
 *
 *  Balking (犹豫)模式用在一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做 了，直接结束返回
 */
@Slf4j
public class Section2_3 {
    public static void main(String[] args) {
        MonitorService m = new MonitorService();
        m.start();
        m.start();
    }

}

@Slf4j
class MonitorService{
    // 用来表示是否已经有线程已经在执行启动了
    private volatile boolean starting = false;
    public void start() {
        log.info("尝试启动监控线程...");
        synchronized (this) {
            if (starting) {
                return;
            }
            starting = true;
        }
        // 真正启动监控线程...
    }

}

// 犹豫模式 经常用来实现线程安全的单例
final class Singleton1 {
    private Singleton1() {
    }
    private static Singleton1 INSTANCE = null;
    public static synchronized Singleton1 getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new Singleton1();
        return INSTANCE;
    }
}