package com.datou.n3.section5;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 同步模式之保护性暂停
 *  带超时版 GuardedObject（与 join 原理相同）
 *
 *  begin       ->      记录最初时间
 *  timePassed  ->      已经经历的时间
 *  waitTime    ->      剩余等待时间
 *
 *  get() 方法逻辑
 *      假设millis 是 1000s ，如果在400s提前被唤醒（虚假唤醒），这时已经经历的时间假设为 400s
 *      那么还有 600s 要等， 即 long timeout = millis - waitTime;     // 剩余超时时间
 */
@Slf4j(topic = "log.Section5_3")
public class Section5_3 {

    public static void main(String[] args) {
        GuardedObject3 guardedObject = new GuardedObject3();
        new Thread(() -> {
            List<String> response = ThreadUtil.download();
            log.debug("response complete.......");
            guardedObject.complete(response);
        }, "t1").start();


        new Thread(() -> {
            ThreadUtil.sleep(2);
            log.debug("虚假唤醒");
            guardedObject.complete();
        }, "t2").start();

        // 主线程阻塞等待
        Object o = guardedObject.get(10 * 1000);
        log.debug("get response : {}", o);
    }

}

@Slf4j(topic = "log.GuardedObject")
class GuardedObject3 {
    private Object response;
    private final Object lock = new Object();

    public Object get(long millis) {
        synchronized (lock) {
            // 1) 记录最初时间
            long begin = System.currentTimeMillis();
            // 2) 已经经历的时间
            long timePassed = 0;
            while (response == null) {
                // 4) 假设（虚假唤醒）millis 是 1000，
                // 结果在 400 时唤醒了，那么还有 600 要等 long waitTime = millis - timePassed;
                long waitTime = millis - timePassed;
                log.debug("waitTime: {}", waitTime);
                if (waitTime <= 0) {
                    log.debug("break....");
                    break;
                }
                ThreadUtil.wait(lock, waitTime);
                // 3) 如果提前被唤醒，这时已经经历的时间假设为 400
                timePassed = System.currentTimeMillis() - begin;
                log.debug("timePassed: {}, object is {}", timePassed, this.response);
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足，通知等待线程
            this.response = response;
            lock.notifyAll();
        }
    }

    // 虚假唤醒
    public void complete() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}



