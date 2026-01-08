package org.thread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSynchronizationStudy {
//不需要synchronized的操作
/*
1.基本类型赋值
2.引用类型赋值
单行赋值语句不需要同步，多行必须同步
不可变对象无需同步
 */

    //测试线程安全类
    //synchronized是可重入锁，可以加锁多次，但是一定要防止死锁
    @ParameterizedTest()
    @ValueSource(ints = {100, 200, 300, 400, 500, 100, 200, 300, 400, 500})
    public void test1(int x) throws InterruptedException {
        Counter c = new Counter();
        Thread th1 = new Thread(() -> {
            for (int i = 0; i < x; i++) {
                c.add(i);
            }});
        Thread th2 = new Thread(() -> {
            for (int i = 0; i < x; i++) {
                c.dec(i);
            }});
        th1.start();
        th2.start();
        th1.join();
        th2.join();
        System.out.println(c.get());
    }

    //测试wait和notify
    //5个读线程一直读，一个写线程写10次
    @Test
    public void test2() throws InterruptedException {
        TaskQueue tq = new TaskQueue();
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

    //测试线程安全集合类
/*
    interface	non-thread-safe	        thread-safe
    List	    ArrayList	            CopyOnWriteArrayList
    Map	        HashMap	                ConcurrentHashMap
    Set	        HashSet / TreeSet	    CopyOnWriteArraySet
    Queue	    ArrayDeque / LinkedList	ArrayBlockingQueue / LinkedBlockingQueue
    Deque	    ArrayDeque / LinkedList	LinkedBlockingDeque
 */
    @Test
    public void test3() throws InterruptedException {
        Map<Integer, String> map = new ConcurrentHashMap<>();
        Thread th = new Thread(() -> {
           for (int i = 0; i < 10; i++){
               map.put(i, "value" + i);
           }
        });
        th.start();
        Thread th2 = new Thread(() -> {
            for (int i = 0; i < 10; i++){
                System.out.println(map.get(i));
            }
        });
        th2.start();
        th.join();
        th2.join();
    }


    //java Atomic实现方式
    //CAS:Compare And Set
    //如果当前值是prev,那就更新为next,返回为true，如果不是prev,就啥也不干，返回false
    public int incrementAndGet(AtomicInteger var){
        int prev, next;
        do{
            prev = var.get();
            next = prev + 1;
        }while(!var.compareAndSet(prev, next));
        return next;
    }


}
//将synchronized封装起来,这个类就变成了一个线程安全的类
class Counter{
    private int count = 0;
    //这样就和下面是一样的,锁的也是this
    public synchronized void add(int n){
        count += n;
    }
    public void dec(int n){
        synchronized (this){//synchronized锁的是对象，Object，啥对象都行，要同步哪些操作，哪些操作就得锁同一个对象
            count -= n;
        }
    }
    //读一个int变量不需要同步
    public int get(){
        return count;
    }
    public synchronized static void test(int n){
        //相当于下面这个
//        synchronized (Counter.class){
//            //静态方法锁的是Counter.class
//        }
    }
}
class TaskQueue{
    Queue<String> q = new LinkedList<>();

    public synchronized void addTask(String s){
        q.offer(s);
        this.notifyAll();//唤醒所有正在this锁等待的线程
    }

    public synchronized String getTask() throws InterruptedException {
        //如果队列为空，我们需要在这里等待，直到有任务加入,这个时候我们就需要用到wait，wait会释放锁，因此别的线程能够继续执行
        //而不至于一直锁在这里
        while(q.isEmpty()){//注意是while而不是if，因为线程被唤醒后，必须再次检查条件，如果条件不满足，则继续等待，如果条件满足，则继续执行
            //只能在锁对象上调用wait()方法。因为在getTask()中，我们获得了this锁，因此，只能在this对象上调用wait()方法
            this.wait();//线程从等待状态被其他线程唤醒后，wait()方法才会返回，然后，继续执行下一条语句
        }
//        cpp不需要这么麻烦，可以直接cond.wait(lock, [](){return !q.isEmpty();}); //一句话的事
        return q.remove();
    }
}

