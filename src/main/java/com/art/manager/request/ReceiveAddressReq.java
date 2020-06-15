package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReceiveAddressReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = -400805628768132016L;

    private Long id;
    private Long userId;
    private List<Long> ids = new ArrayList<>();
    private boolean status; // true:默认收货地址
    private Date updateTime;
    private String mobile;
}
