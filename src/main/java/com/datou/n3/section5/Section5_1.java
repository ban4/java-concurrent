package com.datou.n3.section5;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "log")
public class Section5_1 {

    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没?[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会!");
                    wait(room);
                }
                log.debug("有烟没?[{}]", hasCigarette);
                log.debug("可以开始干活了");
            }
        }, "小南").start();

        new Thread(() -> {
            synchronized (room) {
                log.debug("外卖送到没?[{}]", hasTakeout);
                if (!hasTakeout) {
                    log.debug("没外卖，先歇会!");
                    wait(room);
                }
                log.debug("外卖送到没?[{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小女").start();

        sleep(20);
        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了噢!");
                room.notifyAll();
            }
        }, "送烟的").start();

    }

    private static void sleep(int i) {
        try {
            Thread.sleep(1000 * i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void wait(Object o) {
        try {
            o.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void wait(Object o, int i) {
        try {
            o.wait(1000 * i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


