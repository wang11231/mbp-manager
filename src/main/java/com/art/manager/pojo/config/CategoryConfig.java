package com.art.manager.pojo.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 分类配置类
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryConfig implements Serializable {

    private static final long serialVersionUID = 6674805111829848953L;
    private Long id;

    private String name;

    private Long parentId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    //1：只查询大类 null：全量查询
    private Integer flag;

}
