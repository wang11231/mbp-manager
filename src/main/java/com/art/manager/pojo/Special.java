package com.art.manager.pojo;

import com.art.manager.vo.CommonCommodityVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Special {

    private Long id;
    private Long typeId; // 专场类型id
    private String specialName; // 专场名
    private String specialTitle; // 专场标题
    private String specialSubtitle; // 专场副标题
    private String special; // 专场封面
    private String operator; // 操作者
    private String name;//专场类型名
    private Long userId;
    private Integer status; // 默认 0， 0:可用  1：删除
    private Date createTime;
    private Date updateTime;
    private String[] specials;
    private List<CommonCommodityVo> commonCommodities = new ArrayList<>();
}
