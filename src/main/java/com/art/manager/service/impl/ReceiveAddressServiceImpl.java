package com.art.manager.service.impl;

import com.art.manager.mapper.wechat.ReceiveAddressMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.wechat.ReceiveAddress;
import com.art.manager.request.ReceiveAddressReq;
import com.art.manager.service.ReceiveAddressService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiveAddressServiceImpl implements ReceiveAddressService {

    @Autowired
    private ReceiveAddressMapper receiveAddressMapper;

    @Transactional
    @Override
    public Msg insertAddress(ReceiveAddress receiveAddress) {
        receiveAddress.setCreateTime(new Date());
        receiveAddress.setUpdateTime(new Date());
        if(receiveAddress.isStatus()){
            receiveAddressMapper.updateStatusByStatus(new Date(), false);
        } else {
            ReceiveAddress receiveAddress1 = receiveAddressMapper.selectAddress();
            if(receiveAddress1 == null){
                receiveAddress.setStatus(true);
            }
        }
        receiveAddressMapper.insert(receiveAddress);
        return new Msg(Msg.SUCCESS_CODE, "新增收货地址成功");
    }

    @Transactional
    @Override
    public Msg updateAddress(ReceiveAddress receiveAddress) {
        receiveAddress.setUpdateTime(new Date());
        if(receiveAddress.isStatus()){
            receiveAddressMapper.updateStatusByStatus(new Date(), false);
        }
        receiveAddressMapper.update(receiveAddress);
        return new Msg(Msg.SUCCESS_CODE, "修改收货地址成功");
    }

    @Transactional
    @Override
    public Msg deleteAdress(ReceiveAddressReq req) {
        req.setUpdateTime(new Date());
        List<ReceiveAddress> addressList = receiveAddressMapper.getAddressList(null, req.getId());
        if(addressList != null && addressList.size() > 0){
            ReceiveAddress receiveAddress = addressList.get(0);
            if(receiveAddress.isStatus()){
                receiveAddressMapper.delete(req);
                ReceiveAddress receiveAddress1 = receiveAddressMapper.selectAddress();
                receiveAddress1.setStatus(true);
                receiveAddressMapper.update(receiveAddress1);
                return new Msg(Msg.SUCCESS_CODE, "删除成功");
            }
        }
        receiveAddressMapper.delete(req);
        return new Msg(Msg.SUCCESS_CODE, "删除成功");
    }

    @Transactional
    @Override
    public Msg updateAddressStatus(ReceiveAddressReq req) {
        req.setUpdateTime(new Date());
        receiveAddressMapper.updateStatusByStatus(new Date(), false);
        receiveAddressMapper.updateStatus(req);
        return new Msg(Msg.SUCCESS_CODE, "设置默认收货地址成功");
    }

    @Override
    public Msg selectAddressList(ReceiveAddressReq req) {
        List<ReceiveAddress> addressList = receiveAddressMapper.getAddressList(req.getMobile(), req.getId());
        PageInfo<ReceiveAddress> pageInfo = new PageInfo<>(addressList);
        Map result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("list", addressList);
        return new Msg(Msg.SUCCESS_CODE, result);
    }

}
