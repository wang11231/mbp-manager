package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 拍卖商品作品图实体类
 */
@Getter
@Setter
@NoArgsConstructor
public class AuctionPic implements Serializable {

    private static final long serialVersionUID = 7486585434710727991L;

    private Long id;

    private Long auctionId;

    private Long nextAuctionId;

    private String worksUrl;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
