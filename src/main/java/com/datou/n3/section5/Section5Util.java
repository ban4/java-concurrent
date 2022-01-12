package com.datou.n3.section5;

import java.util.Arrays;
import java.util.List;

public class Section5Util {
    public static void sleep(long i) {
        try {
            Thread.sleep(1000 * i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object o) {
        try {
            o.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object o, long i) {
        try {
            o.wait(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 模拟下载返回结果
    public static List<String> download() {
        sleep(8);
        return Arrays.asList("aaa,bb,c,ddd,e");
    }
}
