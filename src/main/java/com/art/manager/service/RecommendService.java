package com.art.manager.service;

import com.art.manager.pojo.Recommend;
import com.art.manager.request.RecommendReq;

import java.util.List;
import java.util.Map;

public interface RecommendService {

    Map<String, Object> getRecommendList(Map<String, Object> params);

    int updateStatus(RecommendReq req);

    int insertRecommend(Recommend recommend);

    Map<String, Object> getRecommend();

    int delete(List<Long> ids);

    int update(Recommend recommend);
}
