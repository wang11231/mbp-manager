package com.art.manager.mapper;

import com.art.manager.dto.SpecialDto;
import com.art.manager.pojo.Special;
import com.art.manager.pojo.SpecialType;
import com.art.manager.vo.SpecialAndTypeVo;
import com.art.manager.vo.SpecialVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SpecialMapper {

    List<SpecialVo> getList(Map<String, Object> map);

    List<SpecialAndTypeVo> getSpecialList(Long id);

    int insertSpecialType(SpecialType specialType);

    int insertSpecial(Special special);

    int remove(Long commodityId);

    String getSpecialName(Long id);

    int updateSpecialType(SpecialType specialType);

    int updateSpecial(Special special);

    int deleteSpecialTypeByIds(List<Long> list);

    int deleteSpecialByIds(List<Long> list);

    List<Long> selectSpecialById(List<Long> list);

    List<SpecialDto> selectSpecial();

    Special getSpecialById(Long id);


    List<SpecialType> getTencentSpecialList();

    List specialCommonList(Integer id);
}
