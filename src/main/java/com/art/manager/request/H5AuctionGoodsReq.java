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
public class H5AuctionGoodsReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 6175663609912376063L;
    private String dayType;
    private String timeType;
    private String mobile;

}
