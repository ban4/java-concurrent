package com.datou.n4.section2;

import lombok.extern.slf4j.Slf4j;

import static com.datou.util.ThreadUtil.sleep;

@Slf4j(topic = "log")
public class Section2_2 {
    public static void main(String[] args) {
        Room2 r = new Room2();
        new Thread(() -> {
            r.roomSleep();
        }, "小女").start();

        new Thread(() -> {
            r.roomStudy();
        }, "小南").start();
    }
}

@Slf4j(topic = "log")
class Room2 {
    private final Object sleepLock = new Object();
    private final Object studyLock = new Object();

    public void roomSleep() {
        synchronized (sleepLock) {
            log.info("sleep 4 小时");
            sleep(4);
        }
    }

    public void roomStudy() {
        synchronized (studyLock) {
            log.info("study 2 小时");
            sleep(2);
        }
    }
}
