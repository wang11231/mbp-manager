package com.art.manager.pojo.schedule;

import lombok.Data;

import java.util.Date;

/**
 * 拍品商品定时任务类
 */
@Data
public class AuctionGoodsShedule {
    private Long id;
    private String preGoodsStatus;
    private String currentGoodsStatus;
    private Date currentTime;
    private String goodsStatusRemark;

}
