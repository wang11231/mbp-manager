package com.art.manager.wechatcontroller;

import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Special;
import com.art.manager.pojo.SpecialType;
import com.art.manager.request.CommodityReq;
import com.art.manager.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/homepage")
public class WechatHomePageController {

    @Autowired
    private RotaryAdvertisyService rotaryAdvertisyService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private CommonCommodityService commonCommodityService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private SpecialService specialService;
    /**
     *  banner
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/getBannerInfo", method = RequestMethod.GET)
    public Msg getBannerInfo(HttpServletRequest req, HttpServletResponse resp){
        try {
            return new Msg(Msg.SUCCESS_CODE, rotaryAdvertisyService.queryAll());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 普通推荐位
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/getRecommend", method = RequestMethod.GET)
    public Msg getRecommend(HttpServletRequest req, HttpServletResponse resp){
        try {
            return new Msg(Msg.SUCCESS_CODE, recommendService.getRecommend());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 普通推荐位
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/getRelationRecommend", method = RequestMethod.GET)
    public Msg getRelationRecommend(HttpServletRequest req, HttpServletResponse resp){
        try {
            return new Msg(Msg.SUCCESS_CODE, recommendService.getRecommend());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/getCommodityInfo", method = RequestMethod.GET)
    public Msg getCommodityInfo(@RequestBody CommodityReq req){
        try {
            return new Msg(Msg.SUCCESS_CODE, commonCommodityService.getCommodityVo(req.getId()));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 关联商品推荐位
     * @param req
     * @param
     * @return
     */

    @RequestMapping(value = "/getRecommendCommodityInfoList", method = RequestMethod.POST)
    public Msg getRecommendCommodityInfoList(@RequestBody Map<String, Object> params, HttpServletRequest req, HttpServletResponse resp){
        try {
            Map<String, Object> recommendCommodityInfoList = commonCommodityService.getRecommendCommodityInfoList(params);
            return new Msg(Msg.SUCCESS_CODE,recommendCommodityInfoList);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 精品推荐商品
     */
    @RequestMapping(value = "/getBoutiqueList", method = RequestMethod.GET)
    public Msg getBoutiqueList(HttpServletRequest req, HttpServletResponse resp){
        try {
            List<CommonCommodity> boutiqueList = commonCommodityService.getBoutiqueList();
            return new Msg(Msg.SUCCESS_CODE,boutiqueList);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 普通商品 前20条查询
     */
    @RequestMapping(value = "/getCommonShopList", method = RequestMethod.GET)
    public Msg getCommonShopList(HttpServletRequest req, HttpServletResponse resp){
        try {
            List<CommonCommodity> getCommonShopList = commonCommodityService.getCommonShopList();
            return new Msg(Msg.SUCCESS_CODE,getCommonShopList);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 首页新闻列表
     *
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/getNewsList", method = RequestMethod.GET)
    public Msg getNewsList(HttpServletRequest request) {
        try {
            return new Msg(Msg.SUCCESS_CODE, "成功", newsService.getNewsList());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 公众号专场类型列表
     */
    @RequestMapping(value = "/getTencentSpecialList", method = RequestMethod.GET)
    public Msg getTencentSpecialList(HttpServletRequest request, HttpServletResponse response){

        try {
            List<SpecialType> tencentSpecialList = specialService.getTencentSpecialList();
            return new Msg(Msg.SUCCESS_CODE, "成功",tencentSpecialList);
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
    /**
     * 公众号专场展示列表
     */
    @RequestMapping(value = "/getSpecialList", method = RequestMethod.POST)
    public Msg getTencentSpecial( @RequestBody Map<String, Object> params, HttpServletRequest request){

        try {
            List<Special> tencentSpecial = specialService.getTencentSpecial(params);
            return new Msg(Msg.SUCCESS_CODE, "成功",tencentSpecial);
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 公众号专场商品
     */
    @RequestMapping(value = "/getSpecialCommodityList", method = RequestMethod.POST)
    public Msg getSpecialCommodityList( @RequestBody Map<String, Object> params, HttpServletRequest request){

        try {
            List<CommonCommodity> getSpecialShopList = commonCommodityService.getSpecialCommodityList(params);
            return new Msg(Msg.SUCCESS_CODE, "成功",getSpecialShopList);
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


}
