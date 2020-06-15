package com.art.manager.pojo.picture;

import lombok.Data;

@Data
public class ShowPicture {

    private Long id;
    private Long commodityId;
    private String picturesWorks; // 作品图(多张)
}
