package com.art.manager.service;

import com.art.manager.dto.RotaryAdvertisyDto;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.RotaryAdvertisy;
import com.art.manager.pojo.SysUser;

import java.util.List;
import java.util.Map;

public interface RotaryAdvertisyService {

    Map<String, Object> getRotaryAdvertisy(Map<String, Object> params);

    Msg moveUp(Long id);

    Msg moveDown(Long id);

    int updateStatus(Integer status, Long id);

    int insert(RotaryAdvertisy rotaryAdvertisy);

    int delete(List<Long> ids);

    int update(RotaryAdvertisy rotaryAdvertisy);

    List<RotaryAdvertisyDto> queryAll();
}
