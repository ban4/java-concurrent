package com.datou.n4.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 可打断锁
 *  lock 不可打断锁
 *
 *  lockInterruptibly 可打断锁
 *      如果没有竞争 那么此方法就会获取 lock 对象锁
 *      如果有竞争就进入阻塞队列，可以被其他线程用 interrupt 方法打断
 */
@Slf4j
public class Section4_3 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t = new Thread(() -> {
            log.debug("启动t ......");
            try {
                // 如果没有竞争 那么此方法就会获取 lock 对象锁
                // 如果有竞争就进入阻塞队列，可以被其他线程用 interrupt 方法打断
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获得锁，返回");
                return;
            }
        }, "t1");

        log.debug("start ......");
        lock.lock();
        t.start();

        try {
            sleep(5);
            log.debug("打断t线程");
            t.interrupt();
        } finally {
            lock.unlock();
        }
    }
}
