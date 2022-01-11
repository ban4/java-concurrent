package com.datou.n3.section3;

import java.util.ArrayList;
import java.util.List;

/**
 * 在 3_3 的基础上，为 ThreadSafe 类添加子类，子类覆盖 method2 或 method3 方法，
 *
 * 可能导致如下报错：
 * Exception in thread "Thread-214" java.lang.IndexOutOfBoundsException: Index: 0, Size: com.datou.n3.2
 *         at java.util.ArrayList.rangeCheck(ArrayList.java:657)
 *         at java.util.ArrayList.remove(ArrayList.java:496)
 *         at com.datou.n3.section3.ThreadSafeSubClass.lambda$method3$1(Section3_4.java:50)
 *         at java.lang.Thread.run(Thread.java:748)
 *         Exception in thread "Thread-361" java.lang.IndexOutOfBoundsException: Index: 0, Size: 1
 *
 * 结论：局部变量引用的对象，逃离方法的作用范围（暴露引用） --- 线程不安全
 *
 * 从这个例子可以看出 private 或 final 提供【安全】的意义所在，请体会开闭原则中的【闭】
 */
public class Section3_4 {
    public static void main(String[] args) {
        ThreadSafeTest threadUnsafe = new ThreadSafeSubClass();
//        ThreadSafeSubClass threadUnsafe = new ThreadSafeSubClass();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                threadUnsafe.method1(5000);
            }, "Thread" + i).start();
        }
    }
}

class ThreadSafeTest {
    public void method1(int loopNumber) {
        List list = new ArrayList();
        for (int i = 0; i < loopNumber; i++) {
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

class ThreadSafeSubClass extends ThreadSafeTest {
    @Override
    public void method3(List list) {
        new Thread(() -> {
            System.out.println("-------");
            list.remove(0);
        }).start();
    }
}


