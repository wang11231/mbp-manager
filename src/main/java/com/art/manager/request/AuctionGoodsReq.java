package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuctionGoodsReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = -3043370214512298352L;
    private String goodsName;
    private Double startPrice;
    private Double baseAmount;
    private String goodsStatus;
    private String mobile;
    private List<Long> ids = new ArrayList<>();

}
