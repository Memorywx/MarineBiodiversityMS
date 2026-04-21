CREATE DATABASE IF NOT EXISTS marine_biodiversity
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE marine_biodiversity;
SET NAMES utf8mb4;

CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(64) NOT NULL COMMENT '登录用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '加密密码',
  `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `role` TINYINT NOT NULL DEFAULT 3 COMMENT '角色：0-ADMIN,1-RESEARCHER,2-STUDENT,3-PUBLIC',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核,1-正常,2-禁用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `species` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chinese_name` VARCHAR(128) NOT NULL COMMENT '中文名',
  `scientific_name` VARCHAR(256) NOT NULL COMMENT '学名',
  `phylum` VARCHAR(64) DEFAULT NULL COMMENT '门',
  `class_name` VARCHAR(64) DEFAULT NULL COMMENT '纲',
  `order_name` VARCHAR(64) DEFAULT NULL COMMENT '目',
  `family` VARCHAR(64) DEFAULT NULL COMMENT '科',
  `genus` VARCHAR(64) DEFAULT NULL COMMENT '属',
  `species` VARCHAR(64) DEFAULT NULL COMMENT '种',
  `morphological_features` TEXT COMMENT '形态特征',
  `living_habits` TEXT COMMENT '生活习性',
  `distribution` TEXT COMMENT '分布区域描述',
  `distribution_lat` DECIMAL(10,8) DEFAULT NULL COMMENT '分布中心纬度',
  `distribution_lng` DECIMAL(11,8) DEFAULT NULL COMMENT '分布中心经度',
  `protection_level` VARCHAR(32) DEFAULT NULL COMMENT '保护等级',
  `iucn_status` VARCHAR(32) DEFAULT NULL COMMENT 'IUCN濒危状态',
  `video_url` VARCHAR(512) DEFAULT NULL COMMENT '视频链接',
  `references` TEXT COMMENT '参考文献',
  `is_public` TINYINT NOT NULL DEFAULT 1 COMMENT '是否公开：0-否,1-是',
  `create_by` BIGINT NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scientific_name` (`scientific_name`),
  KEY `idx_chinese_name` (`chinese_name`),
  KEY `idx_phylum` (`phylum`),
  KEY `idx_class` (`class_name`),
  KEY `idx_order` (`order_name`),
  KEY `idx_family` (`family`),
  KEY `idx_protection_level` (`protection_level`),
  KEY `idx_iucn_status` (`iucn_status`),
  KEY `idx_is_public` (`is_public`),
  KEY `idx_create_by` (`create_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物种信息表';

CREATE TABLE `species_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `species_id` BIGINT NOT NULL COMMENT '关联物种ID',
  `image_url` VARCHAR(512) NOT NULL COMMENT '图片URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_species_id` (`species_id`),
  CONSTRAINT `fk_image_species` FOREIGN KEY (`species_id`) REFERENCES `species` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物种图片表';

CREATE TABLE `ecosystem` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL COMMENT '名称',
  `type` VARCHAR(64) DEFAULT NULL COMMENT '类型编码',
  `description` TEXT COMMENT '描述',
  `geo_range` TEXT COMMENT '地理范围',
  `environment_features` TEXT COMMENT '环境特征',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生态系统表';

CREATE TABLE `observation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `observation_time` DATETIME NOT NULL COMMENT '观测时间',
  `location_name` VARCHAR(256) DEFAULT NULL COMMENT '地点名称',
  `latitude` DECIMAL(10,8) NOT NULL COMMENT '纬度',
  `longitude` DECIMAL(11,8) NOT NULL COMMENT '经度',
  `ecosystem_id` BIGINT DEFAULT NULL COMMENT '关联生态系统',
  `observer` VARCHAR(128) DEFAULT NULL COMMENT '观测人员',
  `observer_user_id` BIGINT DEFAULT NULL COMMENT '观测用户ID',
  `water_temperature` DECIMAL(5,2) DEFAULT NULL COMMENT '水温',
  `salinity` DECIMAL(5,2) DEFAULT NULL COMMENT '盐度',
  `ph_value` DECIMAL(4,2) DEFAULT NULL COMMENT 'pH值',
  `depth` DECIMAL(8,2) DEFAULT NULL COMMENT '深度(m)',
  `remarks` TEXT COMMENT '备注',
  `create_by` BIGINT NOT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_ecosystem_id` (`ecosystem_id`),
  KEY `idx_observation_time` (`observation_time`),
  KEY `idx_create_by` (`create_by`),
  KEY `idx_lat_lng` (`latitude`,`longitude`),
  CONSTRAINT `fk_obs_ecosystem` FOREIGN KEY (`ecosystem_id`) REFERENCES `ecosystem` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='观测记录表';

CREATE TABLE `observation_species` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `observation_id` BIGINT NOT NULL COMMENT '观测记录ID',
  `species_id` BIGINT NOT NULL COMMENT '物种ID',
  `estimated_quantity` INT DEFAULT NULL COMMENT '估算数量',
  `behavior` VARCHAR(256) DEFAULT NULL COMMENT '行为描述',
  `remarks` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_observation_species` (`observation_id`,`species_id`),
  KEY `idx_species_id` (`species_id`),
  CONSTRAINT `fk_os_observation` FOREIGN KEY (`observation_id`) REFERENCES `observation` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_os_species` FOREIGN KEY (`species_id`) REFERENCES `species` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='观测-物种关联表';

CREATE TABLE `operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `username` VARCHAR(64) DEFAULT NULL COMMENT '用户名',
  `operation` VARCHAR(128) NOT NULL COMMENT '操作描述',
  `method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
  `request_url` VARCHAR(256) DEFAULT NULL COMMENT '请求URL',
  `request_params` TEXT COMMENT '请求参数',
  `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-失败,1-成功',
  `error_msg` TEXT COMMENT '错误信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
