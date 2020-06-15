package com.art.manager.vo;

import com.art.manager.dto.CommodityDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RecommendCommodityVo {

    private Long id;
    private String[] pictureUrl; // 广告预览图
    private String picture;
    private String advertisyUrl; // 广告连接
    private Integer type; // 1:关联商品推荐位 2: 普通推荐位
    private Date createTime;
    private Date updateTime;
    private String operator; // 创建者
    private String commodityId;
    private List<CommodityDto> selectedList = new ArrayList<>();
}
