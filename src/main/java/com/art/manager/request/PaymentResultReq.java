package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaymentResultReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 5272077793425513274L;

    private String mobile;
    private String outTradeNo;
    private String status;
    private List<Integer> paymentTypes;

}
