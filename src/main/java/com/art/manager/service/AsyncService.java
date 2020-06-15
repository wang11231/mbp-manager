package com.art.manager.service;

import com.art.manager.config.Executors;
import com.art.manager.schedule.base.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {

    @Async("asyncServiceExecutor")
    public String Async(){
        log.info("{} start", Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("{} end", Thread.currentThread().getName());
        return null;
    }

    public String pool(){
        try {
            Task task1 = new Task1();
            task1.setTaskNo("2019000FDF");

            Executors.threadPool.execute(
                    task1
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class Task1 extends Task{

        @Override
        public void execute() {
            log.info("{} start", Thread.currentThread().getName());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("{} end", Thread.currentThread().getName());

        }
    }

}
