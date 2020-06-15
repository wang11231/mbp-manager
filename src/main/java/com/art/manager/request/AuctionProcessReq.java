package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AuctionProcessReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 5498955153635963352L;

    private Long goodsId;
    private String goodsName;
    private String username;

}
