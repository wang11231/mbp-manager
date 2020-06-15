package com.art.manager.mapper;

import com.art.manager.pojo.SysUser;
import com.art.manager.pojo.SysUserExample;
import com.art.manager.request.SysUserReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysUserMapper {
    int countByExample(SysUserExample example);

    int deleteByExample(SysUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    List<SysUser> selectByExample(SysUserExample example);

    SysUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysUser record, @Param("example") SysUserExample example);

    int updateByExample(@Param("record") SysUser record, @Param("example") SysUserExample example);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByUsername(@Param("username") String username);

    List<SysUser> getList(SysUserReq req);

    int getAdminCount(SysUser record);

    int getCountByUsername(SysUser SysUser);

    String getUsernameById(Long id);

}