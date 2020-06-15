package com.art.manager.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 艺术家信息Vo类
 */
@Getter
@Setter
@NoArgsConstructor
public class ArtistInfoVo implements Serializable {

    private static final long serialVersionUID = 4130405200458776427L;
    private Long id;

    private String name;
    private String url;
    private String[] urls;
    private String firstLetter;

}
