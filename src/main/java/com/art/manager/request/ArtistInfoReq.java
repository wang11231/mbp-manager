package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 艺术家信息请求类
 */
@Getter
@Setter
@NoArgsConstructor
public class ArtistInfoReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 3612083769899963687L;

    private String name;

    private List<Integer> ids = new ArrayList<>();

}
