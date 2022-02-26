package com.datou.n2;

import com.datou.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.*;

@Slf4j(topic = "log")
public class Section5 {

    static int joinNum = 0;
    public static void main(String[] args) throws InterruptedException {

//        runAndStartDemo();
//        sleepDemo();
//        yeildDemo();
//        joinDemo();
//        interrupt1Demo();

//        daemonDemo();
        parkDemo();
    }

    /**
     * 小结：
     *  直主线程调用run(), 没有启动新的线程。  同步执行
     *  start 开启t1新的子线程新，通过t1线程间接执行 run()。异步同时执行
     */
    private static void runAndStartDemo() {
        Thread r = new Thread("t1") {
            @Override
            public void run() {
                log.info("run ---------");
            }
        };
        r.run();        // 09:30:17 [main] c - run ---------
        r.start();      // 09:30:17 [t1] c - run ---------
    }

    /**
     * sleep():
     *      当前线程从 Running 进入 Timed Waiting 状态(阻塞)
     *      其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException
     *      睡眠结束后的线程未必会立刻得到执行
     *      建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性
     */
    private static void sleepDemo() {
        Thread t = new Thread("t1"){
            @Override
            public void run(){
                try {
                    sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true){
                    log.info("ssss");
                }
            }
        };
        t.start();
        System.out.println("ok");
    }

    /**
     * yield():
     *      当前线程从 Running 进入 Runnable 就绪状态，然后调度执行其它线程
     *      具体的实现依赖于操作系统的任务调度器
     *
     * 线程优先级setPriority():
     *      线程优先级会提示(hint)调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
     *      如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用
     */
    private static void yeildDemo() {
        Runnable task1 = () -> {
            int count = 0;
            for (; ; ) {
                System.out.println("-----> " + count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (; ; ) {
//                yield();
                System.out.println("              -----> " + count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
//        t1.setPriority(Thread.MIN_PRIORITY);
//        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
    }

    /**
     * join()
     *  1、无join()， 主线程与t线程互不干扰（并行）， joinNum = 0
     *  com.datou.n3.2、使用join（），主线程需要等待 t 线程执行完，再执行（串行）， joinNum = 10
     */
    private static void joinDemo() throws InterruptedException {
        Runnable task = () -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            joinNum = 10;
        };
        Thread t = new Thread(task,"t");
        t.start();
//        t.join(1000);           //  有时效的join        09:02:44 [main] c - 结果为：0
        t.join();                     // 10:00:55 [main] c - 结果为：10
        log.info("结果为：{}", joinNum);
    }

    /**
     * interrupt 方法
     *  打断 sleep，wait，join 的线程，这几个方法都会让线程进入阻塞状态，
     *      会清空打断状态 isInterrupted() = false （如需后续操作，需要手动处理状态）
     *
     *  打断正常运行的线程，不会清空打断状态
     *
     */
    private static void interrupt() throws InterruptedException {
        Thread t = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");
        t.start();

        TimeUnit.SECONDS.sleep(1);
        t.interrupt();
        log.debug(" 打断状态: {}", t.isInterrupted());          // 打断状态: false



        Thread t2 = new Thread(()->{
            while (true){
                Thread thread = currentThread();
                boolean interrupted = thread.isInterrupted();
                if (interrupted){
                    log.debug(" 打断状态: {}", interrupted);    // 打断状态: true
                    break;
                }
            }
        },"t2");
        t2.start();

        TimeUnit.SECONDS.sleep(1);
        t2.interrupt();
    }


    /**
     * 主线程与守护线程
     *      默认情况，Java 进程需要等待所有线程都运行结束，才会结束。
     *      守护线程，只要其它非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。
     *
     * 注意
     *      垃圾回收器线程就是一种守护线程
     *      Tomcat 中的 Acceptor 和 Poller 线程都是守护线程，所以 Tomcat 接收到 shutdown 命令后，不会等 待它们处理完当前请求
     */
    private static void daemonDemo() throws InterruptedException {
        log.debug("开始运行");
        Thread t = new Thread(()->{
            log.debug("线程开始运行...");
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("线程结束运行...");
        },"daemon");
        t.setDaemon(true);
        t.start();

        sleep(100);
        log.debug("运行结束");
    }


    /**
     * LocakSupport.park() 打断线程
     *  1、子线程park()，子线程打断并不会执行后续代码
     *  2、子线程park()，其它线程打断（interrupt）该子线程，子线程继续执行，子线程再次调用park() 不生效
     *  解决方法：使用 interrupted() 清除打断状态。
     *
     *      isInterrupted()： 打断状态为true，在子线程内再次调用park() 不生效
     *      interrupted()：  打断状态为true（该方法清除打断状态），在子线程内再次调用park() 生效
     */
    private static void parkDemo() {

//        // 1、线程调用park()，线程打断并不会执行后续代码
//        Thread t1 = new Thread(() -> {
//            log.debug("park...");
//            LockSupport.park();
//            log.debug("unpark...");
//            log.debug("打断状态:{}", Thread.currentThread().isInterrupted());
//
//        }, "t1");
//        t1.start();



        // 子线程park()，其它线程打断（interrupt）该子线程，子线程继续执行，子线程再次调用park() 不生效
//        Thread t2 = new Thread(() -> {
//            log.debug("park...");
//            LockSupport.park();
//            log.debug("unpark...");
//            log.debug("打断状态:{}", Thread.currentThread().isInterrupted());
//
//            LockSupport.park();
//            log.debug("unpark...");
//            log.debug("打断状态:{}", Thread.currentThread().isInterrupted());
//        }, "t2");
//        t2.start();
//        ThreadUtil.sleep(3);
//        t2.interrupt();


        // 子线程park()，其它线程打断（interrupt）该子线程，子线程继续执行Thread.interrupted()，重置打断状态，park() 生效，
        Thread t3 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态:{}", Thread.interrupted());

            log.debug("打断状态:{}", Thread.currentThread().isInterrupted());
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态:{}", Thread.currentThread().isInterrupted());
        }, "t3");
        t3.start();

        ThreadUtil.sleep(3);
        t3.interrupt();
    }



}


