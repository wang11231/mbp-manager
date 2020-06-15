package com.art.manager.mapper;

import com.art.manager.dto.RecommendDto;
import com.art.manager.pojo.Recommend;
import com.art.manager.vo.RecommendCommodityVo;
import com.art.manager.vo.RecommendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface RecommendMapper {

    List<RecommendVo> getList(Map<String, Object> params);

    int updateStatusById(@Param("status") Integer status, @Param("id") Long id, @Param("operator") String operator);

    int insert(Recommend recommend);

    List<RecommendDto> getRecommend();

    int delete(@Param("ids") List<Long> ids);

    int update(Recommend recommend);

    List<RecommendCommodityVo> getRecommendCommodityList(Map<String, Object> params);
}
