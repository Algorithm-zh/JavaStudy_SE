package org.thread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionStudy {
    //测试Condition
    @Test
    public void test() throws InterruptedException {
        TaskQueue2 tq = new TaskQueue2();
        List<Thread> list = new ArrayList<>();
        for(int i = 0; i < 5; i ++){
            list.add(new Thread(() -> {
                while(true){
                    String task = null;
                    try {
                        task = tq.getTask();
                    } catch (InterruptedException e) {
                        break;
                    }
                    if(task == null){
                        break;
                    }
                    System.out.println(task);
                }
            }));
            list.get(i).start();
        }
        var th = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                tq.addTask(Thread.currentThread().getName() + " " + i);
                try{Thread.sleep(100);}catch (InterruptedException e){}
            }
        });
        th.start();
        th.join();
        Thread.sleep(100);
        for (Thread thread : list) {
            thread.interrupt();
        }
    }



}
class TaskQueue2{
    private final Lock lock = new ReentrantLock();//可以替代synchronized， 更安全高效，但是要捕获异常
    //使用Condition时，引用的Condition对象必须从Lock实例的newCondition()返回，这样才能获得一个绑定了Lock实例的Condition实例
    private final Condition condition = lock.newCondition();//实现wait和notify功能
    Queue<String> q = new LinkedList<>();

    public void addTask(String s){
        lock.lock();
        try{
            q.add(s);
            condition.signalAll();
        }finally {
            lock.unlock();//在finally释放锁，防止异常时锁不释放
        }
    }
    public synchronized String getTask() throws InterruptedException {
        lock.lock();
        try{
            while(q.isEmpty()){
                condition.await();//注意是await，而不是wait,wait是java语言自带的对象锁，await是Condition实例的方法
            }
            return q.remove();
        }finally {
            lock.unlock();
        }
    }
}

