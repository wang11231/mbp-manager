package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理出价上限实体类
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuctionProxyLimit implements Serializable {
    private static final long serialVersionUID = 1367832387490833127L;
    private Long id;
    private Long userId;
    private String username;
    private Long goodsId;
    private BigDecimal priceLimit;
    private String startDate;
    private String endDate;
    private Date nextEndDate;
    private Integer proxyStatus;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    /**
     * 领先状态，true是领先，默认false
     */
    private Boolean leaderStatus = false;
    private String remark;
    private Date currentTime;
    private Long addrId;

}
