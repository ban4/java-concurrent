package com.datou.n5;

public class Section3_1 {

}

/**
 *   double-checked locking 问题
 *       单例模式为例
 *
 * 以下的实现特点是:
 *  懒惰实例化
 *  首次使用 getInstance() 才使用 synchronized 加锁，后续使用时无需加锁
 *  有隐含的，但很关键的一点:第一个 if 使用了 INSTANCE 变量，是在同步块之外，如果不加volatile 在多线程环境下，代码是有问题的
 */
final class Singleton2{
    private Singleton2(){ }
//    private static Singleton2 INSTANCE = null; 多线程有问题， 需要加volatile
    private static volatile Singleton2 INSTANCE = null;

    public static Singleton2 getInstance() {
        // 实例没创建，才会进入内部的 synchronized代码块
        if (null == INSTANCE) {
            synchronized (Singleton2.class) {           // t2
                // 也许有其它线程已经创建实例，所以再判断一次
                if (null == INSTANCE) {                 // t1
                    INSTANCE = new Singleton2();
                }
            }
        }
        return INSTANCE;
    }

}
