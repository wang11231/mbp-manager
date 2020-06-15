package com.art.manager.service.impl;

import com.art.manager.mapper.artist.ArtistInfoMapper;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.artist.ArtistInfo;
import com.art.manager.request.ArtistInfoReq;
import com.art.manager.service.ArtistInfoService;
import com.art.manager.util.ChineseCharacterUtil;
import com.art.manager.vo.ArtistInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistInfoServiceImpl implements ArtistInfoService {

    @Autowired
    private ArtistInfoMapper artistInfoMapper;

    @Override
    public ArtistInfo getArtistInfo(ArtistInfoReq req) {
        if(req == null || req.getId() == null){
            req = new ArtistInfoReq();
        }
        List<ArtistInfo> list = artistInfoMapper.getList(req);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    @Override
    public List<ArtistInfoVo> getList(ArtistInfoReq req) {
        if(req == null){
            req = new ArtistInfoReq();
        }
        List<ArtistInfo> list = artistInfoMapper.getList(req);
        List<ArtistInfoVo> result = new ArrayList<>();
        if(list == null || list.size() == 0){
            return result;
        }
        for(ArtistInfo artistInfo : list){
            ArtistInfoVo vo = new ArtistInfoVo();
            vo.setId(artistInfo.getId());
            vo.setName(artistInfo.getName());
            result.add(vo);
        }
        return result;
    }

    @Override
    public Map<String, Object> getListByPage(ArtistInfoReq req, Page page) {
        if(req == null){
            req = new ArtistInfoReq();
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<ArtistInfo> list = artistInfoMapper.getList(req);
        PageInfo<ArtistInfo> pageInfo = new PageInfo<>(list);
        int pages = Page.getPages(pageInfo.getTotal(), page.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", list);
        return result;
    }

    @Override
    public int insert(ArtistInfo artistInfo) {
        transUrls(artistInfo);
        return artistInfoMapper.insert(artistInfo);
    }

    @Override
    public int updateById(ArtistInfo artistInfo) {
        if(artistInfo == null || artistInfo.getId() == null){
            throw new RuntimeException("id信息为空");
        }
        if(StringUtils.isBlank(artistInfo.getName()) && artistInfo.getUrls() != null && artistInfo.getUrls().length > 0
                && StringUtils.isBlank(artistInfo.getRemark()) && StringUtils.isBlank(artistInfo.getDesc())){
            throw new RuntimeException("修改的信息为空");
        }
        transUrls(artistInfo);
        return artistInfoMapper.updateById(artistInfo);
    }

    @Override
    public int deleteByIds(ArtistInfoReq req) {
        if(req == null || req.getIds() == null || req.getIds().size() == 0){
            throw new RuntimeException("删除id信息为空");
        }
        return artistInfoMapper.deleteByIds(req.getIds());
    }

    @Override
    public ArtistInfo selectById(ArtistInfoReq req) {
        if(req == null || req.getId() == null){
            throw new RuntimeException("id信息为空");
        }
        ArtistInfo artistInfo = artistInfoMapper.selectById(req);
        artistInfo.setUrls(new String[]{artistInfo.getUrl()});
        artistInfo.setWarrantUrls(new String[]{artistInfo.getWarrantUrl()});
        artistInfo.setCredentialUrls(new String[]{artistInfo.getCredentialUrl()});
        return artistInfo;
    }

    @Override
    public Map<String, Object> getArtistList(ArtistInfoReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<ArtistInfoVo> artistList = artistInfoMapper.getArtistList(req);
        if(artistList != null && artistList.size() >0){
            for(ArtistInfoVo artistInfoVo : artistList){
                artistInfoVo.setUrls(new String[]{artistInfoVo.getUrl()});
            }
        }
        PageInfo<ArtistInfoVo> pageInfo = new PageInfo<>(artistList);
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", artistList);
        return result;
    }

    @Override
    public Map<String, Object> getArtistListNoPage() {
        List<ArtistInfoVo> artistList = artistInfoMapper.getArtistList(null);
        Map<String, List<ArtistInfoVo>> map = new TreeMap();
        if(artistList != null && artistList.size() >0) {
            for (ArtistInfoVo artistInfoVo : artistList) {
                artistInfoVo.setUrls(new String[]{artistInfoVo.getUrl()});
            }
            artistList = artistList.stream().filter(art -> {
                        art.setFirstLetter(ChineseCharacterUtil.convertHanzi2PinyinOneLetter(art.getName()));
                        return true;
                    }
            ).collect(Collectors.toList());
            Map<String, List<ArtistInfoVo>> innerMap = artistList.stream().collect(Collectors.groupingBy(ArtistInfoVo::getFirstLetter));
            map = sortByKey(innerMap);
        }
        Map<String, Object> result = new HashMap();
        result.put("map", map);
        return result;
    }

    public <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 转换urls->url
     * @param artistInfo
     */
    private void transUrls(ArtistInfo artistInfo){
        if(artistInfo == null){
            return;
        }
        String[] urls = artistInfo.getUrls();
        if(urls == null || urls.length == 0){
            return;
        }
        artistInfo.setUrl(urls[0]);
        String[] warrantUrls = artistInfo.getWarrantUrls();
        if(warrantUrls == null || warrantUrls.length == 0){
            return;
        }
        artistInfo.setWarrantUrl(warrantUrls[0]);

        String[] credentialUrls = artistInfo.getCredentialUrls();
        if(credentialUrls == null || credentialUrls.length == 0){
            return;
        }
        artistInfo.setCredentialUrl(credentialUrls[0]);
    }

}
