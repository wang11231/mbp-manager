package com.art.manager.service.impl;

import com.art.manager.dto.CategoryConfigDto;
import com.art.manager.dto.CommonCommodityDto;
import com.art.manager.mapper.config.CategoryConfigMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.config.CategoryConfig;
import com.art.manager.service.CategoryConfigService;
import com.art.manager.vo.CategoryConfigVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class CategoryConfigServiceImpl implements CategoryConfigService {
    @Autowired
    private CategoryConfigMapper categoryConfigMapper;

    @Override
    @Transactional
    public boolean insert(List<CategoryConfigVo> list) {
        if(list == null || list.size() == 0){
            return false;
        }
        for(CategoryConfigVo config : list){
            if(config.getCode() == null){//插入
                insertConfig(config);
            }else{//更新
                updateConfig(config);
            }
        }
        return true;
    }

    @Override
    public List<CategoryConfigVo> getList(CategoryConfig record) {
        return getCategories(record);
    }

    private List<CategoryConfigVo> getCategories(CategoryConfig record){
        if(record == null){
            record = new CategoryConfig();
        }
        List<CategoryConfig> list = categoryConfigMapper.getList(record);
        List<CategoryConfigVo> result = new ArrayList<>();
        if(list == null || list.size() == 0){
            return result;
        }
        for(CategoryConfig config : list){
            CategoryConfigVo vo = new CategoryConfigVo();
            vo.setCode(config.getId());
            vo.setName(config.getName());
            if(config.getParentId() == null){
                result.add(vo);
            }else{
                Iterator<CategoryConfigVo> iterator = result.iterator();
                while(iterator.hasNext()){
                    CategoryConfigVo next = iterator.next();
                    if(next.getCode().equals(config.getParentId())){
                        next.getChild().add(vo);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean deleteByCode(CategoryConfigVo vo) {
        if(vo.getCode() == null){
            throw new RuntimeException("类别code为空");
        }
        CategoryConfig config = new CategoryConfig();
        config.setId(vo.getCode());
        categoryConfigMapper.deleteById(config);
        return false;
    }

    @Override
    public List<CategoryConfigVo> getFirstList() {
        CategoryConfig record = new CategoryConfig();
        record.setFlag(1);//只查询大类
        return getCategories(record);
    }

    @Override
    public List<CategoryConfigVo> getSecondList(CategoryConfigVo vo) {
        List<CategoryConfigVo> result = new ArrayList<>();
        if(vo == null){
            return result;
        }
        if(vo.getCode() == null){
            throw new RuntimeException("类别code为空");
        }
        CategoryConfig record = new CategoryConfig();
        record.setParentId(vo.getCode());
        List<CategoryConfig> list = categoryConfigMapper.getList(record);
        //转换
        if(list == null || list.size() == 0){
            return result;
        }
        for(CategoryConfig config : list) {
            CategoryConfigVo categoryConfigVo = new CategoryConfigVo();
            categoryConfigVo.setCode(config.getId());
            categoryConfigVo.setName(config.getName());
            result.add(categoryConfigVo);
        }
        return result;
    }

    @Override
    public CommonCommodityDto getTypeByCode(Long typeCode, Long styleCode) {
        String typaName = categoryConfigMapper.getTypaName(typeCode);
        String styleName = categoryConfigMapper.getStyleName(styleCode);
        CommonCommodityDto commonCommodityDto = new CommonCommodityDto();
        commonCommodityDto.setTypeName(typaName);
        commonCommodityDto.setStyleName(styleName);
        return commonCommodityDto;
    }

    @Override
    public List<CategoryConfigDto> getStyleList() {
        return categoryConfigMapper.getStyleList();

    }

    @Override
    public List<CategoryConfig> selectById(String name) {
        return categoryConfigMapper.selectById(name);
    }

    private void insertConfig(CategoryConfigVo config) {
        //插入父配置
        Long id = insertConfig(config.getName(), null);
        //插入子配置
        List<CategoryConfigVo> list = config.getChild();
        if(list != null && list.size() > 0){
            for(CategoryConfigVo vo:list){
                insertConfig(vo.getName(), id);
            }
        }
    }

    private Long insertConfig(String name, Long parentId) {
        CategoryConfig categoryConfig = new CategoryConfig();
        categoryConfig.setName(name);
        categoryConfig.setParentId(parentId);
        categoryConfigMapper.insert(categoryConfig);
        log.debug("CategoryConfig id:{}", categoryConfig.getId());
        return categoryConfig.getId();
    }

    private void updateConfig(CategoryConfigVo config) {
        //插入父配置
        updateConfig(config.getName(), config.getCode());
        //插入子配置
        List<CategoryConfigVo> list = config.getChild();
        if(list != null && list.size() > 0){
            for(CategoryConfigVo vo:list){
                if(vo.getCode() != null){
                    updateConfig(vo.getName(), vo.getCode());
                }else{
                    insertConfig(vo.getName(), config.getCode());
                }
            }
        }
    }

    private void updateConfig(String name, Long id) {
        CategoryConfig categoryConfig = new CategoryConfig();
        categoryConfig.setName(name);
        categoryConfig.setId(id);
        categoryConfigMapper.updateNameById(categoryConfig);
    }

}
