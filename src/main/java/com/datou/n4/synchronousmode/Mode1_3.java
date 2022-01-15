package com.datou.n4.synchronousmode;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.LockSupport;
import static com.datou.util.ThreadUtil.sleep;

/**
 * 同步模式之顺序控制
 *  固定运行顺序 比如，必须先 2 后 1 打印
 *      park、unpark 版本
 */
@Slf4j
public class Mode1_3 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            // 当没有『许可』时，当前线程暂停运行;有『许可』时，用掉这个『许可』，当前线程恢复运行
            LockSupport.park();
            log.debug("1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("2");
            // 给线程 t1 发放『许可』(多次连续调用 unpark 只会发放一个『许可』)
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        sleep(1);
        t2.start();
    }

}
