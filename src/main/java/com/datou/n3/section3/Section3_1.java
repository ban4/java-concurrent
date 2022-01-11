package com.datou.n3.section3;

/**
 * 局部变量 --- 线程安全
 * 每个线程调用 test() 方法时局部变量 i，会在每个线程的栈帧内存中被创建多份，因此不存在共享
 */
public class Section3_1 {

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                test();
            }, "Thread" + i);
        }
    }

    public static void test() {
        int i = 10;
        i++;
    }
}
