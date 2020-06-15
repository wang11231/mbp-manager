package com.art.manager.service;

import com.art.manager.dto.NewsDto;
import com.art.manager.pojo.news.News;
import com.art.manager.request.NewsReq;

import java.util.List;
import java.util.Map;

/**
 * @author hao.chang
 */
public interface NewsService {
    /**
     *  添加新闻标题和内容
     * @param params
     */
    int insertNews(Map<String, Object> params);

    /**
     *  修改新闻标题和内容
     * @param params
     */
    int updataNewsNews(Map<String, Object> params);

    /**
     * 新闻列表
     * @param params
     * @return
     */
    Map<String, Object> getNewsList(Map<String, Object> params);

    int updateStatus(NewsReq req);

    /**
     * 删除新闻
     * @param params
     */
    int delNews(Map<String, Object> params);


    /**
     * 通过titleId拿新闻内容
     * @param params
     */
    News getNewsContent(Map<String, Object> params);

    /**
     * 新闻列表  公众号
     * @return
     */
    List<NewsDto> getNewsList();

    NewsDto newsDetail(NewsReq req);
}
