package com.datou.n3.section5;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
public class Test {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
//            People people = new People();
//            people.run();
        }

        Thread.sleep(5 * 1000);

        MailBox.getIds().forEach(id -> {
            new Postman(id, "mail --- " + id).start();
        });

    }
}

class MailBox {
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;
    // 产生唯一id
    private static synchronized int generateId() {
        return id++;
    }
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Object getGuardedObject(){
        return boxes.remove(id);
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }

}
@Slf4j(topic = "log.GuardedObject")
class GuardedObject{
    private int id;
    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public synchronized void setResponse(Object response) {
        this.response = response;
        this.notifyAll();
        log.info("set response : {} , notifyAll()", response);
    }

    public synchronized Object getResponse() throws InterruptedException {
        while (response == null){
            log.info("get , wait()");
            this.wait();
        }
        return response;
    }
}

//@Slf4j(topic = "log.People")
//class People {
//    public void run() {
//        new Thread(() -> {
//            GuardedObject guardedObject = MailBox.createGuardedObject();
//            Object mail = null;
//            log.info("开始收信 id:{} ", guardedObject.getId());
//            try {
//                mail = guardedObject.getResponse();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.info("收信成功 id:{}，mail {} ", guardedObject.getId(), mail);
//        }).start();
//    }
//}
@Slf4j(topic = "log.People")
class People extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        GuardedObject guardedObject = MailBox.createGuardedObject();
        log.info("开始收信 id:{} ", guardedObject.getId());
        Object mail = guardedObject.getResponse();
        log.info("收信成功 id:{}，mail {} ", guardedObject.getId(), mail);
    }
}

@Slf4j(topic = "log.Postman")
class Postman extends Thread{
    private int id;
    private String mail;
    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }
    @Override
    public void run() {
        GuardedObject guardedObject = MailBox.createGuardedObject();
        guardedObject.setResponse(mail);
        log.info("送信 id:{}，内容 {} ", id, mail);
    }
}


