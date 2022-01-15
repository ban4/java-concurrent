package com.datou.n4.synchronousmode;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.datou.util.ThreadUtil.sleep;

/**
 * 同步模式之顺序控制
 *  交替输出 比如，线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc
 *      ReentrantLock condition版本
 */
@Slf4j
public class Mode2_2 {

    public static void main(String[] args) {
        AwaitSignal wn2 = new AwaitSignal(5);
        Condition a = wn2.newCondition();
        Condition b = wn2.newCondition();
        Condition c = wn2.newCondition();

        new Thread(() -> {
            wn2.print("a", a, b);
        }).start();
        new Thread(() -> {
            wn2.print("b", b, c);
        }).start();
        new Thread(() -> {
            wn2.print("c", c, a);
        }).start();

        sleep(1);
        wn2.start(a);
    }

}

class AwaitSignal extends ReentrantLock{
    int loopNumber;
    public AwaitSignal(int loopNumber){
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition curCon, Condition nextCon) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                ThreadUtil.await(curCon);
                System.out.print(str);
                nextCon.signal();
            } finally {
                this.unlock();
            }
        }
    }
    public void start(Condition first){
        try {
            this.lock();
            first.signal();
        }finally {
            this.unlock();
        }
    }
}




//    public static void main(String[] args) {
//        ReentrantLock lock = new ReentrantLock();
//        Condition a = lock.newCondition();
//        Condition b = lock.newCondition();
//        Condition c = lock.newCondition();
//
//        for (int i = 0; i < 5; i++) {
//            new Thread(() -> {
//                lock.lock();
//                try {
//                    ThreadUtil.await(a);
//                    System.out.print("a");
//                    b.signal();
//                } finally {
//                    lock.unlock();
//                }
//            }, "t1").start();
//
//            new Thread(() -> {
//                lock.lock();
//                try {
//                    ThreadUtil.await(b);
//                    System.out.print("b");
//                    c.signal();
//                } finally {
//                    lock.unlock();
//                }
//            }, "t2").start();
//
//            new Thread(() -> {
//                lock.lock();
//                try {
//                    ThreadUtil.await(c);
//                    System.out.print("c");
//                    a.signal();
//                } finally {
//                    lock.unlock();
//                }
//            }, "t3").start();
//        }
//
//        try {
//            lock.lock();
//            a.signal();
//        }finally {
//            lock.unlock();
//        }
//    }