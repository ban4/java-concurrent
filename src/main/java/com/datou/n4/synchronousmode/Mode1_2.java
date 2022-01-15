package com.datou.n4.synchronousmode;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.datou.util.ThreadUtil.await;
import static com.datou.util.ThreadUtil.sleep;

/**
 * 同步模式之顺序控制
 *  固定运行顺序 比如，必须先 2 后 1 打印
 *      ReentrantLock condition版本
 */
@Slf4j
public class Mode1_2 {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static Boolean flag = false;
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            try {
                lock.lock();
                while (!flag){
                    ThreadUtil.await(condition);
                }
                log.debug("1");
            }finally {
                lock.unlock();
            }
        },"t1");

        Thread t2 = new Thread(()->{
            try {
                lock.lock();
                log.debug("2");
                flag = true;
                condition.signal();
            }finally {
                lock.unlock();
            }
        },"t2");

        t1.start();
        sleep(1);
        t2.start();
    }

}
