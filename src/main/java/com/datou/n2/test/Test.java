package com.datou.n2.test;

import lombok.extern.slf4j.Slf4j;
/**
 * 华罗庚《统筹方法》
 *   洗水壶1分钟    烧开水 15分钟                     泡茶
 *                洗茶壶,洗茶杯,拿茶叶 4分钟
 */
@Slf4j(topic = "c.Test")
public class Test {

    public static void main(String[] args) throws InterruptedException {
        log.info("-洗水壶1s");
        sleep(1);

        Thread t2 = new Thread(() -> {
            log.info("--烧开水15s");
            sleep(15);
        }, "t2");

        Thread t3 = new Thread(()->{
            log.info("--洗茶壶,洗茶杯,拿茶叶4s");
            sleep(4);
        },"t3");
        t2.start();
        t3.start();
        t2.join();
        log.info("泡茶");
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(1000 * time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
