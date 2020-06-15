package com.art.manager.mapper.artist;

import com.art.manager.pojo.artist.ArtistInfo;
import com.art.manager.request.ArtistInfoReq;
import com.art.manager.vo.ArtistInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ArtistInfoMapper {

    List<ArtistInfo> getList(ArtistInfoReq req);

    int insert(ArtistInfo artistInfo);

    int updateById(ArtistInfo artistInfo);

    int deleteByIds(@Param("list") List<Integer> list);

    ArtistInfo selectById(ArtistInfoReq req);

    List<Long> getArtistName(@Param("name") String name);

    String getArtistNameById(Long id);

    List<ArtistInfoVo> getArtistList(ArtistInfoReq req);
}
