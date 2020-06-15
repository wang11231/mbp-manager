package com.art.manager.service.impl;

import com.art.manager.dto.RotaryAdvertisyDto;
import com.art.manager.mapper.RotaryAdvertisyMapper;
import com.art.manager.mapper.SysUserMapper;
import com.art.manager.pojo.*;
import com.art.manager.service.RotaryAdvertisyService;
import com.art.manager.vo.RotaryAdvertisyVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RotaryAdvertisyServiceImpl implements RotaryAdvertisyService {

    @Autowired
    private RotaryAdvertisyMapper rotaryAdvertisyMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public Map<String, Object> getRotaryAdvertisy(Map<String, Object> params) {
        List<RotaryAdvertisyVo> list = rotaryAdvertisyMapper.getList(params);
        Long id = rotaryAdvertisyMapper.selectId();
        if(list != null && list.size() > 0){
            for(RotaryAdvertisyVo rotaryAdvertisy : list){
                String[] pictureUrl = {rotaryAdvertisy.getPicture()};
                rotaryAdvertisy.setPictureUrl(pictureUrl);
                if(rotaryAdvertisy.getId().equals(id)){
                    rotaryAdvertisy.setLast(true);
                }
            }
        }
        Map<String, Object> result = new HashMap();
        result.put("list", list);
        return result;
    }

    @Transactional
    @Override
    public Msg moveUp(Long id) {
        RotaryAdvertisy rotaryAdvertisy = rotaryAdvertisyMapper.getSortById(id);
        RotaryAdvertisy rotaryAdvertisy1 = rotaryAdvertisyMapper.getUpData(rotaryAdvertisy.getSort());
        if(rotaryAdvertisy1 == null){
            return new Msg(Msg.FAILURE_CODE, "已是第一条数据");
        }
        Integer temp = rotaryAdvertisy.getSort();
        rotaryAdvertisy.setSort(rotaryAdvertisy1.getSort());
        rotaryAdvertisy1.setSort(temp);
        String username = sysUserMapper.getUsernameById(rotaryAdvertisy.getUserId());
        rotaryAdvertisy.setOperator(username);
        rotaryAdvertisy1.setOperator(username);
        rotaryAdvertisyMapper.updateByPrimaryKeySelective(rotaryAdvertisy);
        rotaryAdvertisyMapper.updateByPrimaryKeySelective(rotaryAdvertisy1);
        return new Msg(Msg.SUCCESS_CODE, "更新成功");
    }

    @Transactional
    @Override
    public Msg moveDown(Long id) {
        RotaryAdvertisy rotaryAdvertisy = rotaryAdvertisyMapper.getSortById(id);
        RotaryAdvertisy nextData = rotaryAdvertisyMapper.getNextData(rotaryAdvertisy.getSort());
        if(nextData == null){
            return new Msg(Msg.FAILURE_CODE, "已是最后一条数据");
        }
        String username = sysUserMapper.getUsernameById(rotaryAdvertisy.getUserId());
        rotaryAdvertisy.setOperator(username);
        nextData.setOperator(username);
        Integer temp = rotaryAdvertisy.getSort();
        rotaryAdvertisy.setSort(nextData.getSort());
        nextData.setSort(temp);
        rotaryAdvertisyMapper.updateByPrimaryKeySelective(rotaryAdvertisy);
        rotaryAdvertisyMapper.updateByPrimaryKeySelective(nextData);
        return new Msg(Msg.SUCCESS_CODE, "更新成功");
    }

    @Transactional
    @Override
    public int updateStatus(Integer status, Long id) {
        int maxSort = rotaryAdvertisyMapper.selectSort(status);
        return rotaryAdvertisyMapper.updateStatusById(status, id, maxSort+1);
    }

    @Transactional
    @Override
    public int insert(RotaryAdvertisy rotaryAdvertisy) {
        int maxSort = rotaryAdvertisyMapper.selectSort(0);
        if(maxSort == 0){
            maxSort = 19999;
        }
        String username = sysUserMapper.getUsernameById(rotaryAdvertisy.getUserId());
        transUrls(rotaryAdvertisy);
        rotaryAdvertisy.setOperator(username);
        rotaryAdvertisy.setStatus(0);
        rotaryAdvertisy.setSort(maxSort+1);
        rotaryAdvertisy.setCreateTime(new Date());
        rotaryAdvertisy.setUpdateTime(new Date());
        return rotaryAdvertisyMapper.insert(rotaryAdvertisy);
    }

    @Override
    public int delete(List<Long> ids) {
        return rotaryAdvertisyMapper.delete(ids);
    }

    @Override
    public int update(RotaryAdvertisy rotaryAdvertisy) {
        RotaryAdvertisy rotary = rotaryAdvertisyMapper.getSortById(rotaryAdvertisy.getId());
        String username = sysUserMapper.getUsernameById(rotaryAdvertisy.getUserId());
        transUrls(rotaryAdvertisy);
        rotaryAdvertisy.setOperator(username);
        rotaryAdvertisy.setUpdateTime(new Date());
        rotaryAdvertisy.setSort(rotary.getSort());
        return rotaryAdvertisyMapper.update(rotaryAdvertisy);
    }

    @Override
    public List<RotaryAdvertisyDto> queryAll() {
        return rotaryAdvertisyMapper.queryAll();
    }

    /**
     * 转换urls->url
     * @param rotaryAdvertisy
     */
    private void transUrls(RotaryAdvertisy rotaryAdvertisy){
        if(rotaryAdvertisy == null){
            return;
        }
        String[] urls = rotaryAdvertisy.getPictureUrl();
        if(urls == null || urls.length == 0){
            return;
        }
        rotaryAdvertisy.setPicture(urls[0]);
    }
}
