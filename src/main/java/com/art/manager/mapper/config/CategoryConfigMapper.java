package com.art.manager.mapper.config;

import com.art.manager.dto.CategoryConfigDto;
import com.art.manager.pojo.config.CategoryConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryConfigMapper {

    int insert(CategoryConfig record);

    int deleteById(CategoryConfig record);

    int updateNameById(CategoryConfig record);

    List<CategoryConfig> getList(CategoryConfig record);

    String getTypaName(@Param("typeCode") Long typeCode);

    String getStyleName(Long styleCode);

    List<CategoryConfigDto> getStyleList();

    List<CategoryConfig> selectById(String name);

    List<Integer> selectId(String name);
}
