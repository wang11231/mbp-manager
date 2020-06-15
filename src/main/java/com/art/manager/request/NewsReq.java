package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class NewsReq extends BaseReq implements Serializable {
    private Long id;
    private Integer status;
    private List<Long> ids = new ArrayList<>();
    private Long userId;
}
