package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.SysUser;
import com.art.manager.pojo.news.News;
import com.art.manager.request.NewsReq;
import com.art.manager.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 首页新闻类
 *
 * @author hao.chang
 */
@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 首页新闻增加
     *
     * @param params
     * @param
     * @return
     */
    @RequestMapping(value = "/insertNews", method = RequestMethod.POST)
    public Msg insertNews(@RequestBody Map<String, Object> params) {
        try {
            int i = newsService.insertNews(params);
            if (i > 0) {
                return new Msg(Msg.SUCCESS_CODE, "添加成功");
            } else {
                return new Msg(Msg.SUCCESS_CODE, "添加失败");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 首页新闻修改
     *
     * @param params
     * @param
     * @return
     */
    @RequestMapping(value = "/updataNews", method = RequestMethod.POST)
    public Msg updataNews(@RequestBody Map<String, Object> params) {
        try {
            int i = newsService.updataNewsNews(params);
            if (i > 0) {
                return new Msg(Msg.SUCCESS_CODE, "修改成功");
            } else {
                return new Msg(Msg.FAILURE_CODE, "修改失败");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 首页新闻列表
     *
     * @param params
     * @param
     * @return
     */
    @RequestMapping(value = "/getNewsList", method = RequestMethod.POST)
    public Msg getNewsList(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> getNewList = newsService.getNewsList(params);
            return new Msg(Msg.SUCCESS_CODE, "成功", getNewList);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 上下线
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Msg updateStatus(@RequestBody NewsReq req) {
        try {
            newsService.updateStatus(req);
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        } catch (Exception e) {
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 新闻首页删除
     */
    @RequestMapping(value = "/delNews", method = RequestMethod.POST)
    public Msg delNews(@RequestBody Map<String, Object> params) {
        try {
            int i = newsService.delNews(params);
            if (i > 0) {
                return new Msg(Msg.SUCCESS_CODE, "删除成功");
            } else {
                return new Msg(Msg.FAILURE_CODE, "删除失败");
            }
        } catch (Exception e) {
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 通过titleId 拿新闻内容
     */
    @RequestMapping(value = "/getNewsContent", method = RequestMethod.POST)
    public Msg getNewsContent(@RequestBody Map<String, Object> params) {
        try {
            News news = newsService.getNewsContent(params);
            return new Msg(Msg.SUCCESS_CODE, "成功", news);
        } catch (Exception e) {
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}