package com.art.manager.config.login.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author jumping
 * @version 1.0.0
 * @time 2015/6/29
 */
@Getter
@Setter
@ToString(exclude = {"certNo", "loginToken"})
public class LoginResDTO {

    /**
     * 最后登陆时间
     */
    private String lastLoginTime;

    /**
     * 实名状态
     */
    private String realNameStatus;

    /**
     * 用户用户会话
     */
    private String sessionKey;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 证件号
     */
    private String certNo;

    /**
     * 证件类型
     */
    private String certType;

    /**
     * 归属地
     */
    private String location;

    /**
     * 运营商类型
     */
    private String operatorType;

    /**
     * 密码创建类型
     */
    private String pwdCreateType;

    /**
     * 登陆令牌
     */
    private String loginToken;

    /**
     * 是否冻结
     * true 已冻结
     * false 未冻结
     */
    private boolean isFrozen;

    /**
     * 头像文件URL
     */
    private String headFileUrl;

    /**
     * 账户标记（判断是否的默开用户）
     */
    private String accountStatus;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机登陆号
     */
    private String productNo;

    /**
     * 邮箱
     */
    private String loginEmail;
    /**
     * 邮箱状态
     */
    private String loginEmailStatus;

    /**
     * 2: 集团统一账户授权需要绑定授权；
     * 1: 需要高级认证登录；
     * 0：登录完成
     */
    private int loginFlag;

    /**
     * 当前时间
     */
    private String currentDate;

    /**
     * 是否默开
     */
    private boolean desOpen;

    /**
     * 用户星级
     */
    private String grade;
    /**
     * 用户是否今天生日
     */
    private boolean birthToday;

    /**
     *账户冻结状态：01-NORMAL-正常  02-FREEZE-冻结  03-CLOSE-销户  04-LOCK-锁定  05-LOST-挂失  06-NOENABLE-未激活
     */
    private String custStatus;

    /**
     * 关联账户详情
     * add by dinglei
     * 2016-11-14
     */
    //private List<OperatorGradeInfoResDto> relatedAccountList;

    /**
     * 用户升星标识
     * “1”可升级到三星，“0”不满足升星条件
     * add by dinglei 2016-11-19
     */
    private int raiseFlag;

    //需要转动转盘
    private boolean slideVerifyFlag;

    //余额是否冻结
    private Boolean isBalanceFrozen;

    private String operatorNo;

    /**
     * 账户客户号
     */
    private String contractNo;

    //高级认证方式
    private List<String> validMethodList;
    //==============收银台H5二次免登所需要的信息=====================
    private String tempToken;

    //红包冻结用户标识
    private boolean redPacketFroze=Boolean.FALSE;
}
