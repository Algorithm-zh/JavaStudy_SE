package org.thread;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/*
因为ExecutorService只是接口，Java标准库提供的几个常用实现类有：
    FixedThreadPool：线程数固定的线程池；
    CachedThreadPool：线程数根据任务动态调整的线程池；
    SingleThreadExecutor：仅单线程执行的线程池。
 */
public class ThreadPoolStudy {

    //CachedThreadPool测试
    @Test
    public void test1(){
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i = 0; i < 6; i ++){
            es.submit(new Task("task" + i));
        }
        //会等正在执行的任务完成再关闭
        es.shutdown();
        //限制线程数量在4-10个之间的方法，可以研究一下newCachedThreadPool
        /*
            return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                          60L, TimeUnit.SECONDS,
                                          new SynchronousQueue<Runnable>());
         */
        ExecutorService es2 = new ThreadPoolExecutor(4, 10, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    //ScheduledThreadPool测试
    //有一种任务，需要定期反复执行，例如，每秒刷新证券价格。这种任务本身固定，需要反复执行的
    @Test
    @Disabled
    public void test2() throws InterruptedException {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(5);
        //1秒后执行的一次性任务
        es.schedule(new Task("task one"), 1, TimeUnit.SECONDS);
        //2秒后开始执行定时任务，每三秒执行
        es.scheduleAtFixedRate(new Task("task fix"), 2, 3, TimeUnit.SECONDS);
        //2秒后开始执行以固定的3秒为间隔执行(和上面的区别是，它是不管你这个任务执行的时间的，计算这个任务执行了2秒多了，到3秒也会立马执行，而这个是这个任务执行完了再等3秒)
        es.scheduleWithFixedDelay(new Task("task interval"), 2, 3, TimeUnit.SECONDS);
        Thread.sleep(6000);
        //注意！ 这种定时的任务shutdown不会等待他们结束
        es.shutdown();
    }

    //Future测试
    //当我们提交一个Callable任务后，我们会同时获得一个Future对象，然后，我们在主线程某个时刻调用Future对象的get()方法，就可以获得异步执行的结果。
    //在调用get()时，如果异步任务已经完成，我们就直接获得结果。如果异步任务还没有完成，那么get()会阻塞，直到任务完成后才返回结果
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        //定义任务
        Callable<String> task = new Task2("task future 1");
        //提交任务并获得future
        Future<String> future = es.submit(task);
        //从future获得异步执行的返回结果
        System.out.println(future.get());
    }

    //CompletableFuture测试
    //针对Future做了改进，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法
    @Test
    public void test4() throws InterruptedException {
        //创建异步执行任务
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(()->{
            return queryCode("中国石油");
        });
        //并行执行，在执行完上个任务后再执行这个任务
        CompletableFuture<Double> query = cf.thenApplyAsync(ThreadPoolStudy::fetchPrice);
        //或下面这个，由于fetchPrice的函数签名和Supplier一样,所以可以直接使用方法引用
//        CompletableFuture<Double> query = cf.thenApplyAsync((code)->{
//            return fetchPrice(code);
//        });
        //执行成功
        query.thenAccept((result)->{
            System.out.println("price: " + result);
        });
        //异常
        query.exceptionally((e) -> {
           e.printStackTrace();
           return null;
        });
        Thread.sleep(2000);
    }

    //测试anyOf方法
    //有的场景是你同时从两个网站一起查个东西，只要有一个查到了就是完成了,就可以用这个
    @Test
    public void test5() throws InterruptedException {
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油");
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国邮政");
        });
        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);
        cfQuery.thenAccept((result)->{
            System.out.println("code:" + result);
        });
        Thread.sleep(1000);
    }

    static String queryCode(String name) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    public static Double fetchPrice(String code){
        try {
            System.out.println(code);
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }


}

class Task implements Runnable{
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("start task:" + name);
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }
        System.out.println("end task:" + name);
    }
}

//Runnable没有返回值，所以如果需要返回值，我们可以使用Callable
class Task2 implements Callable<String>{

    private final String name;

    public Task2(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        return name;
    }
}