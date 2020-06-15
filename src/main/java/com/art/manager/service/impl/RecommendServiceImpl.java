package com.art.manager.service.impl;

import com.art.manager.dto.CommodityDto;
import com.art.manager.dto.RecommendDto;
import com.art.manager.mapper.CommonCommodityMapper;
import com.art.manager.mapper.RecommendMapper;
import com.art.manager.mapper.SysUserMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.Recommend;
import com.art.manager.pojo.RotaryAdvertisy;
import com.art.manager.request.RecommendReq;
import com.art.manager.service.RecommendService;
import com.art.manager.vo.RecommendVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CommonCommodityMapper commodityMapper;
    @Override
    public Map<String, Object> getRecommendList(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        PageHelper.startPage(pageNum, pageSize);
        List<RecommendVo> list = recommendMapper.getList(params);
        if(list != null && list.size() > 0){
            for(RecommendVo recommend : list){
                String[] pictureUrl = {recommend.getPicture()};
                recommend.setPictureUrl(pictureUrl);
                if(recommend.getSize() != null && recommend.getSize() == 1){
                    recommend.setSizeStr("小图");
                } else if(recommend.getSize() != null && recommend.getSize() == 2){
                    recommend.setSizeStr("大图");
                }
                if(StringUtils.isNotEmpty(recommend.getCommodityId())){
                    recommend.setCommodityIds(recommend.getCommodityId().split(","));
                    List<CommodityDto> commodiyList = getCommodiyList(recommend.getCommodityId().split(","));
                    recommend.setSelectedList(commodiyList);
                }
            }
        }
        PageInfo<RecommendVo> pageInfo = new PageInfo<>(list);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", list);
        return result;
    }

    @Transactional
    @Override
    public int updateStatus(RecommendReq req) {
        String username = sysUserMapper.getUsernameById(req.getUserId());
        return recommendMapper.updateStatusById(req.getStatus(), req.getId(), username);
    }

    @Override
    public int insertRecommend(Recommend recommend) {
        recommend.setCreateTime(new Date());
        recommend.setUpdateTime(new Date());
        recommend.setStatus(0);
        transUrls(recommend);
        if(recommend.getCommodityIds() != null && recommend.getCommodityIds().length > 0){
            recommend.setCommodityId(StringUtils.join(recommend.getCommodityIds(), ","));
        }
        String username = sysUserMapper.getUsernameById(recommend.getUserId());
        recommend.setOperator(username);
        return recommendMapper.insert(recommend);
    }

    @Override
    public Map<String, Object> getRecommend() {
        List<RecommendDto> recommendList = recommendMapper.getRecommend();
        List<RecommendDto> bigSizeList = new ArrayList<>();
        List<RecommendDto> smallList = new ArrayList<>();
        for (int i = 0; i< recommendList.size(); i++){
            RecommendDto recommendDto = recommendList.get(i);
            if(2 == recommendDto.getSize()){
                bigSizeList.add(recommendDto);
            }else if (1 == recommendDto.getSize()){
                smallList.add(recommendDto);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("bigList",bigSizeList);
        map.put("smallList",smallList);
        return  map;
    }

    @Transactional
    @Override
    public int delete(List<Long> ids) {
        return recommendMapper.delete(ids);
    }

    @Override
    public int update(Recommend recommend) {
        recommend.setUpdateTime(new Date());
        transUrls(recommend);
        String username = sysUserMapper.getUsernameById(recommend.getUserId());
        recommend.setOperator(username);
        if(recommend.getCommodityIds() != null && recommend.getCommodityIds().length > 0){
            recommend.setCommodityId(StringUtils.join(recommend.getCommodityIds(), ","));
        } else {
            recommend.setCommodityId("");
        }
        return recommendMapper.update(recommend);
    }

    /**
     * 转换urls->url
     * @param recommend
     */
    private void transUrls(Recommend recommend){
        if(recommend == null){
            return;
        }
        String[] urls = recommend.getPictureUrl();
        if(urls == null || urls.length == 0){
            return;
        }
        recommend.setPicture(urls[0]);
    }

    /**
     * 通过商品id查询商品名称
     */

    public List<CommodityDto> getCommodiyList(String[] commodityIds){
        List<CommodityDto> selectedList = new ArrayList<>();
        for(String str : commodityIds){
            CommodityDto commodityName = commodityMapper.getCommodityName(Long.valueOf(str));
            selectedList.add(commodityName);
        }
        return selectedList;
    }
}
