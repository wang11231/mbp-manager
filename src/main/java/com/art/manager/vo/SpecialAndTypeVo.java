package com.art.manager.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author hao.chang
 */
@Data
public class SpecialAndTypeVo {
       private Long id;
       private Long typeId;
       private String name;
       private String operator;
       private String specialName;
       private Date createTime;
       private Date updateTime;

}