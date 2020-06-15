package com.art.manager.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RotaryAdvertisyVo {
    private Long id;
    private String[] pictureUrl; // 广告预览图
    private String picture;
    private String advertisyUrl; // 广告连接
    private Date createTime;
    private Date updateTime;
    private String operator; // 创建者
    private Integer status; // 状态  0：下线  1：上线
    private Integer sort;
    private boolean isLast; // 是否线上最后一条数据
}
