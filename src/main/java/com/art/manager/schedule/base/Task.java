package com.art.manager.schedule.base;

import lombok.Data;

/**
 * 定时任务基础类
 */
@Data
public abstract class Task implements Runnable{
    /**
     * 任务号：id或订单号
     */
    private String taskNo;

    @Override
    public void run() {
        execute();
    }

    public abstract void execute();
}
