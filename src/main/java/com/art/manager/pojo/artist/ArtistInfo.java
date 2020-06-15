package com.art.manager.pojo.artist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 艺术家信息类
 */
@Getter
@Setter
@NoArgsConstructor
public class ArtistInfo implements Serializable {

    private static final long serialVersionUID = -7769378058729309380L;
    private Long id;

    @NotNull(message = "艺术家姓名不能为空")
    private String name;

    private String remark;

    private String desc;

    private String url;

    private String warrantUrl;
    private String[] warrantUrls;

    private String credentialUrl;
    private String[] credentialUrls;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long userId;

    private String username;

    private String[] urls;
}
