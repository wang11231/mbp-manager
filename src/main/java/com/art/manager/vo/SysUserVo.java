package com.art.manager.vo;

import com.art.manager.pojo.SysMenu;
import com.art.manager.pojo.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SysUserVo implements Serializable {
    private static final long serialVersionUID = 4132399353006742587L;
    private Long id;

    private String username;

    private Integer status;

    private String token;

    private Date createTime;

    private Date updateTime;

    private List<UserRole> userRoles = new ArrayList<>();

    private List<SysMenu> userMenu = new ArrayList<>();

}
