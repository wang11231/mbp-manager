package com.art.manager.controller;

import com.art.manager.controller.base.BaseController;
import com.art.manager.pojo.Msg;
import com.art.manager.service.CategoryConfigService;
import com.art.manager.vo.CategoryConfigVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 类别控制类
 */
@Controller
@RequestMapping("/category")
@Slf4j
public class CategoryConfigController extends BaseController {

    @Autowired
    private CategoryConfigService categoryConfigService;

    @ResponseBody
    @PostMapping("/insert")
    public Msg insert(@RequestBody @Valid List<CategoryConfigVo> config, Errors errors) {
        try {
            validErrors(errors);
            categoryConfigService.insert(config);
            return new Msg(Msg.SUCCESS_CODE, "插入成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
    private List get(){
        List<CategoryConfigVo> config = new ArrayList<>();
        CategoryConfigVo vo1 = new CategoryConfigVo();
        vo1.setCode(11L);
        vo1.setName("大类一");
        List<CategoryConfigVo> chids = new ArrayList<>();
        CategoryConfigVo ch = new CategoryConfigVo();
        ch.setName("大类子类一");
        chids.add(ch);
        vo1.setName("大类一");
        vo1.setChild(chids);
        CategoryConfigVo vo2 = new CategoryConfigVo();
        vo2.setName("大类二");
        vo2.setCode(13L);
        CategoryConfigVo vo3 = new CategoryConfigVo();
        vo3.setName("大类三");
        vo3.setCode(14L);
        config.add(vo1);
        config.add(vo2);
        config.add(vo3);
        return  config;
    }

    @ResponseBody
    @PostMapping("/getList")
    public Msg getList() {
        try {
            List<CategoryConfigVo> list = categoryConfigService.getList(null);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public Msg delete(@RequestBody CategoryConfigVo vo) {
        try {
            categoryConfigService.deleteByCode(vo);
            return new Msg(Msg.SUCCESS_CODE, "删除成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getFirstList")
    public Msg getFirstList() {
        try {
            List<CategoryConfigVo> list = categoryConfigService.getFirstList();
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getSecondList")
    public Msg getSecondList(@RequestBody CategoryConfigVo vo) {
        try {
            List<CategoryConfigVo> list = categoryConfigService.getSecondList(vo);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}
