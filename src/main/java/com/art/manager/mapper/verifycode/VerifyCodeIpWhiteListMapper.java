package com.art.manager.mapper.verifycode;

import com.art.manager.pojo.verifycode.VerifyCodeIpWhiteList;
import com.art.manager.pojo.verifycode.VerifyCodeIpWhiteListExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VerifyCodeIpWhiteListMapper {
    int countByExample(VerifyCodeIpWhiteListExample example);

    int deleteByExample(VerifyCodeIpWhiteListExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VerifyCodeIpWhiteList record);

    int insertSelective(VerifyCodeIpWhiteList record);

    List<VerifyCodeIpWhiteList> selectByExample(VerifyCodeIpWhiteListExample example);

    VerifyCodeIpWhiteList selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VerifyCodeIpWhiteList record, @Param("example") VerifyCodeIpWhiteListExample example);

    int updateByExample(@Param("record") VerifyCodeIpWhiteList record, @Param("example") VerifyCodeIpWhiteListExample example);

    int updateByPrimaryKeySelective(VerifyCodeIpWhiteList record);

    int updateByPrimaryKey(VerifyCodeIpWhiteList record);
}