package com.art.manager.mapper.verifycode;

import com.art.manager.pojo.verifycode.VerifyCode;
import com.art.manager.pojo.verifycode.VerifyCodeExample;
import com.art.manager.pojo.verifycode.VerifyCodeIp;
import com.art.manager.pojo.verifycode.VerifyCodeIpWhiteList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface VerifyCodeMapper {
    int countByExample(VerifyCodeExample example);

    int deleteByExample(VerifyCodeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VerifyCode record);

    int insertSelective(VerifyCode record);

    List<VerifyCode> selectByExample(VerifyCodeExample example);

    VerifyCode selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VerifyCode record, @Param("example") VerifyCodeExample example);

    int updateByExample(@Param("record") VerifyCode record, @Param("example") VerifyCodeExample example);

    int updateByPrimaryKeySelective(VerifyCode record);

    int updateByPrimaryKey(VerifyCode record);

    /**
     * 根据ip和时间查询
     * @param verifyCodeIp
     * @return
     */
    VerifyCodeIp selectVerifyCodeIpByIp(VerifyCodeIp verifyCodeIp);

    /**
     * 插入ip
     * @param verifyCodeIp
     */
    void insertVerifyCodeIp(VerifyCodeIp verifyCodeIp);

    /**
     * 修改次数
     * @param verifyCodeIp
     * @return
     */
    int updateVerifyCodeIpByIp(VerifyCodeIp verifyCodeIp);

    /**
     * 查询白名单
     * @param verifyCodeIpWhiteList
     * @return
     */
    List<VerifyCodeIpWhiteList> selectWhiteListByIp(VerifyCodeIpWhiteList verifyCodeIpWhiteList);

    /**
     * 查询验证码
     * @param verifyCode
     * @return
     */
    VerifyCode selectVerifyCode(VerifyCode verifyCode);

    /**
     * 插入验证码
     * @param verifyCode
     * @return
     */
    void insertVerifyCode(VerifyCode verifyCode);

}