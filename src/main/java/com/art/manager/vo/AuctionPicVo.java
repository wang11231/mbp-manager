package com.art.manager.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class AuctionPicVo implements Serializable {

    private static final long serialVersionUID = 4089296153379025212L;
    private Long id;

    private Long auctionId;

    private String worksUrl;

}
