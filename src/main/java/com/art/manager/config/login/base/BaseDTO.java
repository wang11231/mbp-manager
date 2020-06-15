package com.art.manager.config.login.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jumping
 * @version 1.0.0
 * 客户端公共接口请求对象
 */
@Getter
@Setter
@ToString(callSuper = true)
public class BaseDTO {

    /**
     * 系统类型
     */
    private String systemType;

    /**
     * 系统坂本
     */
    private String systemVersion;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 应用版本
     */
    private String appVersion;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 签名
     */
    private String sign;

    /**
     * 终端名称
     */
    private String deviceName;

    /**
     * SIM卡串号
     */
    private String imsi;

    /**
     * 手机串号
     */
    private String imei;
    /**
     * 蓝牙地址
     */
    private String btMac;

    /**
     * WIFI地址
     */
    private String wifiMac;

    /***
     * 经纬度信息
     */
    private String locInfo;

    /**
     * 基带版本
     */
    private String baseVersion;

    /**
     * 内核版本
     */
    private String kernelVersion;

    /**
     * 广告位标识符
     */
    private String aiDentifier;

    /**
     * 唯一标识
     */
    private String Id;

    /**
     * 操作者姓名
     */
    private String operaDesc;

    /**
     * 操作城市(城市码)
     */
    private String operaCity;

    /**
     * 认证中心要求
     */
    private String wifiName;

    /**
     * 0: wifi无线网络; 1: 蜂窝网络
     */
    private String network;

    /**
     * 基站id，GSM下为CellId，CDMA下为BaseStationId
     */
    private String cellId;

    /**
     * 基站lac，GSM下为LAC，CDMA下为NetworkId
     */
    private String lac;

    /**
     * 是否越狱
     */
    private boolean rooted;

    /**
     * 设备ID号
     */
    private String deviceId;

    /**
     * 请求受理渠道
     */
    private String srvChanType;

    /**
     * 设备id
     */
    private String regId;

    /**
     * 设备指纹，风控数字证书
     */
    private String threatMetrix;

    /**
     * 设备其它属性
     */
    private String deviceAttribute;
    /**
     * 线程号
     */
    private String traceLogId;

    /**
     * 手机品牌
     */
    private String brand;
}
