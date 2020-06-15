package com.art.manager.service;

import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.request.CommodityReq;
import com.art.manager.vo.CommodityDetail;
import com.art.manager.vo.CommodityVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CommonCommodityService {

    int saveCommonCommodity(CommonCommodity commodity);
    int updateCommonCommodity(CommonCommodity commodity);
    Msg getCommonCommodityByCondition(Map<String, Object> params);
    Msg batchDelCommonCommodity(List<Long> ids);
    Msg updateStatusById(Map<String, Object> params);

    CommonCommodity getCommodityInfo(CommodityReq req);

    Msg getCommodityById(Long specialId);

    CommodityVo getCommodityVo(Long id);


    Map<String, Object> getRecommendCommodityInfoList(Map<String, Object> params);

    List<CommonCommodity> getBoutiqueList();

    List<CommonCommodity> getCommonShopList();

    List<CommonCommodity> getSpecialCommodityList(Map<String, Object> params);

    CommodityDetail commodityDetail(CommodityReq req);

    Map<String, Object> getSearchList(Map<String, Object> params);

    Map<String, Object> commodityList(CommodityReq req);
}
