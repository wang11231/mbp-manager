package com.art.manager.service.impl;

import com.art.manager.dto.CommodityDto;
import com.art.manager.dto.CommonCommodityDto;
import com.art.manager.enums.GoodsEnum;
import com.art.manager.enums.OrderNoPrefixEnum;
import com.art.manager.mapper.*;
import com.art.manager.mapper.artist.ArtistInfoMapper;
import com.art.manager.mapper.config.CategoryConfigMapper;
import com.art.manager.mapper.picture.ShowPictureMapper;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Order;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.artist.ArtistInfo;
import com.art.manager.pojo.config.CategoryConfig;
import com.art.manager.pojo.picture.ShowPicture;
import com.art.manager.request.ArtistInfoReq;
import com.art.manager.request.CommodityReq;
import com.art.manager.service.CategoryConfigService;
import com.art.manager.service.CommonCommodityService;
import com.art.manager.service.base.BaseService;
import com.art.manager.util.RandomUtil;
import com.art.manager.vo.CommodityDetail;
import com.art.manager.vo.CommodityVo;
import com.art.manager.vo.RecommendCommodityVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class CommonCommodityServiceImpl extends BaseService implements CommonCommodityService {

    @Autowired
    private CommonCommodityMapper commodityMapper;
    @Autowired
    private CategoryConfigService categoryConfigService;
    @Autowired
    private SpecialMapper specialMapper;
    @Autowired
    private ArtistInfoMapper artistInfoMapper;
    @Autowired
    private ShowPictureMapper showPictureMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CategoryConfigMapper categoryConfigMapper;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCommonCommodity(CommonCommodity commodity) {
        if(StringUtils.isBlank(commodity.getGoodId())){
            throw new RuntimeException("商品id为空");
        }
        //商品验重
        Integer count = commodityMapper.selectByCommdityname(commodity.getCommdityName());
        if(count > 0){
            return -1;
        }
        commodity.setCreateTime(new Date());
        commodity.setUpdateTime(new Date());
        commodity.setStatus("2");
        transUrls(commodity);
        String username = sysUserMapper.getUsernameById(commodity.getUserId());
        commodity.setOperator(username);
        //String goodId = OrderNoPrefixEnum.PT + RandomUtil.getPwd();
        //commodity.setGoodId(goodId);
        if(commodity.getDiscountPrice() == null || commodity.getDiscountPrice().compareTo(new BigDecimal(0)) == 0){
            commodity.setPrice(commodity.getMarkePrice());
        } else {
            commodity.setPrice(commodity.getDiscountPrice());
        }
        commodity.setInterestCount(/*(long)new Random().nextInt(90) +10*/3L);
        commodityMapper.insertCommonCommodity(commodity);
        String[] picturesWorkss = commodity.getPicturesWorkss();
        List<ShowPicture> list = new ArrayList<>();
        for(String s : picturesWorkss){
            ShowPicture showPicture = new ShowPicture();
            showPicture.setCommodityId(commodity.getId());
            showPicture.setPicturesWorks(s);
            list.add(showPicture);
        }
        return showPictureMapper.savePicturUrl(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommonCommodity(CommonCommodity commodity) {
        //商品验重
        //Integer count = commodityMapper.selectByCommdityname(commodity.getCommdityName());
        List<Long> idList = commodityMapper.idList(commodity.getCommdityName());
        if(idList != null && idList.size() > 0){
            for(Long id : idList){
                if(!id.equals(commodity.getId())){
                    return -1;
                }
            }
        }
        /*if(count > 0){
            return -1;
        }*/
        if(commodity == null || commodity.getId() == null){
            throw new RuntimeException("id为空");
        }
        commodity.setUpdateTime(new Date());
        String username = sysUserMapper.getUsernameById(commodity.getUserId());
        commodity.setOperator(username);
        if(commodity.getDiscountPrice() == null || commodity.getDiscountPrice().compareTo(new BigDecimal(0)) == 0){
            commodity.setPrice(commodity.getMarkePrice());
        } else {
            commodity.setPrice(commodity.getDiscountPrice());
        }
        commodityMapper.updateCommonCommodity(commodity);
        String[] picturesWorkss = commodity.getPicturesWorkss();
        List<ShowPicture> list = new ArrayList<>();
        for(String s : picturesWorkss){
            ShowPicture showPicture = new ShowPicture();
            showPicture.setCommodityId(commodity.getId());
            showPicture.setPicturesWorks(s);
            list.add(showPicture);
        }
        showPictureMapper.updateById(commodity.getId());
        return showPictureMapper.savePicturUrl(list);
        /*String[] picturesWorkss = commodity.getPicturesWorkss();
        List<ShowPicture> list = new ArrayList<>();
        for(String s : picturesWorkss){
            ShowPicture showPicture = new ShowPicture();
            showPicture.setCommodityId(commodity.getId());
            showPicture.setPicturesWorks(s);
            list.add(showPicture);
        }
        return showPictureMapper.updateById(list);*/
    }

    @Override
    public Msg getCommonCommodityByCondition(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        String artist = String.valueOf(params.get("artist"));
        List<Long> artists = null;
        if(StringUtils.isNotEmpty(artist) && !artist.equals("null")){
            artists = artistInfoMapper.getArtistName(artist);
        }
        params.put("artistId", artists);
        PageHelper.startPage(pageNum, pageSize);
        List<CommonCommodityDto> commodityList = commodityMapper.selectByCondition(params);
        if(commodityList != null && commodityList.size() > 0){
            commodityList.forEach(item ->{
                if(item.getDiscountPrice() == null){
                    item.setDiscountPrice(new BigDecimal(0));
                }
                String[] showPictures= {item.getShowPicture()};
                item.setShowPictures(showPictures);
                CommonCommodityDto typeByCode = categoryConfigService.getTypeByCode(Long.valueOf(item.getTypeCode()),
                        Long.valueOf(item.getStyleCode()));
                item.setStyleName(typeByCode.getStyleName());
                item.setTypeName(typeByCode.getTypeName());
                String specialName = specialMapper.getSpecialName(item.getSpecialId());
                item.setSpecialField(specialName);
                item.setArtist(artistInfoMapper.getArtistNameById(item.getArtistId()));
                List<String> pictureUrl = showPictureMapper.getPictureUrl(item.getId());
                item.setPicturesWorks(pictureUrl.toArray(new String[pictureUrl.size()]));
                if(item.getStock() > 0){
                    item.setGoodStatus(1);
                } else{
                    item.setGoodStatus(0);
                }
            });
        }
        PageInfo<CommonCommodityDto> pageInfo = new PageInfo<>(commodityList);
        Map result = new HashMap();
        if(commodityList != null && commodityList.size() > 0){
            int pages = Page.getPages(pageInfo.getTotal(), pageSize);
            result.put("total", pageInfo.getTotal());
            result.put("pages", pages);
            result.put("list", commodityList);
        } else{
            result.put("list", new ArrayList<>());
        }
        return new Msg(Msg.SUCCESS_CODE, "查询成功", result);
    }

    @Override
    @Transactional
    public Msg batchDelCommonCommodity(List<Long> ids) {
        commodityMapper.BatchDel(ids);
        return new Msg(Msg.SUCCESS_CODE, "删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg updateStatusById(Map<String, Object> params) {
        String username = sysUserMapper.getUsernameById(Long.valueOf(String.valueOf(params.get("userId"))));
        params.put("operator", username);
        commodityMapper.updateStatus(params);
        return new Msg(Msg.SUCCESS_CODE, "操作成功");
    }

    @Override
    public CommonCommodity getCommodityInfo(CommodityReq req) {
        CommonCommodity commonCommodity = commodityMapper.getCommonCommodity(req.getId());
        String showPicture = commonCommodity.getShowPicture();
        if(commonCommodity.getDiscountPrice() == null){
            commonCommodity.setDiscountPrice(new BigDecimal(0));
        }
        String[] showPictures = {showPicture};
        commonCommodity.setShowPictures(showPictures);
        List<String> pictureUrl = showPictureMapper.getPictureUrl(req.getId());
        if(pictureUrl != null && pictureUrl.size()>0){
            String[] picturesWorkss = pictureUrl.toArray(new String[pictureUrl.size()]);
            commonCommodity.setPicturesWorkss(picturesWorkss);
        }
        return commonCommodity;
    }

    @Override
    public Msg getCommodityById(Long specialId) {
        List<CommodityDto> list= commodityMapper.getCommodityById(specialId);
        if(list != null && list.size() > 0){
            for(CommodityDto commodityDto : list){
                if(commodityDto.getDiscountPrice() == null){
                    commodityDto.setDiscountPrice(new BigDecimal(0));
                }
            }
        }
        return new Msg(Msg.SUCCESS_CODE, list);
    }

    @Override
    public CommodityVo getCommodityVo(Long id) {
        CommodityVo commodityVo = commodityMapper.getCommodityVo(id);
        List<String> pictureUrl = showPictureMapper.getPictureUrl(id);
        commodityVo.setPicturesWorkss(pictureUrl.toArray(new String[pictureUrl.size()]));
        return commodityVo;
    }

    @Override
    public  Map<String, Object> getRecommendCommodityInfoList(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        PageHelper.startPage(pageNum, pageSize);
        List<RecommendCommodityVo> list = recommendMapper.getRecommendCommodityList(params);
        if(list != null && list.size() > 0){
            for(RecommendCommodityVo recommendCommodityVo : list){
                String[] pictureUrl = {recommendCommodityVo.getPicture()};
                recommendCommodityVo.setPictureUrl(pictureUrl);
                if(StringUtils.isNotEmpty(recommendCommodityVo.getCommodityId())){
                    List<CommodityDto> commodiyList = getCommodiyList(recommendCommodityVo.getCommodityId().split(","));
                    recommendCommodityVo.setSelectedList(commodiyList);
                }
            }
        }
        PageInfo<RecommendCommodityVo> pageInfo = new PageInfo<>(list);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", list);
        return result;
    }

    /**
     * 精品推荐商品
     * @param
     * @return
     */
    @Override
    public List<CommonCommodity> getBoutiqueList() {
        List<CommonCommodity> commonCommodity = commodityMapper.getBoutiqueList();
        return commonCommodity;
    }

    @Override
    public List<CommonCommodity> getCommonShopList() {
        PageHelper.startPage(1, 20);
        List<CommonCommodity> commonShopList = commodityMapper.getCommonShopList();
        if(commonShopList != null && commonShopList.size() > 0){
            for(CommonCommodity commodity : commonShopList){
                if(commodity.getDiscountPrice() == null || commodity.getDiscountPrice().equals(0)){
                    commodity.setPrice(commodity.getMarkePrice());
                }
            }
        }
        return commonShopList;
    }

    @Override
    public List<CommonCommodity> getSpecialCommodityList(Map<String, Object> params) {
        List<CommonCommodity> specialCommodityList = commodityMapper.getSpecialCommodityList(params);
        if(specialCommodityList != null && specialCommodityList.size() > 0){
            for(CommonCommodity commodity : specialCommodityList){
                if(commodity.getDiscountPrice() == null){
                    commodity.setDiscountPrice(new BigDecimal(0));
                }
            }
        }
        return specialCommodityList;
    }

    @Override
    public CommodityDetail commodityDetail(CommodityReq req) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy日MM月dd日");
        CommonCommodity commonCommodity = commodityMapper.getCommonCommodityById(req.getId());
        if(commonCommodity == null){
            return null;
        }
        CommodityDetail commodityDetail = new CommodityDetail();
        String showPicture = commonCommodity.getShowPicture();
        String[] showPictures = {showPicture};
        commodityDetail.setShowPictures(showPictures);
        commodityDetail.setId(commonCommodity.getId());
        commodityDetail.setFreight(commonCommodity.getFreight());
        commodityDetail.setCommdityName(commonCommodity.getCommdityName());
        commodityDetail.setInterestCount(commonCommodity.getInterestCount());
        if(commonCommodity.getDiscountPrice() == null){
            commodityDetail.setDiscountPrice(new BigDecimal(0));
        } else {
            commodityDetail.setDiscountPrice(commonCommodity.getDiscountPrice());
        }
        commodityDetail.setMarkePrice(commonCommodity.getMarkePrice());
        commodityDetail.setSpecialId(commonCommodity.getSpecialId());
        commodityDetail.setIntroductionWorks(commonCommodity.getIntroductionWorks());
        commodityDetail.setPrice(commonCommodity.getPrice());
        commodityDetail.setGoodId(commonCommodity.getGoodId());
        List<String> pictureList = showPictureMapper.getPictureUrl(commonCommodity.getId());
        commodityDetail.setPicturesWorkss(pictureList.toArray(new String[pictureList.size()]));
        commodityDetail.setSpecialName(specialMapper.getSpecialName(commonCommodity.getSpecialId()));
        commodityDetail.setStock(commonCommodity.getStock());
        Order order = orderMapper.getOrder(commonCommodity.getCommdityName());
        Map<String, Object> baseInfo = new HashMap<>();
        if(order != null){
            commodityDetail.setBuyName(order.getAccountNumber());
            commodityDetail.setDealTime(format.format(order.getDealTime()));
        }
        Map<String, Object> artistMap = new HashMap<>();
        ArtistInfoReq artistInfoReq = new ArtistInfoReq();
        artistInfoReq.setId(commonCommodity.getArtistId());
        ArtistInfo artistInfo = artistInfoMapper.selectById(artistInfoReq);
        String[] urls = {artistInfo.getUrl()};
        artistMap.put("artistName", artistInfo.getName());
        artistMap.put("urls", urls);
        artistMap.put("desc", artistInfo.getDesc());
        artistMap.put("remark", artistInfo.getRemark());
        baseInfo.put("artistName", artistInfo.getName());
        baseInfo.put("coreSpecification", commonCommodity.getCoreSpecification());
        baseInfo.put("creationYear", commonCommodity.getCreationYear());
        baseInfo.put("theme", commonCommodity.getTheme());
        baseInfo.put("id", commonCommodity.getId());
        commodityDetail.setArtistInfo(artistMap);
        commodityDetail.setBaseInfo(baseInfo);
        setInterestCount(GoodsEnum.COMMON, req.getId(), req.getMobile());
        return commodityDetail;
    }

    @Override
    public Map<String, Object> getSearchList(Map<String, Object> params) {
        String name = (String) params.get("name");
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        PageHelper.startPage(pageNum, pageSize);
        List<CategoryConfig> categoryConfigList = categoryConfigService.selectById(name);
        for(int i =0 ; i<categoryConfigList.size(); i++){
            CategoryConfig categoryConfig = categoryConfigList.get(0);
            params.put("styleCode",categoryConfig.getId());
        }
        List<CommonCommodity> searchList = commodityMapper.getSearchList(params);
        if(searchList != null && searchList.size() > 0){
            for(CommonCommodity commodity : searchList){
                if(commodity.getDiscountPrice() == null){
                    commodity.setDiscountPrice(new BigDecimal(0));
                }
            }
        }
        PageInfo<CommonCommodity> pageInfo = new PageInfo<>(searchList);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", searchList);
        return result;
    }

    @Override
    public Map<String, Object> commodityList(CommodityReq req) {
        List<Integer> styleCodeList = categoryConfigMapper.selectId(req.getStyleName());
        req.setStyleCodeList(styleCodeList);
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<CommonCommodity> commodityList = commodityMapper.commodityList(req);
        if(commodityList != null && commodityList.size() > 0){
            commodityList.forEach(item ->{
                if(item.getDiscountPrice() == null){
                    item.setDiscountPrice(new BigDecimal(0));
                }
                String[] showPictures= {item.getShowPicture()};
                item.setShowPictures(showPictures);
                CommonCommodityDto typeByCode = categoryConfigService.getTypeByCode(Long.valueOf(item.getTypeCode()),
                        Long.valueOf(item.getStyleCode()));
                item.setStyleName(typeByCode.getStyleName());
                item.setTypeName(typeByCode.getTypeName());
                /*String specialName = specialMapper.getSpecialName(item.getSpecialId());
                item.setSpecialField(specialName);*/
                List<String> pictureUrl = showPictureMapper.getPictureUrl(item.getId());
                item.setPicturesWorkss(pictureUrl.toArray(new String[pictureUrl.size()]));
            });
        }

        String specialName = specialMapper.getSpecialName(req.getSpecialId());
        PageInfo<CommonCommodity> pageInfo = new PageInfo<>(commodityList);
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", commodityList);
        if(StringUtils.isNotEmpty(specialName)){
            result.put("specialName", specialName);
        }
        return result;
    }

    /**
     * 转换urls->url
     * @param commodity
     */
    private void transUrls(CommonCommodity commodity){
        if(commodity == null){
            return;
        }
        String[] urls = commodity.getShowPictures();
        if(urls == null || urls.length == 0){
            return;
        }
        commodity.setShowPicture(urls[0]);
    }

    public List<CommodityDto> getCommodiyList(String[] commodityIds){
        List<CommodityDto> selectedList = new ArrayList<>();
        for(String str : commodityIds){
            CommodityDto commodity = commodityMapper.getCommodityName(Long.valueOf(str));
            if(commodity != null && commodity.getDiscountPrice() == null){
                commodity.setDiscountPrice(new BigDecimal(0));
            }
            if(commodity != null){
                selectedList.add(commodity);
            }
        }
        return selectedList;
    }

}
