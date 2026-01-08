package org.thread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;

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


    //测试ReadWriteLock(悲观锁）
    @Test
    public void test2() throws InterruptedException {
        Counter2 c = new Counter2();
        Thread th1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                c.inc(i);
            }});
        Thread th2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(c.get()[i]);
            }});
        th1.start();
        th2.start();
        th1.join();
        th2.join();
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

//悲观锁ReadWriteLock
class Counter2 {
    //读写锁，可以保证只允许一个线程写，多个线程同时读
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();//悲观锁，读的过程不允许写
    // 注意: 一对读锁和写锁必须从同一个rwlock获取:
    private final Lock rlock = rwlock.readLock();


    private final Lock wlock = rwlock.writeLock();
    private int[] counts = new int[10];

    public void inc(int index) {
        wlock.lock(); // 加写锁
        try {
            counts[index] += 1;
        } finally {
            wlock.unlock(); // 释放写锁
        }
    }

    public int[] get() {
        rlock.lock(); // 加读锁
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            rlock.unlock(); // 释放读锁
        }
    }
}

//乐观锁StampedLock
class Point {
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁
        // 注意下面两行代码不是原子操作
        // 假设x,y = (100,200)
        double currentX = x;
        // 此处已读取到x=100，但x,y可能被写线程修改为(300,400)
        double currentY = y;
        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            //有写入，需要重新获取悲观读锁
            stamp = stampedLock.readLock(); // 获取一个悲观读锁
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}


//适用于限制下载人数或访问次数之类的情况
class AccessLimitControl {
    // 任意时刻仅允许最多3个线程获取许可:
    final Semaphore semaphore = new Semaphore(3);

    public String access() throws Exception {
        // 如果超过了许可数量,其他线程将在此等待:
        semaphore.acquire();
        try {
            return UUID.randomUUID().toString();
        } finally {
            semaphore.release();
        }
    }
}
