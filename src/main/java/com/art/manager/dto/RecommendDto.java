package com.art.manager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class RecommendDto {

    private Long id;
    private String picture; // 广告预览图
    private String advertisyUrl; // 广告连接
    private Integer size;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String commodityId;

    //List<CommodityDto>
}
