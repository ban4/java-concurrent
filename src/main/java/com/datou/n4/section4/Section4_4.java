package com.datou.n4.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 锁超时，立刻失败
 */
@Slf4j
public class Section4_4 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(() -> {
            log.debug("线程启动 ...... ");
            if(!lock.tryLock()){
                log.debug("获取锁 立刻失败");
                return;
            }
            log.debug("t1 ---- 获得了锁");
        }, "t1");

        lock.lock();
        log.debug("获得了锁");
        t.start();
        try {
            sleep(2);
        } finally {
            lock.unlock();
            log.debug("end unlock");
        }
    }
}
