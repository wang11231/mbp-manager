package com.art.manager.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 字典值
 */
@Getter
@Setter
@NoArgsConstructor
public class FieldDict {

    private long       id;
    private String     dictType;//字典类型',
    private String     dictCode;//字典码值
    private String     dictName;//字典名称
    private String     dictDesc;//字典描述
    private String     dictRemark;//备份
    private Integer    status;//状态
    private Date       createAt;//创建时间
    private Date       updateAt;//更新时间

}
