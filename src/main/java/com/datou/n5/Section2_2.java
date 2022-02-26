package com.datou.n5;

import lombok.extern.slf4j.Slf4j;

/**
 * 两阶段终止模式：
 *  停止标记用 volatile 是为了保证该变量在多个线程之间的可见性
 *  我们的例子中，即主线程把它修改为 true 对 t1 线程可见
 */
@Slf4j
public class Section2_2 {

    public static void main(String[] args) throws InterruptedException {
        TPTVolatile t = new TPTVolatile();
        t.start();

        Thread.sleep(3500);
        log.debug("stop");
        t.stop();
    }

}
@Slf4j
class TPTVolatile {
    private Thread monitorThread;
    private volatile boolean stop = false;

    public void start() {
        monitorThread = new Thread(() -> {
            while (true) {
                if (stop) {
                    log.debug("结束 料理后事。。。");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("将结果保存");
                } catch (InterruptedException e) {
                   log.error("InterruptedException...");
                }
            }
        }, "监控线程");

        monitorThread.start();
    }

    public void stop() {
        stop = true;
        // 不加打断需要等到下一次 sleep 结束才能退出循环，这里是为了更快结束
        monitorThread.interrupt();
    }
}
