package com.datou.n4.synchronousmode;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 同步模式之顺序控制
 *  固定运行顺序 比如，必须先 2 后 1 打印
 *      wait notify 版本
 */
@Slf4j
public class Mode1_1 {
    // 用来同步的对象
    static Object lock = new Object();
    // t2 运行标记， 代表 t2 是否执行过
    static Boolean flag = false;
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            synchronized (lock){
                while (!flag){
                    ThreadUtil.wait(lock);
                }
                log.debug("1");
            }
        },"t1");

        Thread t2 = new Thread(()->{
            synchronized (lock){
                log.debug("2");
                flag = true;
                lock.notifyAll();
            }
        },"t2");

        t1.start();
        sleep(1);
        t2.start();
    }

}
