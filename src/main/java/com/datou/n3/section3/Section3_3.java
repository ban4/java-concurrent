package com.datou.n3.section3;

import java.util.ArrayList;
import java.util.List;

/**
 * 局部变量 --- 线程安全（在作用范围内）
 * <p>
 * (3_2 成员变量 --- 线程不安全) 将list 修改为局部变量，那么就不会有线程不安全问题了
 * <p>
 * 分析:
 * list 是局部变量，每个线程调用时会创建其不同实例，没有共享
 * 而 method2 的参数是从 method1 中传递过来的，与 method1 中引用同一个对象 method3 的参数分析与 method2 相同
 */
public class Section3_3 {

    public static void main(String[] args) {
        Threadsafe threadsafe = new Threadsafe();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                threadsafe.method1(2000);
            }, "Thread" + i).start();
        }
    }
}

class Threadsafe {
    public void method1(int loopNumber) {
        List list = new ArrayList();
        for (int i = 0; i < loopNumber; i++) {
            // 临界区、产生 竞态条件
            method2(list);
            method3(list);
        }
    }

    public void method2(List list) {
        list.add("1");
    }

    public void method3(List list) {
        list.remove(0);
    }
}
