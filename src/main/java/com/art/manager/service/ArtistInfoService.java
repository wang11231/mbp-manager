package com.art.manager.service;

import com.art.manager.pojo.Page;
import com.art.manager.pojo.artist.ArtistInfo;
import com.art.manager.request.ArtistInfoReq;
import com.art.manager.vo.ArtistInfoVo;

import java.util.List;
import java.util.Map;

public interface ArtistInfoService {

    ArtistInfo getArtistInfo(ArtistInfoReq req);

    List<ArtistInfoVo> getList(ArtistInfoReq req);

    Map<String, Object> getListByPage(ArtistInfoReq req, Page page);

    int insert(ArtistInfo artistInfo);

    int updateById(ArtistInfo artistInfo);

    int deleteByIds(ArtistInfoReq req);

    ArtistInfo selectById(ArtistInfoReq req);

    Map<String, Object> getArtistList(ArtistInfoReq req);

    public Map<String, Object> getArtistListNoPage();

}
