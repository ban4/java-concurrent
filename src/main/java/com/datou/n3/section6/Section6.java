package com.datou.n3.section6;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static com.datou.util.ThreadUtil.sleep;

@Slf4j(topic = "log")
public class Section6 {

    public static void main(String[] args) {
        // 先 park 再 unpark
        Thread t1 = new Thread(() -> {
            log.debug("start ...");
            sleep(2);
            log.debug("park ...");
            LockSupport.park();
            log.debug("resume ...");
        }, "t1");
        t1.start();

        sleep(4);
        log.debug("unpark");
        LockSupport.unpark(t1);


        // 先 unpark 再 park
        Thread t2 = new Thread(() -> {
            log.debug("start ...");
            sleep(5);
            log.debug("park ...");
            LockSupport.park();
            log.debug("resume ...");
        }, "t2");
        t2.start();

        sleep(4);
        log.debug("unpark");
        LockSupport.unpark(t2);
    }

}

