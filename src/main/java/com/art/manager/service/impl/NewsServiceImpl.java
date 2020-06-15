package com.art.manager.service.impl;

import com.art.manager.dto.NewsDto;
import com.art.manager.mapper.SysUserMapper;
import com.art.manager.mapper.news.NewsMapper;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.news.News;
import com.art.manager.pojo.news.NewsExample;
import com.art.manager.request.NewsReq;
import com.art.manager.service.NewsService;
import com.art.manager.util.RandomUtil;
import com.art.manager.vo.NewsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hao.chang
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int insertNews(Map<String, Object> params) {
        News news = new News();
        news.setTitleId(RandomUtil.getPwd());
        news.setStatus((String) params.get("status"));
        news.setNewsTitle((String) params.get("newsTitle"));
        news.setNewsContent((String) params.get("newsContent"));
        String username = sysUserMapper.getUsernameById(Long.valueOf(String.valueOf(params.get("userId"))));
        news.setOperator(username);
        int insertNews = newsMapper.insertSelective(news);
        return insertNews;
    }

    @Override
    public int updataNewsNews(Map<String, Object> params) {
        String titleId = (String) params.get("titleId");
        News news = new News();
        news.setTitleId(titleId);
        news.setNewsTitle((String) params.get("newsTitle"));
        news.setNewsContent((String) params.get("newsContent"));
        news.setStatus((String) params.get("status"));
        news.setUpdateTime(new Date());
        String username = sysUserMapper.getUsernameById(Long.valueOf(String.valueOf(params.get("userId"))));
        news.setOperator(username);
        int updataNewsNews = newsMapper.updateSelective(news);
        return updataNewsNews;
    }

    @Override
    public Map<String, Object> getNewsList(Map<String, Object> params) {
        int pageNum = Integer.parseInt(params.get("pageNum").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage(pageNum, pageSize);
        /*NewsExample newsExample = new NewsExample();
        newsExample.createCriteria().andStatusEqualTo("0");
        List<News> news = newsMapper.selectByExample(newsExample);*/
        List<NewsVo> newsList = newsMapper.getNewsList(params);
        PageInfo<NewsVo> pageInfo = new PageInfo<>(newsList);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", newsList);
        return result;
    }

    @Override
    public int updateStatus(NewsReq req) {
        String username = sysUserMapper.getUsernameById(req.getUserId());
        return newsMapper.updateStatusById(req.getStatus(), req.getId(), username);
    }

    @Override
    public int delNews(Map<String, Object> params) {
        List idsList = (List) params.get("titleIds");
        int delNews = newsMapper.deleteIds(idsList);
        return delNews;
    }

    @Override
    public News getNewsContent(Map<String, Object> params) {
        News news = newsMapper.selectContent(params);
        return news;
    }

    @Override
    public List<NewsDto> getNewsList() {
        return newsMapper.getNewsDto();
    }

    @Override
    public NewsDto newsDetail(NewsReq req) {
        return newsMapper.newsDetail(req.getId());
    }

}