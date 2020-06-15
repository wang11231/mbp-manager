package com.art.manager.mapper.wechat;

import com.art.manager.dto.WechatUserDto;
import com.art.manager.pojo.wechat.WeChatUser;
import com.art.manager.request.WechatUserReq;
import com.art.manager.vo.WeChatUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WeChatUserMapper {
    int saveWechatUser(WeChatUser weChatUser);

    WeChatUser selectMobile(String openid);

    int updateByopenid(WeChatUser weChatUser);

    WeChatUserVo selectWechatUser(@Param("mobile") String mobile, @Param("openid") String openid);

    int updateMobile(WeChatUser weChatUser);

    int updateOpenid(WeChatUser weChatUser);

    List<WechatUserDto> getWechatUserList(WechatUserReq req);

    int updateByMobile(WeChatUser weChatUser);

    int updateStatus(@Param("status") String status, @Param("mobile") String mobile, @Param("lastLoginTime") Date lastLoginTime);

    String selectStatus(String mobile);

    String getBalance(String mobile);

    int updateBalanceByMobile(@Param("mobile") String mobile, @Param("balance") BigDecimal balance);

    String selectBalance(String openid);

    String getBond(String mobile);

    int updateBondByMobile(@Param("mobile") String mobile, @Param("bond") BigDecimal bond);

    int updateBoneAndBalance(@Param("mobile") String mobile, @Param("bond") BigDecimal bond, @Param("balance") BigDecimal balance);

    int updateBalanceToPayOrder(@Param("mobile") String mobile, @Param("balance") BigDecimal balance);

    int updateBoneAndBalanceByMobile(@Param("mobile") String mobile, @Param("bond") BigDecimal bond, @Param("balance") BigDecimal balance);

    int backUserBaseAmount(@Param("goodsId") Long goodsId);

    int backUserBaseAmountWithoutSuccess(Map map);

    int updateUserBondAndDeduct(Map params);

    int delWechatUser(Long id);

    int updateBalanceByOpenid(@Param("openid") String openid, @Param("balance") BigDecimal balance);

}
