package com.learning.TikaTask.app;


import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.*;
import java.util.Date;

class Service implements Callable{
    int i;
    public Service(int i){
        this.i = i;

    }

    @Override
    public String call() {
        System.out.println(i+" ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hi"+i;
    }
}
public class MultiThreadingTask {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        List<Future<String>> futureList = new ArrayList<>();
        System.out.println(new Date());
        for(int i=0; i<25; i++){
            futureList.add((Future<String>) es.submit(new Service(i)));
        }
        es.shutdown();
        es.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(new Date());

        for(Future<String> future: futureList){
            System.out.println(future.get());
        }

    }
}
