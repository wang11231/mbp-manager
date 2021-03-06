package com.art.manager.pojo;

import com.art.manager.vo.SysRoleVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SysUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 916913492916869934L;
    private Long id;

    @NotNull(message="用户名不能为空")
    private String username;

    @NotNull(message="密码不能为空")
    private transient String password;

    private String telephone;

    private String email;

    private String desc;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long[] roles;

    private List<SysRoleVo> userRoles = new ArrayList<>();

    private List<SysMenu> userMenu = new ArrayList<>();

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}