package com.art.manager.mapper.verifycode;

import com.art.manager.pojo.verifycode.VerifyCodeIp;
import com.art.manager.pojo.verifycode.VerifyCodeIpExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VerifyCodeIpMapper {
    int countByExample(VerifyCodeIpExample example);

    int deleteByExample(VerifyCodeIpExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VerifyCodeIp record);

    int insertSelective(VerifyCodeIp record);

    List<VerifyCodeIp> selectByExample(VerifyCodeIpExample example);

    VerifyCodeIp selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VerifyCodeIp record, @Param("example") VerifyCodeIpExample example);

    int updateByExample(@Param("record") VerifyCodeIp record, @Param("example") VerifyCodeIpExample example);

    int updateByPrimaryKeySelective(VerifyCodeIp record);

    int updateByPrimaryKey(VerifyCodeIp record);
}