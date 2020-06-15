package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.auction.AuctionGoods;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.H5AuctionGoodsReq;
import com.art.manager.service.auction.AuctionGoodsService;
import com.art.manager.vo.AuctionGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *  拍品商品控制类
 */
@RestController
@RequestMapping("/auction")
@Slf4j
public class AuctionGoodsController {

    @Autowired
    private AuctionGoodsService auctionGoodsService;


    @ResponseBody
    @PostMapping("/insert")
    public Msg insert(@RequestBody AuctionGoods auctionGoods) {
        try{
            boolean result = auctionGoodsService.insert(auctionGoods);
            return new Msg(Msg.SUCCESS_CODE, result ? "新增成功" : "新增失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/deleteByIds")
    public Msg deleteByIds(@RequestBody AuctionGoodsReq req) {
        try {
            boolean result = auctionGoodsService.deleteByIds(req.getIds());
            return new Msg(Msg.SUCCESS_CODE, result ? "删除成功" : "删除失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateById")
    public Msg updateById(@RequestBody AuctionGoods auctionGoods) {
        try {
            boolean result = auctionGoodsService.updateById(auctionGoods);
            return new Msg(Msg.SUCCESS_CODE, result ? "修改成功" : "修改失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectById")
    public Msg selectById(@RequestBody AuctionGoodsReq req) {
        try {
            AuctionGoodsVo auctionGoodsVo = auctionGoodsService.selectById(req, false);
            return new Msg(Msg.SUCCESS_CODE, auctionGoodsVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectH5ById")
    public Msg selectH5ById(@RequestBody AuctionGoodsReq req) {
        try {
            AuctionGoodsVo auctionGoodsVo = auctionGoodsService.selectById(req, true);
            return new Msg(Msg.SUCCESS_CODE, auctionGoodsVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getListByPage")
    public Msg getListByPage(@RequestBody AuctionGoodsReq req) {
        try {
            Map<String, Object> list = auctionGoodsService.getList(req);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getH5Goods")
    public Msg getH5Goods(@RequestBody H5AuctionGoodsReq req) {
        try {
            Map<String, Object> list = auctionGoodsService.getH5Goods(req);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getH5GoodsForToday")
    public Msg getH5GoodsForToday(@RequestBody H5AuctionGoodsReq req) {
        try {
            Map<String, Object> list = auctionGoodsService.getH5GoodsForToday(req);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getH5GoodsCount")
    public Msg getH5GoodsCount(){
        try {
            Map<String, Object> list = auctionGoodsService.getH5GoodsCount();
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/backBaseAmount")
    public Msg backBaseAmount(@RequestBody AuctionBaseAmount auctionBaseAmount){
        try {
            auctionGoodsService.backBaseAmount(auctionBaseAmount.getGoodsId(),auctionBaseAmount.getUsername());
            return new Msg(Msg.SUCCESS_CODE, "ok");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


}
