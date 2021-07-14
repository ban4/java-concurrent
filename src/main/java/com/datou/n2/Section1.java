package com.datou.n2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 2.1 创建和运行线程
 */
@Slf4j(topic = "c")     //  logback.xml 中 <logger name="c">
public class Section1 {

    public static void main(String[] args) {
        // 1.创建和运行线程
        one();
    }

    /**
     * 小结
     * 方法1 是把线程和任务合并在了一起，
     * 方法2 是把线程和任务分开了 用 Runnable 更容易与线程池等高级 API 配合
     * 用 Runnable 让任务类脱离了 Thread 继承体系，更灵活
     */
    private static void one() {
        /**
         *  方法一，直接使用 Thread
         */
        methodByThread();

        /**
         *  方法二，使用 Runnable 配合 Thread 把【线程】和【任务】(要执行的代码)分开
         *      Thread 代表线程
         *      Runnable 可运行的任务(线程要执行的代码)
         */
        methodByRunnable();

        /**
         * 方法三，FutureTask 配合 Thread
         *  FutureTask 能够接收 Callable 类型的参数，用来处理有返回结果的情况
         */
        methodByFutureTask();
    }

    private static void methodByThread() {
        Thread t = new Thread("t 线程") {
            @Override
            public void run() {
                log.info("thread");
            }
        };
        t.start();
    }

    private static void methodByRunnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.info("runnable");
            }
        };
        Thread r = new Thread(runnable);
        r.setName("r 线程");
        r.start();

        /**
         * Java 8 以后可以使用 lambda 精简代码
         */
        Runnable runnable1 = () -> {log.info("lambda 创建线程");log.info("多行不可以省略大括号");};
        Thread r1 = new Thread(runnable1, "r1（lambda方式）线程");
        r1.start();
    }

    private static void methodByFutureTask() {
        // 创建任务对象
        FutureTask<Integer> f = new FutureTask<>(() -> {
            log.info("FutureTask 线程");
            return 100;
        });
        // 参数1 是任务对象; 参数2 是线程名字，推荐
        new Thread(f, "f 线程").start();
        // 主线程阻塞，同步等待 task 执行完毕的结果
        try {
            Integer result = f.get();
            log.info("结果是:{}", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}


