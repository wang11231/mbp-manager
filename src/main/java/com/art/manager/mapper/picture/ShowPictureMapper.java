package com.art.manager.mapper.picture;

import com.art.manager.pojo.picture.ShowPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ShowPictureMapper {

    int savePicturUrl(List<ShowPicture> list);

    List<String> getPictureUrl(Long commodityId);

    int updateById(Long commodityId);
}
