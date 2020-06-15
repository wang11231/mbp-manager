package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 拍卖商品实体类
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuctionGoods implements Serializable {
    private static final long serialVersionUID = 4753684263690698306L;

    private Long id;
    private String auctionGoodsId;
    private String goodsName;
    private BigDecimal startPrice;
    private BigDecimal baseAmount;
    private BigDecimal incrementStep;
    private BigDecimal onePrice;
    private String goodsStatus;
    private Long styleCode;
    private String categoryName;
    private Long artistId;
    private String artistName;
    private String createYear;
    private String subject;
    private String specification;
    private BigDecimal transportAmount;
    private String userId;
    private String username;
    private String useScene;
    private String introduction;
    private String showPic;
    private List<String> showPics = new ArrayList();
    private String startDate;
    private String endDate;
    private Date nextEndDate;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String goodsStatusRemark;
    private List<String> auctionPics = new ArrayList();
    private Integer settingStatus;
    private Integer resetStatus;
    private Integer autoProxy;
    private Long interestCount;

}
