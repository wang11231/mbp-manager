package com.art.manager.service;

import com.art.manager.dto.CategoryConfigDto;
import com.art.manager.dto.CommonCommodityDto;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.config.CategoryConfig;
import com.art.manager.vo.CategoryConfigVo;

import java.util.List;
import java.util.Map;

public interface CategoryConfigService {

    boolean insert(List<CategoryConfigVo> list);

    List<CategoryConfigVo> getList(CategoryConfig record);

    boolean deleteByCode(CategoryConfigVo vo);

    List<CategoryConfigVo> getFirstList();

    List<CategoryConfigVo> getSecondList(CategoryConfigVo vo);

    CommonCommodityDto getTypeByCode(Long typeCode, Long styleCode);

    List<CategoryConfigDto> getStyleList();

    List<CategoryConfig> selectById(String name);
}
