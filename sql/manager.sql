drop table IF EXISTS t_field_dict;
CREATE TABLE `t_field_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_type` varchar(50) NOT NULL COMMENT '字典类型',
  `dict_code` varchar(32) DEFAULT NULL COMMENT '字典码值',
  `dict_name` varchar(100) DEFAULT NULL COMMENT '字典名称',
  `dict_desc` varchar(200) DEFAULT NULL COMMENT '字典描述',
  `dict_remark` varchar(200) DEFAULT NULL COMMENT '字典备注',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `field_dict_type` (`dict_type`,`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典值';

drop table IF EXISTS t_role_menu;
CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `menu_id` int(11) DEFAULT NULL COMMENT '模块id',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `role_id` (`role_id`),
  KEY `menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

drop table IF EXISTS t_sys_login_log;
CREATE TABLE `t_sys_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(32) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table IF EXISTS t_sys_menu;
CREATE TABLE `t_sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `seq` int(2) COMMENT '菜单序号（1：一级:，2：二级）',
  `menu_class` varchar(45) DEFAULT NULL COMMENT '模块的class',
  `menu_name` varchar(45) NOT NULL COMMENT '模块的名字',
  `menu_url` varchar(128) DEFAULT NULL,
  `menu_desc` varchar(32) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `menu_name` (`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='菜单表';

drop table IF EXISTS t_sys_role;
CREATE TABLE `t_sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) DEFAULT NULL,
  `role_desc` varchar(128) DEFAULT NULL COMMENT '角色描述',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色表';

drop table IF EXISTS t_sys_user;
CREATE TABLE `t_sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(45) DEFAULT '' COMMENT '用户名/手机号/邮箱',
  `password` varchar(256) NOT NULL COMMENT '用户密码',
  `telephone` varchar(13) DEFAULT '' COMMENT '用户手机/联系方式',
  `email` varchar(64) DEFAULT '' COMMENT '用户邮箱',
  `desc` varchar(45) DEFAULT NULL COMMENT '姓名',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='这个是用户表';

drop table IF EXISTS t_user_role;
CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '关联的用户id',
  `role_id` int(11) DEFAULT NULL COMMENT '关联的角色id',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

drop table IF EXISTS t_verify_code;
CREATE TABLE `t_verify_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_no` varchar(15) DEFAULT NULL COMMENT '电话号码',
  `verify_code` varchar(10) DEFAULT NULL COMMENT '验证码',
  `expired_time` datetime DEFAULT NULL COMMENT '失效时间',
  `status` int(2) DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `verify_code_phone_no` (`phone_no`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='短信验证码表';

drop table IF EXISTS t_verify_code_ip;
CREATE TABLE `t_verify_code_ip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_no` varchar(15) DEFAULT NULL COMMENT '电话号码',
  `ip` varchar(50) DEFAULT NULL COMMENT '请求ip',
  `count` int(3) DEFAULT '1' COMMENT '同一请求Ip同一天访问次数',
  `day_time` varchar(20) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ip_day_time` (`ip`,`day_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Ip记录表';

drop table IF EXISTS t_verify_code_ip_white_list;
CREATE TABLE `t_verify_code_ip_white_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL COMMENT '请求ip',
  `status` int(3) DEFAULT '1' COMMENT '状态位，默认1：有效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_white_list_ip` (`ip`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证码Ip白名单';

drop table IF EXISTS t_category_config;
CREATE TABLE `t_category_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) COMMENT '名称',
  `parent_id` int(11) COMMENT '父id',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='类别配置表';

drop table IF EXISTS t_artist_info;
CREATE TABLE `t_artist_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `url` varchar(100) COMMENT '图片地址',
  `credential_url` varchar(100) COMMENT '艺术家证件照地址',
  `warrant_url` varchar(100) COMMENT '艺术家授权书地址',
  `remark` varchar(100) COMMENT '备注',
	`user_id` int(11) NOT NULL COMMENT '操作员id',
	`username` varchar(50) NOT NULL COMMENT '操作员',
  `desc` text COMMENT '描述',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `name` (`name`),
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='艺术家信息表';

DROP TABLE IF EXISTS `t_auction_goods`;
CREATE TABLE `t_auction_goods` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `auction_goods_id` varchar(20) DEFAULT NULL COMMENT '拍卖商品ID，用于前段展示',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
	`start_price` decimal(12,2) DEFAULT NULL COMMENT '起拍价',
	`base_amount` decimal(12,2) DEFAULT NULL COMMENT '保证金',
	`increment_step` decimal(12,2) DEFAULT NULL COMMENT '加价幅度',
	`one_price` decimal(12,2) DEFAULT NULL COMMENT '一口价',
	`goods_status` varchar(2) DEFAULT '1' COMMENT '商品状态 1:未开始，2:拍卖中，3:竞拍成功，4:已出售，0:流拍',
	`goods_status_remark` varchar(100) DEFAULT NULL COMMENT '商品状态修改备注',
  `category_id` bigint(11) NOT NULL COMMENT '类别id',
  `artist_id` bigint(11) DEFAULT NULL COMMENT '艺术家id',
  `create_year` varchar(10) DEFAULT NULL COMMENT '创作年代',
  `subject` varchar(50) DEFAULT NULL COMMENT '题材',
	`specification` varchar(50) DEFAULT NULL COMMENT '规格',
  `transport_amount` decimal(12,2) DEFAULT NULL COMMENT '运费',
  `user_id` varchar(20) DEFAULT NULL COMMENT '操作者id',
	`username` varchar(20) DEFAULT NULL COMMENT '操作者',
  `use_scene` varchar(20) DEFAULT NULL COMMENT '使用场景',
  `introduction` varchar(255) DEFAULT NULL COMMENT '描述',
	`show_pic` varchar(100) DEFAULT NULL COMMENT '展示图',
	`start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `setting_status` int(2) NOT NULL DEFAULT 1 COMMENT '代理出价、立即出价最后5分钟延长标志，默认1：没有设置，0：已设置',
	`status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
	`reset_status` int(2) NOT NULL DEFAULT 1 COMMENT '流拍重置状态(0:重置过,1:没有重置)',
	`auto_proxy` int(2) NOT NULL DEFAULT 1 COMMENT '是否执行过自动代理出价(0:已执行,1:未执行)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `goods_name` (`goods_name`),
	KEY `start_price` (`start_price`),
  KEY `base_amount` (`base_amount`),
  KEY `create_time` (`create_time`),
  KEY `goods_status` (`goods_status`),
  KEY `start_date` (`start_date`),
  KEY `end_date` (`end_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='拍卖商品';

DROP TABLE IF EXISTS `t_auction_pic`;
CREATE TABLE `t_auction_pic` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `auction_id` bigint(11) DEFAULT NULL COMMENT '拍卖商品id',
  `works_url` varchar(100) DEFAULT NULL COMMENT '作品路径',
	`status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
	KEY `auction_id` (`auction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='拍卖商品作品图';

DROP TABLE IF EXISTS `t_auction_order`;
CREATE TABLE `t_auction_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `goods_id` bigint(11) NOT NULL COMMENT '商品id',
	`goods_name` varchar(100) NOT NULL COMMENT '商品名',
	`buyer_id` bigint(11) DEFAULT NULL COMMENT '下单人账号id',
	`buyer_name` varchar(50) NOT NULL COMMENT '下单人账号',
	`payer_id` bigint(11) DEFAULT NULL COMMENT '付款人账号id',
	`payer_name` varchar(50) DEFAULT NULL COMMENT '付款人账号',
	`deal_price` decimal(12,2) NOT NULL COMMENT '成交价',
	`buyer_date` datetime NOT NULL COMMENT '下单时间',
	`expire_date` datetime NOT NULL COMMENT '失效时间',
	`pay_date` datetime DEFAULT NULL COMMENT '支付时间',
	`deal_date` datetime DEFAULT NULL COMMENT '处理时间，运营人员修改状态的时间',
	`category_id` bigint(11) NOT NULL COMMENT '类别id',
	`order_status` varchar(2) DEFAULT '1' COMMENT '订单状态 1:待支付，2:待发货，3:待收货，4:已收货, 0 已失效',
  `transport_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  `transport_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `transport_addr` varchar(200) DEFAULT NULL COMMENT '物流地址',
  `receive_addr` varchar(200) DEFAULT NULL COMMENT '收货地址',
  `receive_contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `receive_phone` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作者',
	`status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_no` (`order_no`),
	KEY `goods_name` (`goods_name`),
  KEY `buyer_name` (`buyer_name`),
  KEY `order_status` (`order_status`),
  KEY `expire_date` (`expire_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='拍卖订单';

DROP TABLE IF EXISTS `t_auction_process`;
CREATE TABLE `t_auction_process` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(11)DEFAULT NULL COMMENT '商品id',
	`goods_name` varchar(100) DEFAULT NULL COMMENT '商品名',
	`user_id` bigint(11) DEFAULT NULL COMMENT '用户账号id',
	`username` varchar(50) DEFAULT NULL COMMENT '用户账号',
	`current_price` decimal(12,2) DEFAULT NULL COMMENT '目前价格',
	`add_price_type` varchar(2) DEFAULT '1' COMMENT '加价形式：1、代理出价 2、立即出价 3、一口价 ',
	`process_status` varchar(2) DEFAULT '1' COMMENT '流程状态: 1、出局，2、领先',
	`remark` varchar(100) DEFAULT NULL COMMENT '流程状态修改备注',
	`status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `addrId` bigint(11) DEFAULT NULL COMMENT '收货地址id',
  PRIMARY KEY (`id`),
  KEY `goods_id` (`goods_id`),
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='拍品竞拍流程';

DROP TABLE IF EXISTS `t_auction_base_amount`;
CREATE TABLE `t_auction_base_amount` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
	`user_id` bigint(11) DEFAULT NULL COMMENT '用户账号id',
	`username` varchar(50) DEFAULT NULL COMMENT '用户账号',
  `goods_id` bigint(11)DEFAULT NULL COMMENT '商品id',
	`base_amount` decimal(12,2) DEFAULT NULL COMMENT '保证金',
	`base_amount_status` int(2) NOT NULL DEFAULT 1 COMMENT '冻结状态(0:解冻,1:冻结,2:扣除，订单超时未支付)',
	`pay_type` varchar(10) NOT NULL DEFAULT '' COMMENT '支付方式',
	`status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='拍品保证金信息表';
CREATE INDEX idx_ba_create_time ON t_auction_base_amount (create_time);

DROP TABLE IF EXISTS `t_auction_proxy_limit`;
CREATE TABLE `t_auction_proxy_limit` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户账号id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户账号',
  `goods_id` bigint(11) DEFAULT NULL COMMENT '商品id',
  `price_limit` decimal(12,2) DEFAULT NULL COMMENT '代理出价上限',
  `proxy_status` int(2) NOT NULL DEFAULT '1' COMMENT '代理状态(0:无效,1:有效)',
  `start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `addrId` bigint(11) DEFAULT NULL COMMENT '收货地址id',
  PRIMARY KEY (`id`),
  KEY `username` (`username`),
  KEY `goods_id` (`goods_id`),
  KEY `start_date` (`start_date`),
  KEY `end_date` (`end_date`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='拍品代理出价上限信息表';

DROP TABLE IF EXISTS `t_common_commodity`;
CREATE TABLE `t_common_commodity` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `commdity_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `marke_price` decimal(12,2) DEFAULT NULL COMMENT '市场价',
  `discount_price` decimal(12,2) DEFAULT NULL COMMENT '折扣价',
  `core_specification` varchar(20) DEFAULT NULL COMMENT '画芯规格',
  `type_code` int(11) NOT NULL COMMENT '商品类别',
  `style_code` int(11) NOT NULL COMMENT '商品风格',
  `status` varchar(20) DEFAULT '2' COMMENT '商品状态 0：上线  1：已删除 2:下线',
  `artist_id` bigint(11) DEFAULT NULL,
  `creation_year` varchar(20) DEFAULT NULL COMMENT '创作年代',
  `theme` varchar(20) DEFAULT NULL COMMENT '题材',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `show_picture` varchar(200) DEFAULT NULL,
  `pictures_works` varchar(500) DEFAULT NULL COMMENT '作品图',
  `special_id` bigint(11) DEFAULT '0',
  `freight` decimal(12,2) DEFAULT NULL COMMENT '运费',
  `author` varchar(20) DEFAULT NULL COMMENT '作者',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作者',
  `use_scene` varchar(20) DEFAULT NULL COMMENT '使用场景',
  `stock` int(2) DEFAULT '1' COMMENT '库存',
  `good_id` varchar(20) DEFAULT NULL COMMENT '商品id',
  `introduction_works` varchar(500) DEFAULT NULL COMMENT '作品介绍',
  `backup1` varchar(20) DEFAULT NULL COMMENT '备用字段',
  `backup2` varchar(20) DEFAULT NULL COMMENT '备用字段',
  `backup3` varchar(20) DEFAULT NULL COMMENT '备用字段',
  `is_recommend` char(2) DEFAULT '0' COMMENT '是否是精品推荐: 0：是 1：不是',
  `price` decimal(12,2) DEFAULT NULL COMMENT '价格  折扣价不为空=折扣价 反之=原价',
  PRIMARY KEY (`id`),
  KEY `index_commdity_name` (`commdity_name`) USING BTREE,
  KEY `index_type` (`type_code`),
  KEY `index_style` (`style_code`),
  KEY `index_status` (`status`),
  KEY `create_time` (`create_time`),
  KEY `update_time` (`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title_id` varchar(16) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题ID',
  `news_title` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '新闻标题',
  `news_content` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '新闻内容',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作者',
  `status` varchar(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '0 : 下线 1: 上线， 2：删除'',',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `title_id` (`title_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单号',
  `commodity_name` varchar(50) DEFAULT NULL COMMENT '商品名',
  `deal_price` decimal(12,2) DEFAULT NULL COMMENT '成交价',
  `artist` varchar(20) DEFAULT NULL COMMENT '艺术家',
  `account_number` varchar(50) DEFAULT NULL COMMENT '下单人账号',
  `drawee_number` varchar(50) DEFAULT NULL COMMENT '付款人账号',
  `type_name` varchar(20) DEFAULT NULL,
  `style_name` varchar(20) DEFAULT NULL,
  `logistics_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `logistics_address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `special_field` varchar(64) DEFAULT NULL COMMENT '专场',
  `detailed_address` varchar(255) DEFAULT NULL,
  `receiver_mobile` varchar(32) DEFAULT NULL,
  `receiver_name` varchar(64) DEFAULT NULL,
  `operator` varchar(20) DEFAULT NULL,
  `cn_address` varchar(64) DEFAULT NULL COMMENT '省市区中文地址',
  `del_flag` int(2) DEFAULT '0' COMMENT '0:正常 1：已删除',
  `logistics_company` varchar(64) DEFAULT NULL COMMENT '物流公司',
  `transport_addr` varchar(128) DEFAULT NULL COMMENT '物流地址',
  `status` int(11) DEFAULT '0' COMMENT '状态   0:代付款 1:代发货 2：已发货 3：待收货 4:已收货',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `deal_Time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_no_index` (`order_no`),
  KEY `accountNumber_index` (`account_number`),
  KEY `orderTime_index` (`order_time`),
  KEY `status_index` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

DROP TABLE IF EXISTS `t_receive_address`;
CREATE TABLE `t_receive_address` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL,
  `receiver_name` varchar(20) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_mobile` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `province` varchar(20) DEFAULT NULL COMMENT '省',
  `mobile` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL COMMENT '市',
  `county` varchar(20) DEFAULT NULL COMMENT '县/区',
  `street` varchar(20) DEFAULT NULL COMMENT '街道',
  `detailed_address` varchar(64) DEFAULT NULL COMMENT '详细地址',
  `lable` varchar(20) DEFAULT NULL COMMENT '标签',
  `cn_address` varchar(255) DEFAULT NULL COMMENT '中文地址',
  `del_flag` int(2) DEFAULT '1' COMMENT '0:已删除1：正常',
  `status` bit(1) DEFAULT b'0' COMMENT '0：默认收货地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';


DROP TABLE IF EXISTS `t_recommend`;
CREATE TABLE `t_recommend` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `picture` varchar(255) DEFAULT NULL COMMENT '广告预览图',
  `advertisy_url` varchar(255) DEFAULT NULL COMMENT '广告连接',
  `operator` varchar(20) DEFAULT NULL COMMENT '创建者',
  `commodity_id` varchar(64) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '状态  0：下线  1：上线 2：删除',
  `size` int(11) DEFAULT NULL COMMENT '大图2 小图1',
  `type` int(11) DEFAULT NULL COMMENT '1:关联商品推荐位 2: 普通推荐位',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `create_index` (`create_time`),
  KEY `status_index` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='推荐位广告表';

DROP TABLE IF EXISTS `t_rotary_advertisy`;
CREATE TABLE `t_rotary_advertisy` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `picture` varchar(255) DEFAULT NULL COMMENT '广告预览图',
  `advertisy_url` varchar(255) DEFAULT NULL COMMENT '广告连接',
  `operator` varchar(20) DEFAULT NULL COMMENT '创建者',
  `status` int(11) DEFAULT '0' COMMENT '状态  0：下线  1：上线 2:删除',
  `sort` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `create_index` (`create_time`),
  KEY `status_index` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='轮播广告表';

DROP TABLE IF EXISTS `t_show_picture`;
CREATE TABLE `t_show_picture` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `commodity_id` bigint(11) DEFAULT NULL,
  `pictures_works` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '0:无效,1:有效',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_special`;
CREATE TABLE `t_special` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `type_id` bigint(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `special` varchar(200) DEFAULT NULL,
  `special_subtitle` varchar(50) DEFAULT NULL,
  `special_title` varchar(50) DEFAULT NULL,
  `special_name` varchar(20) DEFAULT NULL,
  `operator` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type_id_index` (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='专场表';

DROP TABLE IF EXISTS `t_special_type`;
CREATE TABLE `t_special_type` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `operator` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`),
  KEY `create_time_ix` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='专场类型表';

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
  `balance` decimal(12,2) DEFAULT '0' COMMENT '余额',
  `status` varchar(2) DEFAULT '0' COMMENT '//0:可用',
  `bond` decimal(12,2) DEFAULT '0' COMMENT '保证金',
  `deduct` decimal(12,2) DEFAULT '0' COMMENT '扣除金额（订单失效未支付）',
  `mobile` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='微信用户信息表';
CREATE INDEX wechat_user_mobile ON t_wechat_user (mobile);
CREATE INDEX wechat_user_openid ON t_wechat_user (openid);

DROP TABLE IF EXISTS `t_payment_result`;
CREATE TABLE `t_payment_result` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `return_code` varchar(16) DEFAULT NULL COMMENT '返回状态码',
  `return_msg` varchar(128) DEFAULT NULL COMMENT '返回信息',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号 ',
  `result_code` varchar(16) DEFAULT NULL COMMENT '业务结果 ',
  `err_code` varchar(32) DEFAULT NULL COMMENT '错误代码 ',
  `err_code_des` varchar(128) DEFAULT NULL,
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '支付时订单号 充值时账户 提现账号',
  `prepay_id` varchar(64) DEFAULT NULL COMMENT '预支付交易会话标识该值有效期为2小时',
  `total_fee` decimal (12,2) DEFAULT NULL COMMENT '支付、充值、提现金额 ',
  `transaction_id` varchar(128) DEFAULT NULL COMMENT '微信支付订单号 ',
  `payment_type` int(5) DEFAULT NULL COMMENT '11:微信支付订单 12：余额支付订单 13：微信支付保证金 14：余额支付保证金 21：充值 22：保证金退回 31：提现',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `refund_status` varchar(128) DEFAULT NULL COMMENT '退款状态 1:退款中  2：退款成功   3：退款异常  4：退款关闭 ',
  `payment_no` varchar(128) DEFAULT NULL COMMENT '微信付款单号 ',
  `out_refund_no` varchar(128) DEFAULT NULL COMMENT '商户退款单号 ',
  `refund_id` varchar(128) DEFAULT NULL COMMENT '微信退款单号 ',
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单号 ',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `out_trade_no` (`out_trade_no`),
  KEY `payment_type` (`payment_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='支付结果记录表';


delete from t_sys_user;
INSERT INTO `t_sys_user`(`id`, `username`, `password`, `telephone`, `email`, `desc`, `status`, `create_time`, `update_time`) VALUES (1, 'admin', '$2a$10$N17sVx1ikhuI6DnLyQnhpe4F7KYGOE52xxzip2I4tNqbQGJ2H9pJa', '', '', NULL, 1, '2019-05-07 09:54:44', '2019-05-07 09:54:44');

delete from  t_sys_role;
INSERT INTO `t_sys_role` VALUES (1, 'admin', '管理员', 1, '2019-5-3 06:53:31', '2019-5-4 00:40:06');
INSERT INTO `t_sys_role` VALUES (2, 'operator', '运营', 1, '2019-5-3 06:53:32', '2019-5-4 00:40:06');
INSERT INTO `t_sys_role` VALUES (3, 'financial', '财务', 1, '2019-5-3 06:56:19', '2019-5-4 00:40:06');

delete from  t_sys_menu;
INSERT INTO `t_sys_menu` VALUES (1, 1,'access-management','权限管理', '/accessManagement', '权限管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (2, 1,'user-management','用户管理', '/user/userManagement', '用户管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (3, 1,'commodity-category','类别管理', '/commodityCategory', '类别管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (4, 1,'specialized-management','专场管理', '/specialized/specializedManagement', '专场管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (5, 1,'artist-management','艺术家管理', '/artist/artistManagement', '艺术家管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (6, 2,'commodity-management','商品管理', '', '商品管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (7, null,'','普通商品', '/commodity/commonCommodity', '普通商品', 6, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (8, null,'','拍卖商品', '/commodity/auctionCommodity', '拍卖商品', 6, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (9, 2,'order-management','订单管理', '', '订单管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (10, null,'','普通订单', '/order/commonOrder', '普通订单', 9, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (11, null,'','拍卖订单', '/order/auctionOrder', '拍卖订单', 9, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (12, 2,'graphic-management','图文管理', '', '图文管理', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (13, null,'','轮播广告', '/videotex/wheelAdvertisement', '轮播广告', 12, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (14, null,'','推荐位广告', '/videotex/recomAdvertisement', '推荐位广告', 12, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (15, null,'','新闻管理', '/videotex/newsManagement', '新闻管理', 12, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');
INSERT INTO `t_sys_menu` VALUES (16, 1,'financial-statements','财务报表', '/financialStatements', '财务报表', NULL, 1, '2019-5-3 06:50:38', '2019-5-4 00:40:05');

delete from  t_user_role;
INSERT INTO `t_user_role`(`id`, `user_id`, `role_id`, `status`, `create_time`, `update_time`) VALUES (1, 1, 1, 1, '2019-05-07 09:54:44', '2019-07-22 09:30:28');
INSERT INTO `t_user_role`(`id`, `user_id`, `role_id`, `status`, `create_time`, `update_time`) VALUES (2, 1, 2, 1, '2019-05-07 09:54:44', '2019-07-22 09:30:29');
INSERT INTO `t_user_role`(`id`, `user_id`, `role_id`, `status`, `create_time`, `update_time`) VALUES (3, 1, 3, 1, '2019-05-07 09:54:44', '2019-07-22 09:30:30');

delete from t_role_menu;
INSERT INTO `t_role_menu` VALUES (1, 1, 1, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (2, 1, 2, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (3, 1, 3, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (4, 1, 4, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (5, 1, 5, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (6, 1, 6, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (7, 1, 7, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (8, 1, 8, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (9, 1, 9, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (10, 1, 10, 1, '2019-5-3 06:59:24', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (11, 1, 11, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (12, 1, 12, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (13, 1, 13, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (14, 1, 14, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (15, 1, 15, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (16, 1, 16, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (17, 2, 2, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (18, 2, 3, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (19, 2, 4, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (20, 2, 5, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (21, 2, 6, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (22, 2, 7, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (23, 2, 8, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (24, 2, 9, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (25, 2, 10, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (26, 2, 11, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (27, 2, 12, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (28, 2, 13, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (29, 2, 14, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (30, 2, 15, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');
INSERT INTO `t_role_menu` VALUES (31, 3, 16, 1, '2019-5-3 06:59:25', '2019-5-4 00:40:05');







