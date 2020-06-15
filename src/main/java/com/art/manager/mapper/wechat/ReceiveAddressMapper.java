package com.art.manager.mapper.wechat;

import com.art.manager.pojo.wechat.ReceiveAddress;
import com.art.manager.pojo.wechat.WeChatUser;
import com.art.manager.request.ReceiveAddressReq;
import com.art.manager.vo.WeChatUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface ReceiveAddressMapper {

    int insert(ReceiveAddress receiveAddress);

    int update(ReceiveAddress receiveAddress);

    int delete(ReceiveAddressReq req);

    int updateStatus(ReceiveAddressReq req);

    List<ReceiveAddress> getAddressList(@Param("mobile") String mobile, @Param("id") Long id);

    int updateStatusByStatus(@Param("updateTime") Date updateTime, @Param("status") boolean status);

    ReceiveAddress selectAddress();

    ReceiveAddress selectById(ReceiveAddress req);
}
