package com.datou.n3.section3;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员变量 --- 线程不安全
 *
 * 情况：如果线程2 还未 add，线程1 remove 就会报错:
 *      Exception in thread "Thread0" Exception in thread "Thread1" java.lang.ArrayIndexOutOfBoundsException: -1
 *      Exception in thread "Thread1" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
 * 分析:
 *      无论哪个线程中的 method2 引用的都是同一个对象中的 list 成员变量
 *      method3 与 method2 相同
 */
public class Section3_2 {
    public static void main(String[] args) {
        ThreadUnsafe threadUnsafe = new ThreadUnsafe();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                threadUnsafe.method1(200);
            }, "Thread" + i).start();
        }
    }
}

class ThreadUnsafe {
    List list = new ArrayList();
    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            // 临界区、产生 竞态条件
            method2();
            method3();
        }
    }
    private void method2() {
        list.add("1");
    }
    private void method3() {
        list.remove(0);
    }
}
