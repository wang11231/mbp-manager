package com.art.manager.mapper;

import com.art.manager.dto.CommodityDto;
import com.art.manager.dto.CommonCommodityDto;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.request.CommodityReq;
import com.art.manager.vo.CommodityVo;
import com.art.manager.vo.CommonCommodityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CommonCommodityMapper {

    // 新增普通商品
    int insertCommonCommodity(CommonCommodity commodity);
    // 查询普通商品列表
    List<CommonCommodityDto> selectByCondition(Map<String, Object> params);
    // 修改普通商品
    int updateCommonCommodity(CommonCommodity commodity);

    List<CommonCommodityDto> selectAll();

    int BatchDel(@Param("ids") List<Long> ids);

    int updateStatus(Map<String, Object> params);

    List<CommonCommodityVo> getListBySpecialId(List<Long> list);

    int delectCommodity(List<Long> list);

    CommonCommodity getCommonCommodity(Long id);

    List<CommodityDto> getCommodityById(Long specialId);
    CommodityVo getCommodityVo(Long id);

    CommodityDto getCommodityName(Long id);

    void getCommodityList(List idsList);

    List<CommonCommodity> getBoutiqueList();

    List<CommonCommodity> getCommonShopList();

    List<CommonCommodity> getSpecialCommodityList(Map<String, Object> params);

    CommonCommodity getCommonCommodityById(Long id);

    Integer selectByCommdityname(String commdityName);

    List<CommonCommodity> getSearchList(Map<String, Object> params);

    CommonCommodity selectCommonCommodity(String id);

    List<CommonCommodity> commodityList(CommodityReq req);

    CommonCommodity getCommodityByName(String commdityName);

    List<Long> idList(String commdityName);

    int updateStock(@Param("id") Long id, @Param("stock") Integer stock, @Param("updateTime")Date updateTime);

    int updateInterestCount(@Param("id") Long id, @Param("interestCount") Long interestCount);

}
