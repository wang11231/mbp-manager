package com.art.manager.service;

import com.art.manager.dto.SpecialDto;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Special;
import com.art.manager.pojo.SpecialType;
import com.art.manager.request.SpecialReq;

import java.util.List;
import java.util.Map;

public interface SpecialService {

    Msg getList(Map<String, Object> map);
    Msg getSpecialList(Map<String, Object> params);

    int insertSpecialType(SpecialType specialType);

    int insertSpecial(Special special);

    int removeCommondity(Long commodityId);

    int updateSpecialType(SpecialType specialType);

    int updateSpecial(Special special);

    int deleteSpecialType(SpecialReq req);

    int deleteSpecial(List<Long> list);

    List<SpecialDto> selectSpecial();

    Special getSpecialById(SpecialReq req);

    List<Special> getTencentSpecial(Map<String, Object> params);

    List<SpecialType> getTencentSpecialList();

}
