package com.art.manager.pojo.wechat;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 收货地址类
 */
@Data
public class ReceiveAddress implements Serializable {

    private Long id;
    private Long userId;
    @NotNull(message = "收货人姓名不能为空")
    private String receiverName; // 收货人姓名
    @NotNull(message = "收货人电话不能为空")
    private String receiverMobile; // 收货人电话
    @NotNull(message = "省不能为空")
    private String province; // 省
    @NotNull(message = "市不能为空")
    private String city; // 市
    @NotNull(message = "县/区不能为空")
    private String county;// 县/区
    private String street;// 街道
    @NotNull(message = "详细地址不能为空")
    private String detailedAddress;
    private String lable; // 标签
    private boolean status; // true：默认收货地址
    private Date createTime;
    private Date updateTime;
    private String mobile;
    private String cnAddress; //中文地址
    private Integer delFlag;


}
