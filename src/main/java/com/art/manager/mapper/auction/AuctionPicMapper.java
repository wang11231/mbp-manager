package com.art.manager.mapper.auction;

import com.art.manager.pojo.auction.AuctionPic;
import com.art.manager.vo.AuctionPicVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AuctionPicMapper {

    int deleteByAuctionId(@Param("ids") List<Long> ids);

    int insertByAuctionId(List<AuctionPic> list);

    int insertByGoodsId(AuctionPic auctionPic);

    List<AuctionPicVo> selectByAuctionId(AuctionPic auctionPic);

}
