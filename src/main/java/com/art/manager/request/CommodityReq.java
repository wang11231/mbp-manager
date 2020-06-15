package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommodityReq extends BaseReq implements Serializable {
    private List<Long> ids = new ArrayList<>();
    private Long id;

    private Long specialId;
    /**
     *  艺术家
     */
    private String artist;

    private BigDecimal discountPrice; // 折扣价

    private Integer typeCode; // 类别
    private Integer styleCode; // 风格
    private List<Integer> styleCodeList; // 风格集合
    private String styleName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer sort; // 1:降序 0：升序
    private String isBuy;//1:未售 0：已售
    /**
     * 是否是精品推荐
     */
    private String isRecommend;
    //手机号
    private String mobile;

}
