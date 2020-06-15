package com.art.manager.controller;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Created with eclipse.
 * DFS工具类
 * <li>1、方法一：请在此区域内填写方法描述</li>
 * <li>2、方法二：请在此区域内填写方法描述</li>
 * User:ouxitao Date: 2015年1月1日 time:上午10:34:07
 */
@Slf4j
public class DfsFileDealUtils implements Serializable {

    private final static DfsFileDealUtils instance = new DfsFileDealUtils();
    private static final long serialVersionUID = 6092401199748325412L;

    private DfsFileDealUtils(){
        init();
    }

    public static DfsFileDealUtils getInstance(){
        return instance;
    }

    public void init(){
        System.out.println("正常不.............");
    }


}
