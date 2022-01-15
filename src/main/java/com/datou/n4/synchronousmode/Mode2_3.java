package com.datou.n4.synchronousmode;

import java.util.concurrent.locks.LockSupport;

/**
 * 同步模式之顺序控制
 *  交替输出 比如，线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc
 *       park、unpark 版本
 */
public class Mode2_3 {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        ParkUnpark pk = new ParkUnpark(5);
        t1 = new Thread(() -> { pk.print("a", t2); });
        t2 = new Thread(() -> { pk.print("b", t3); });
        t3 = new Thread(() -> { pk.print("c", t1); });
        t1.start();t2.start();t3.start();
        LockSupport.unpark(t1);
    }

}

class ParkUnpark{
    int loopNumber;
    public ParkUnpark(int i){
        this.loopNumber = i;
    }
    public void print(String str, Thread nextT){
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(nextT);
        }
    }
}
