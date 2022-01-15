package com.datou.n4.section3;

import lombok.extern.slf4j.Slf4j;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 活锁
 *   两个线程互相改变对方的结束条件，最后谁也无法结束
 */
@Slf4j
public class Section3_3 {
    static volatile int count = 10;
    public static void main(String[] args) {
        // 期望减到 0 退出循环
        new Thread(() -> {
            while (count > 0) {
                sleep(1);
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();

        // 期望超过 20 退出循环
        new Thread(() -> {
            while (count < 20) {
                sleep(1);
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();

    }

}
