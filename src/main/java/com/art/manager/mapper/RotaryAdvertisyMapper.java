package com.art.manager.mapper;

import com.art.manager.dto.RotaryAdvertisyDto;
import com.art.manager.pojo.RotaryAdvertisy;
import com.art.manager.vo.RotaryAdvertisyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface RotaryAdvertisyMapper {
    List<RotaryAdvertisyVo> getList(Map<String, Object> params);

    RotaryAdvertisy getSortById(Long id);

    RotaryAdvertisy getUpData(Integer sort);

    RotaryAdvertisy getNextData(Integer sort);

    int updateByPrimaryKeySelective(RotaryAdvertisy rotaryAdvertisy);

    int updateStatusById(@Param("status") Integer status, @Param("id") Long id, @Param("sort") Integer sort);

    int insert(RotaryAdvertisy rotaryAdvertisy);

    int selectSort(Integer status);

    int delete(@Param("ids") List<Long> ids);

    int update(RotaryAdvertisy rotaryAdvertisy);

    Long selectId();

    List<RotaryAdvertisyDto> queryAll();
}
