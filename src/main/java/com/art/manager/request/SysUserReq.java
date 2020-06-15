package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户请求类
 */
@Getter
@Setter
@NoArgsConstructor
public class SysUserReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 4191672408942162487L;

    private String username;

    private Long[] ids;

}


