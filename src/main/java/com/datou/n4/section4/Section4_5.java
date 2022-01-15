package com.datou.n4.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 锁超时，超时失败
 */
@Slf4j
public class Section4_5 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(() -> {
            log.debug("                           t启动 ");
            try {
                if(!lock.tryLock(5, TimeUnit.SECONDS)){
                    log.debug("                           t 获取锁 立刻失败");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.debug("                           t 获得了锁");
            try {
                sleep(3);
            } finally {
                lock.unlock();
                log.debug("                           t unlock");
            }
        }, "t");

        lock.lock();
        log.debug("main 获得锁                            ");
        t.start();
        try {
            sleep(20);
        } finally {
            lock.unlock();
            log.debug("main unlock                            ");
        }
    }
}
