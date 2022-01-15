package com.datou.n4.section2;

import lombok.extern.slf4j.Slf4j;

import static com.datou.util.ThreadUtil.sleep;

@Slf4j(topic = "log")
public class Section2_1 {
    public static void main(String[] args) {
        Room1 r = new Room1();
        new Thread(() -> {
            r.roomSleep();
        }, "小女").start();

        new Thread(() -> {
            r.roomStudy();
        }, "小南").start();
    }
}

@Slf4j(topic = "log")
class Room1 {

    public void roomSleep() {
        synchronized (this) {
            log.info("sleep 4 小时");
            sleep(4);
        }
    }

    public void roomStudy() {
        synchronized (this) {
            log.info("study 2 小时");
            sleep(2);
        }
    }

}
