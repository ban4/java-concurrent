package com.datou.n4.synchronousmode;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 同步模式之顺序控制
 *  交替输出 比如，线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc
 *      wait notify 版本
 */
@Slf4j
public class Mode2_1 {
    /*
     线程     输出内容        等待标记
     t1       a             1
     t2       b             2
     t3       c             3
     */
    public static void main(String[] args) {
        WaitNotify wn = new WaitNotify(1, 5);
        new Thread(()->{
            wn.print("a",1,2);
        },"t1").start();
        new Thread(()->{
            wn.print("b",2,3);
        },"t2").start();
        new Thread(()->{
            wn.print("c",3,1);
        },"t3").start();
    }
}

class WaitNotify{
    private int flag;           // 等待标记
    private int loopNumber;     // 循环次数
    public WaitNotify(int flag, int loopNumber){
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
    public void print(String str,int waitFlag,int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized(this){
                while (flag != waitFlag){
                    ThreadUtil.wait(this);
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}




//    static int flag = 1;
//    public static void main(String[] args) {
//        Object lock = new Object();
//        for (int i = 0; i < 5; i++) {
//            new Thread(()->{
//                synchronized (lock){
//                    while (flag != 1){
//                        ThreadUtil.wait(lock);
//                    }
//                    System.out.print("a");
//                    flag = 2;
//                    lock.notifyAll();
//                }
//            },"t1").start();
//
//            new Thread(()->{
//                synchronized (lock){
//                    while (flag != 2){
//                        ThreadUtil.wait(lock);
//                    }
//                    System.out.print("b");
//                    flag = 3;
//                    lock.notifyAll();
//                }
//            },"t2").start();
//
//            new Thread(()->{
//                synchronized (lock){
//                    while (flag != 3){
//                        ThreadUtil.wait(lock);
//                    }
//                    System.out.print("c");
//                    flag = 1;
//                    lock.notifyAll();
//                }
//            },"t3").start();
//        }
//    }
