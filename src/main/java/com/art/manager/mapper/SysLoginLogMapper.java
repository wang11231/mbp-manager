package com.art.manager.mapper;

import com.art.manager.pojo.SysLoginLog;
import com.art.manager.pojo.SysLoginLogExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysLoginLogMapper {
    int countByExample(SysLoginLogExample example);

    int deleteByExample(SysLoginLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysLoginLog record);

    int insertSelective(SysLoginLog record);

    List<SysLoginLog> selectByExample(SysLoginLogExample example);

    SysLoginLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysLoginLog record, @Param("example") SysLoginLogExample example);

    int updateByExample(@Param("record") SysLoginLog record, @Param("example") SysLoginLogExample example);

    int updateByPrimaryKeySelective(SysLoginLog record);

    int updateByPrimaryKey(SysLoginLog record);
}