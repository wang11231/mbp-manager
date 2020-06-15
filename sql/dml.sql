/*
Navicat MySQL Data Transfer

Source Server         : exportDH
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : saas

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-08-28 09:54:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `t_wechat_user`;
CREATE TABLE `t_wechat_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) DEFAULT NULL COMMENT '用户的唯一标识',
  `nickname` varchar(20) DEFAULT NULL COMMENT '用户昵称',
  `sex` int(2) DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `language` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL COMMENT '普通用户个人资料填写的城市',
  `province` varchar(20) DEFAULT NULL COMMENT '用户个人资料填写的省份',
  `country` varchar(20) DEFAULT NULL COMMENT '国家，如中国为CN',
  `headimgurl` varchar(500) DEFAULT NULL COMMENT '用户头像',
  `privileges` varchar(255) DEFAULT NULL COMMENT '用户特权信息',
  `unionid` varchar(50) DEFAULT NULL,
  `balance` decimal(11,0) DEFAULT '0' COMMENT '余额',
  `status` varchar(2) DEFAULT '0' COMMENT '//0:可用',
  `bond` decimal(11,0) DEFAULT '0' COMMENT '保证金',
  `mobile` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='微信用户信息表';

-- ----------------------------
-- Records of t_wechat_user
-- ----------------------------
INSERT INTO `t_wechat_user` VALUES ('18', 'oPcvM5iT3-SJgdMfjPewJmRmjr2E', 'DreamTime', '1', 'zh_CN', '浦东新区', '上海', '中国', 'http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJYDTX98iaDyL8jCfIrFH4SJkM8K5JMCzpzuvyxRGWNLrIOZktVVGKPicYUhCQf77qONHsbCo3TOtDA/132', null, null, '2000', '1', '200', '15800858400', '2019-08-12 21:44:28', '2019-08-12 21:44:59', '2019-08-21 14:38:59');

/*
Navicat MySQL Data Transfer

Source Server         : exportDH
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : saas

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-08-28 10:04:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单号',
  `commodity_name` varchar(50) DEFAULT NULL COMMENT '商品名',
  `deal_price` decimal(10,0) DEFAULT NULL COMMENT '成交价',
  `artist` varchar(20) DEFAULT NULL COMMENT '艺术家',
  `account_number` varchar(50) DEFAULT NULL COMMENT '下单人账号',
  `drawee_number` varchar(50) DEFAULT NULL COMMENT '付款人账号',
  `type_name` varchar(20) DEFAULT NULL,
  `style_name` varchar(20) DEFAULT NULL,
  `logistics_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `logistics_address` varchar(255) DEFAULT NULL COMMENT '物流地址',
  `special_field` varchar(64) DEFAULT NULL COMMENT '专场',
  `operator` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态   0:代付款 1:代发货 2：已发货 3：待收货',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `deal_Time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_no_index` (`order_no`),
  KEY `accountNumber_index` (`account_number`),
  KEY `orderTime_index` (`order_time`),
  KEY `status_index` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('1', '12345yu', 'dfgsadas', '22', 'czxc', '15800858400', '15800858400', 'xcz', 'zxczxc', '444', '333', '111', null, '0', '2019-07-16 11:43:47', '2019-07-16 11:43:50', '2019-07-16 11:43:53', '2019-07-16 11:43:56');
INSERT INTO `t_order` VALUES ('2', '123456789', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '8000', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '上海浦东新区', ' 名人书法专场 一线名家精品', null, '3', '2019-08-09 16:01:57', '2019-08-09 16:02:01', '2019-08-09 16:02:03', '2019-08-09 16:02:07');
INSERT INTO `t_order` VALUES ('3', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', null, '1', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
INSERT INTO `t_order` VALUES ('4', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', '', '2', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
INSERT INTO `t_order` VALUES ('5', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', '', '2', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
INSERT INTO `t_order` VALUES ('6', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', '', '1', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
INSERT INTO `t_order` VALUES ('7', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', '', '1', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
INSERT INTO `t_order` VALUES ('8', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', '', '3', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
INSERT INTO `t_order` VALUES ('9', 'PT234567', '中国书法家协会会员、中国书协培训中心教授 王友谊 书法福 书法', '22', '卢禹舜', '15800858400', '15800858400', '书法', '行书', '121', '1212', ' 名人书法专场 一线名家精品', '', '0', '2019-08-21 13:36:39', '2019-08-21 13:36:43', '2019-08-21 13:36:46', '2019-08-21 13:36:53');
