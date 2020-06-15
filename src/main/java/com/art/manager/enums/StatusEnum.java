package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举类
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    VALID(1,"有效"),
    INVALID(0,"无效");
    private Integer code;
    private String desc;

}
