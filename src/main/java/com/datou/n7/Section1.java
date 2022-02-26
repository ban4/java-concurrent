package com.datou.n7;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Section1 {

    public static void main(String[] args) {
        // 下面的代码在运行时，由于 SimpleDateFormat 不是线程安全的
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                try {
//                    log.debug("{}", sdf.parse("1951-04-21"));
//                } catch (Exception e) {
//                    log.error("{}", e);
//                }
//            }).start();
//        }

        // 思路 - 同步锁 这样虽能解决问题，但带来的是性能上的损失，并不算很好:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                synchronized (sdf) {
                    try {
                        log.debug("{}", sdf.parse("1951-04-21"));
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                }
            }).start();
        }

        // java8 思路 - 不可变
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                LocalDate date = dtf.parse("2018-10-01", LocalDate::from);
                log.debug("{}", date);
            }).start();
        }
    }
}
