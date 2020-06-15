package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AuctionProcess implements Serializable {
    private static final long serialVersionUID = -8998595041778285776L;

    private Long id ;
    @NotNull(message = "商品id不能为空")
    private Long goodsId;
    @NotNull(message = "商品名不能为空")
    private String goodsName;
    @NotNull(message = "用户id不能为空")
    private Long userId ;
    @NotNull(message = "用户名不能为空")
    private String username;
    private BigDecimal currentPrice;
    @NotNull(message = "加价形式不能为空")
    private String addPriceType ;
    private String processStatus ;
    private String remark ;
    private Integer status ;
    private Date createTime;
    private Date updateTime;
    private Long addrId;

}
