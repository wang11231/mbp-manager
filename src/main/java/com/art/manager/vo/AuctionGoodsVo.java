package com.art.manager.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuctionGoodsVo implements Serializable {
    private static final long serialVersionUID = 558281637366978725L;
    private Long id;
    private String  goodsName;
    private String  auctionGoodsId;
    private String  showPic;
    private BigDecimal  startPrice;
    private BigDecimal  baseAmount;
    private BigDecimal  incrementStep;
    private BigDecimal  onePrice;
    private Date startDate;
    private Date endDate;
    private String  goodsStatus;
    private String  username;
    private Long styleCode;
    private String categoryName;
    private Long artistId;
    private String artistName;
    private String createYear;
    private String subject;
    private String specification;
    private BigDecimal transportAmount;
    private String userId;
    private String useScene;
    private String introduction;
    private List<String> showPics = new ArrayList();
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private List<String> auctionPics = new ArrayList();
    private Long  typeCode;
    private String parentName;
    private Integer settingStatus;
    private String warrantUrl;
    private List<String> warrantUrls = new ArrayList();
    private String credentialUrl;
    private List<String> credentialUrls = new ArrayList();
    private BigDecimal currentPrice;
    private BigDecimal priceLimit;
    private String desc;
    private Date currentTime;
    private String buyerName;
    private String payDate;
    private Integer autoProxy;
    private Long interestCount;
}
