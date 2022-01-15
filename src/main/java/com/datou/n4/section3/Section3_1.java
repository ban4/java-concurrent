package com.datou.n4.section3;

import lombok.extern.slf4j.Slf4j;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 一个线程需要同时获取多把锁，这时就容易发生死锁
 *      t1 线程获得 A对象锁，接下来想获取 B对象的锁
 *      t2 线程获得 B对象锁，接下来想获取 A对象 的锁
 */
@Slf4j(topic = "log")
public class Section3_1 {
    private static Object A =  new Object();
    private static Object B =  new Object();
    public static void main(String[] args) {

        new Thread(()->{
            synchronized (A){
                log.debug("t1 ---> lock A");
                sleep(2);
                synchronized (B){
                    log.debug("t1 ---> lock B");
                    log.debug("t1 ---> 操作...");
                }
            }
        },"t1").start();


        new Thread(()->{
            synchronized (B){
                log.debug("t2 ---> lock B");
                sleep(1);
                synchronized (A){
                    log.debug("t2 ---> lock A");
                    log.debug("t2 ---> 操作...");
                }
            }
        },"t2").start();

    }


}
