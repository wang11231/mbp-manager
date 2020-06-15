package com.art.manager.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类配置Vo类
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryConfigVo implements Serializable {

    private static final long serialVersionUID = -5423525176972340155L;
    private Long code;

    @NotNull(message = "类别名称为空")
    private String name;

    private List<CategoryConfigVo> child = new ArrayList<>();

}
