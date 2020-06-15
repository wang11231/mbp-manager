package com.art.manager.vo;

import com.art.manager.dto.CommodityDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RecommendVo {

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
    private String commodityId;
    private String sizeStr;
    private String[] commodityIds;
    private List<CommodityDto> selectedList = new ArrayList<>();
}
