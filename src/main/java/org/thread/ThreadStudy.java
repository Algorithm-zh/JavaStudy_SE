package org.thread;

import org.collection.MapStudy;
import org.junit.jupiter.api.Test;

public class ThreadStudy {

    @Test
    public void CreateThread() throws InterruptedException {
        //方法一：从Thread派生自定义类，覆写run方法
        Thread thread1 = new MyThread();
        thread1.start();//start会在内部自动调用run
        //方法二：实现Runnable接口，并创建Thread对象
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();
        //方法三：lambda
        Thread thread3 = new Thread(() -> System.out.println("start new thread by lambda"));
        thread3.start();
        //方法四：使用方法引用
        Thread thread4 = new Thread(this::method);
        thread4.start();//start方法内部调用了start0方法，native修饰的，表示是JVM调用的c代码实现的，不是java代码实现的,所以必须调用start才能启动新线程
        thread4.setPriority(10);//设置线程优先级1-10

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }
    public void method(){
        System.out.println("start new thread by method");
    }

    @Test
    public void InterruptTest() throws InterruptedException {
        MyThread2 th = new MyThread2();
        th.start();
        Thread.sleep(1000);
        th.interrupt();//中断线程,此方法会立刻结束等待并抛出InterruptedException,
        th.running = false;//也可以是设置标志位
        th.join();
        System.out.println("thread end");
    }

    @Test
    public void DaemonTest() throws InterruptedException {
        //守护线程,在JVM中，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出。
//        在守护线程中，编写代码要注意：守护线程不能持有任何需要关闭的资源，例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
        Thread t = new Thread(() -> {
            while(true){
                System.out.println("hello I am DaemonThread");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

}

class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("start new thread by extends thread");
    }
}
class MyRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("start new thread by Runnable !");
    }
}

class MyThread2 extends Thread{
    //线程间共享变量标记volatile,说白了就是atomic
    public volatile boolean running = true;
    @Override
    public void run() {
        int n = 0;
        while(!isInterrupted() ||  running){
            n ++;
            System.out.println(n + " Hello!");
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}