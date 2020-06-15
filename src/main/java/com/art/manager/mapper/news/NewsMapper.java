package com.art.manager.mapper.news;

import com.art.manager.dto.NewsDto;
import com.art.manager.pojo.news.News;
import com.art.manager.pojo.news.NewsExample;
import java.util.List;
import java.util.Map;

import com.art.manager.vo.NewsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NewsMapper {
    int countByExample(NewsExample example);

    int deleteByExample(NewsExample example);

    @Delete({
        "delete from t_news",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into t_news (id, title_id, ",
        "news_title, news_content, ",
        "status, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=INTEGER}, #{titleId,jdbcType=VARCHAR}, ",
        "#{newsTitle,jdbcType=VARCHAR}, #{newsContent,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(News record);

    int insertSelective(News record);

    List<News> selectByExample(NewsExample example);

    @Select({
        "select",
        "id, title_id, news_title, news_content, status, create_time, update_time",
        "from t_news",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    News selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") News record, @Param("example") NewsExample example);

    int updateByExample(@Param("record") News record, @Param("example") NewsExample example);

    int updateByPrimaryKeySelective(News record);

    @Update({
        "update t_news",
        "set title_id = #{titleId,jdbcType=VARCHAR},",
          "news_title = #{newsTitle,jdbcType=VARCHAR},",
          "news_content = #{newsContent,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(News record);

    int updateSelective(News news);

    List<NewsVo> getNewsList( Map<String, Object> params);

    int updateStatusById(@Param("status") Integer status, @Param("id") Long id, @Param("operator") String operator);

    /**
     * 删除新闻
     * @param idsList
     */
    int deleteIds(List idsList);



    /**
     * 通过id拿新闻内容
     * @param params
     */
    News selectContent(Map<String, Object> params);

    @Select(value = "select id, news_title, create_time from t_news where status = '1' order by id desc, create_time desc")
    List<NewsDto> getNewsDto();

    @Select(value = "select id, news_title, create_time, news_content from t_news where id = #{id} order by id desc, create_time desc")
    NewsDto newsDetail(Long id);
}