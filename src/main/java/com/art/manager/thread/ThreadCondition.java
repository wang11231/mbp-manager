package com.art.manager.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/24 14:35
 * @Description: todo
 */
public class ThreadCondition {

    private Lock lock = new ReentrantLock();
    private Condition Condition1= lock.newCondition();
    private Condition Condition2= lock.newCondition();
    private Condition Condition3= lock.newCondition();
    private int num = 0;
    private void run(Condition current, Condition next){
        lock.lock();
        try{
            while(num < 99){
                System.out.println(Thread.currentThread().getName() + "， num=" + ++num);
                next.signal();
                current.await();
            }
            next.signal();
        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ThreadCondition condition = new ThreadCondition();
        List<Runnable> list = new ArrayList<>();
        list.add(()->condition.run(condition.Condition1, condition.Condition2));
        list.add(()->condition.run(condition.Condition2, condition.Condition3));
        list.add(()->condition.run(condition.Condition3, condition.Condition1));
        for(Runnable run:list){
            new Thread(run).start();
        }
    }




}
