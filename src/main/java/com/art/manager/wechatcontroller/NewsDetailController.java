package com.art.manager.wechatcontroller;

import com.art.manager.pojo.Msg;
import com.art.manager.request.NewsReq;
import com.art.manager.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/newsInfo")
@Slf4j
public class NewsDetailController {

    @Autowired
    private NewsService newsService;

    /**
     * 公众号新闻详情
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/newsDetail", method = RequestMethod.POST)
    public Msg newsDetail(@RequestBody NewsReq req, HttpServletRequest request){

        try {
            return new Msg(Msg.SUCCESS_CODE, newsService.newsDetail(req));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }

    }

}
