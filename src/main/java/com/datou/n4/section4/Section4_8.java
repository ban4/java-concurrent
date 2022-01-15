package com.datou.n4.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 条件变量
 *
 * synchronized 中也有条件变量，即 原理时 waitSet 休息室，当条件不满足时进入 waitSet 等待
 * ReentrantLock 的条件变量比 synchronized 强大之处在于，它是支持多个条件变量的，这就好比
 *      synchronized 是那些不满足条件的线程都在一间休息室等消息
 *      而 ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤 醒
 *
 * 使用要点:
 * await 前需要获得锁
 * await 执行后，会释放锁，进入 conditionObject 等待
 * await 的线程被唤醒(或打断、或超时)取重新竞争 lock 锁
 * 竞争 lock 锁成功后，从 await 后继续执行
 *
 *         ReentrantLock lock = new ReentrantLock();
 *         // 创建一个新的条件变量（即 休息室）
 *         Condition condition = lock.newCondition();
 *
 *         condition.await();      // 等待
 *         condition.await(1, TimeUnit.SECONDS);   // 超时等待
 *         condition.signal();     // 唤醒
 *         condition.signalAll();  // 唤醒所有
 */
@Slf4j
public class Section4_8 {

    static ReentrantLock lock = new ReentrantLock();
    static Condition waitCigaretteQueue = lock.newCondition();
    static Condition waitbreakfastQueue = lock.newCondition();
    static volatile boolean hasCigrette = false;
    static volatile boolean hasBreakfast = false;

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                log.debug("烟 start               ");
                lock.lock();
                while (!hasCigrette) {
                    try {
                        waitCigaretteQueue.await(); //等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到了它的烟               ");
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                log.debug("               早餐 start");
                lock.lock();
                while (!hasBreakfast) {
                    try {
                        waitbreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("               等到了它的早餐");
            } finally {
                lock.unlock();
            }
        }).start();
        sleep(5);
        sendBreakfast();
        sleep(5);
        sendCigarette();
    }

    private static void sendCigarette() {
        lock.lock();
        try {
            log.debug("送烟来了               ");
            hasCigrette = true;
            waitCigaretteQueue.signal();
        } finally {
            lock.unlock();
        }
    }

    private static void sendBreakfast() {
        lock.lock();
        try {
            log.debug("               送早餐来了");
            hasBreakfast = true;
            waitbreakfastQueue.signal();    // 唤醒
        } finally {
            lock.unlock();
        }
    }
}
