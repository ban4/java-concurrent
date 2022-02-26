package com.datou.n5;

import static com.datou.util.ThreadUtil.sleep;

public class Section2_1 {
     static boolean run = true;
    //    volatile static boolean run = true;  volatile 解决可见性问题

    // 退不出的循环， 注意： System.out.println(); 看不到效果
    public static void main(String[] args) {
        new Thread(()->{
            while (run){

            }
        },"t").start();

        sleep(1);
        run = false;
    }

}
