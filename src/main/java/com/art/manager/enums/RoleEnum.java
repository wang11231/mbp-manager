package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举类
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("admin","管理员"),
    OPERATOR("operator","操作员"),
    FINANCE("finance","财务");
    private String code;
    private String desc;
}
