package com.datou.n2;

import lombok.extern.slf4j.Slf4j;

/**
 *  两阶段终止模式 (Two Phase Termination)
 */
@Slf4j(topic = "c.TwoPhaseTermination")
public class TwoPhaseTermination {

    public static void main(String[] args) throws InterruptedException {
        TPTInterrupt t = new TPTInterrupt();
        t.start();

        Thread.sleep(5000);
        log.debug("stop");
        t.stop();
    }

}

@Slf4j(topic = "c.TPTInterrupt")
class TPTInterrupt{
    private Thread thread;

    public void start (){
        thread = new Thread(()->{
            while (true){
                Thread current = Thread.currentThread();
                if (current.isInterrupted()){
                    log.debug("线程被打断，处理后事");
                    break;
                }

                try {
                    Thread.sleep(2000);
                    log.debug("业务操作。。。。。");
                } catch (InterruptedException e) {
                    // 重新设置打断标记 （防止打断 sleep，wait，join时，清空打断状态）
                    current.interrupt();
                }
            }
        }, "监控线程");
        thread.start();
    }

    public void stop(){
        thread.interrupt();
    }

}