package com.art.manager.service;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.wechat.ReceiveAddress;
import com.art.manager.request.ReceiveAddressReq;

public interface ReceiveAddressService {

    Msg insertAddress(ReceiveAddress receiveAddress);

    Msg updateAddress(ReceiveAddress receiveAddress);

    Msg deleteAdress(ReceiveAddressReq req);

    Msg updateAddressStatus(ReceiveAddressReq req);

    Msg selectAddressList(ReceiveAddressReq req);
}
