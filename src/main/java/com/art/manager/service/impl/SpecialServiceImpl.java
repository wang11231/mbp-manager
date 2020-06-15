package com.art.manager.service.impl;

import com.art.manager.dto.SpecialDto;
import com.art.manager.mapper.CommonCommodityMapper;
import com.art.manager.mapper.SpecialMapper;
import com.art.manager.mapper.SysUserMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.Special;
import com.art.manager.pojo.SpecialType;
import com.art.manager.request.SpecialReq;
import com.art.manager.service.SpecialService;
import com.art.manager.util.StringUtils;
import com.art.manager.vo.CommonCommodityVo;
import com.art.manager.vo.SpecialAndTypeVo;
import com.art.manager.vo.SpecialVo;
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
public class SpecialServiceImpl implements SpecialService {

    @Autowired
    private SpecialMapper specialMapper;
    @Autowired
    private CommonCommodityMapper commodityMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Msg getList(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        PageHelper.startPage(pageNum, pageSize);
        List<SpecialVo> list = specialMapper.getList(params);
        /*if(list != null && list.size() > 0){
            list.forEach(item ->{
                List<Special> specialList = specialMapper.getSpecialList(item.getId());
                if(specialList != null && specialList.size() > 0){
                    specialList.forEach(special -> {
                        List<CommonCommodityVo> commodityVoList = commodityMapper.getListBySpecialId(special.getId());
                        special.setCommonCommodities(commodityVoList);
                    });
                }
                item.setSpecialList(specialList);
            });
        }*/
        PageInfo<SpecialVo> pageInfo = new PageInfo<>(list);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", list);
        return new Msg(Msg.SUCCESS_CODE, result);
    }

    @Override
    public Msg getSpecialList(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        if(StringUtils.isEmpty(String.valueOf(params.get("id")))){
            return new Msg(Msg.FAILURE_CODE, "专场类型id为空");
        }
        Long id = Long.valueOf(String.valueOf(params.get("id")));
        PageHelper.startPage(pageNum, pageSize);
        List<SpecialAndTypeVo> specialList = specialMapper.getSpecialList(id);
        PageInfo<SpecialAndTypeVo> pageInfo = new PageInfo<>(specialList);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        if(specialList != null && specialList.size() > 0){
            result.put("name",specialList.get(0).getName());
        } else {
            result.put("name","");
        }
        result.put("pages", pages);
        result.put("list", specialList);
        return new Msg(Msg.SUCCESS_CODE, result);
    }

    @Transactional
    @Override
    public int insertSpecialType(SpecialType specialType) {
        specialType.setCreateTime(new Date());
        specialType.setUpdateTime(new Date());
        String username = sysUserMapper.getUsernameById(specialType.getUserId());
        specialType.setOperator(username);
        return specialMapper.insertSpecialType(specialType);
    }

    @Transactional
    @Override
    public int insertSpecial(Special special) {
        special.setCreateTime(new Date());
        special.setUpdateTime(new Date());
        String username = sysUserMapper.getUsernameById(special.getUserId());
        special.setOperator(username);
        transUrls(special);
        return specialMapper.insertSpecial(special);
    }

    @Transactional
    @Override
    public int removeCommondity(Long commodityId) {
        return specialMapper.remove(commodityId);
    }

    @Transactional
    @Override
    public int updateSpecialType(SpecialType specialType) {
        specialType.setUpdateTime(new Date());
        String username = sysUserMapper.getUsernameById(specialType.getUserId());
        specialType.setOperator(username);
        return specialMapper.updateSpecialType(specialType);
    }

    @Transactional
    @Override
    public int updateSpecial(Special special) {
        special.setUpdateTime(new Date());
        String username = sysUserMapper.getUsernameById(special.getUserId());
        special.setOperator(username);
        transUrls(special);
        return specialMapper.updateSpecial(special);
    }

    @Override
    public int deleteSpecialType(SpecialReq req) {
        List<Long> idList = specialMapper.selectSpecialById(req.getIds());
        if(idList == null || idList.size() == 0){
            return specialMapper.deleteSpecialTypeByIds(req.getIds());
        }
        specialMapper.deleteSpecialTypeByIds(req.getIds());
        specialMapper.deleteSpecialByIds(idList);
        List<CommonCommodityVo> commodityList = commodityMapper.getListBySpecialId(idList);
        if(commodityList != null || commodityList.size() > 0){
            commodityMapper.delectCommodity(idList);
        }
        return 0;
    }

    @Override
    public int deleteSpecial(List<Long> list) {
        return specialMapper.deleteSpecialByIds(list);
    }

    @Override
    public List<SpecialDto> selectSpecial() {
        return specialMapper.selectSpecial();
    }

    @Override
    public Special getSpecialById(SpecialReq req) {
        Special special = specialMapper.getSpecialById(req.getId());
        String[] specials= {special.getSpecial()};
        special.setSpecials(specials);
        return special;
    }

    /**
     * 公众号专场展示列表
     * @param params
     */
    @Override
    public List<Special> getTencentSpecial(Map<String, Object> params) {
        //Special 的 typeId
        Integer id = (Integer) params.get("id");
        List<Special> list =  specialMapper.specialCommonList(id);
        return list;

    }

    /**
     * 公众号专场列表
     * @param
     */
    @Override
    public List<SpecialType> getTencentSpecialList() {
        return specialMapper.getTencentSpecialList();
    }

    /**
     * 转换urls->url
     * @param special
     */
    private void transUrls(Special special){
        if(special == null){
            return;
        }
        String[] urls = special.getSpecials();
        if(urls == null || urls.length == 0){
            return;
        }
        special.setSpecial(urls[0]);
    }



}
