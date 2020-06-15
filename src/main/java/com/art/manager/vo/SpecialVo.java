package com.art.manager.vo;

import com.art.manager.pojo.Special;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SpecialVo implements Serializable {

    private Long id;
    private String name; // 专场类型名
    private String title;
    private String operator; // 操作者
    private Date createTime;
    private Date updateTime;

    private List<Special> specialList = new ArrayList<>();
}
