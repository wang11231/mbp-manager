package com.art.manager.config;

import com.art.manager.schedule.base.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
//@EnableAsync
@Slf4j
public class Executors {

    private static int CORE_POOL_SIZE = 10;
    private static int MAX_POOL_SIZE = 20;
    private static int QUEUE_CAPACITY = 10;
    private static String NAME_PREFIX = "ART_";
    public static ExecutorService threadPool;
    static{
        threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE
                , MAX_POOL_SIZE
                , 60L
                , TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(QUEUE_CAPACITY)
                , new ArtThreadFactory(NAME_PREFIX)
                , new ArtRejectedExecutionHandler());
    }

    /*@Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        //配置最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        //配置队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(NAME_PREFIX);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ArtRejectedExecutionHandler());
        //执行初始化
        executor.initialize();
        return executor;
    }*/

    static class ArtThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        ArtThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = name + "-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    static class ArtRejectedExecutionHandler implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if(r instanceof Task){
                Task task = (Task)r;
                log.error("error，TaskNo:{}", task.getTaskNo());



            }else{
                log.error("task is not corret，theadName:{}", Thread.currentThread().getName());
            }

        }
    }

}
