package com.art.manager.mapper;

import com.art.manager.pojo.FieldDict;
import com.art.manager.pojo.FieldDictExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FieldDictMapper {
    int countByExample(FieldDictExample example);

    int deleteByExample(FieldDictExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FieldDict record);

    int insertSelective(FieldDict record);

    List<FieldDict> selectByExample(FieldDictExample example);

    FieldDict selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FieldDict record, @Param("example") FieldDictExample example);

    int updateByExample(@Param("record") FieldDict record, @Param("example") FieldDictExample example);

    int updateByPrimaryKeySelective(FieldDict record);

    int updateByPrimaryKey(FieldDict record);
}