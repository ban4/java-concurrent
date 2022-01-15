package com.datou.n3.section5;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * 同步模式之保护性暂停
 *
 *  一个线程等待另一个线程的执行结果
 */
@Slf4j(topic = "log.Section5_2")
public class Section5_2 {

    public static void main(String[] args) {
        GuardedObject2 guardedObject = new GuardedObject2();

        new Thread(() -> {
            List<String> response = ThreadUtil.download();
            log.debug("response complete.......");
            guardedObject.complete(response);
        }, "t1").start();

        log.debug("waiting....");
        // 主线程阻塞等待
        Object o = guardedObject.get();
        log.debug("get response : {}", o.toString());
    }

}
@Slf4j(topic = "log.GuardedObject")
class GuardedObject2{
    private Object response;
    private final Object lock = new Object();

    public Object get() {
        synchronized (lock) {
            while (response == null) {
                ThreadUtil.wait(lock);
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
}
