package com.art.manager.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Recommend {

    private Long id;
    private String[] pictureUrl; // 广告预览图
    private String picture;
    private String advertisyUrl; // 广告连接
    private Integer type; // 1:关联商品推荐位 2: 普通推荐位
    private Integer size;
    private Date createTime;
    private Date updateTime;
    private String operator; // 创建者
    private Integer status; // 状态  0：下线  1：上线
    private String[] commodityIds;
    private String commodityId;
    private Long userId;
}
