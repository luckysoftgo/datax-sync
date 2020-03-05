
DROP DATABASE if exists data_catagroy;
CREATE DATABASE data_catagroy DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

use data_catagroy;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dc_catagroy
-- ----------------------------
DROP TABLE IF EXISTS dc_catagroy;
CREATE TABLE dc_catagroy (
    catagroy_id int(11) NOT NULL AUTO_INCREMENT COMMENT '目录id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    catagroy_name varchar(50) DEFAULT '' COMMENT '目录名称',
    catagroy_order int(11) NOT NULL DEFAULT 0 COMMENT '排序号',
    catagroy_desc varchar(200) DEFAULT '' COMMENT '目录描述',
    catagroy_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',
    catagroy_type varchar(50) DEFAULT '' COMMENT '目录类型:"collection（采集）dw（仓库）subject（主题）interface（接口）',
    catagroy_tag varchar(20) DEFAULT '' COMMENT '目录标签:以逗号分隔形式存储',
    catagroy_parent_id int(11) NOT NULL DEFAULT 0 COMMENT '父目录ID,根目录-1',

    PRIMARY KEY (catagroy_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '数据目录表' ;


-- ----------------------------
-- Table structure for dc_business_metadata
-- ----------------------------
DROP TABLE IF EXISTS dc_business_metadata;
CREATE TABLE dc_business_metadata  (
    meta_id int(11) NOT NULL AUTO_INCREMENT COMMENT '业务元数据id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    catagroy_id int(11) NOT NULL NULL DEFAULT 0 COMMENT '目录id',
    bus_meta_key varchar(50) DEFAULT '' COMMENT '业务元数据项',
    bus_meta_value  varchar(200) DEFAULT '' COMMENT '业务元数据值',
    bus_meta_type varchar(50) DEFAULT '' COMMENT '业务字段类型',
    bus_meta_desc varchar(50) DEFAULT '' COMMENT '业务元数据描述',
    bus_meta_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',
    bus_meta_tag varchar(20) DEFAULT '' COMMENT '标签:以逗号分隔形式存储',

    PRIMARY KEY (meta_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8  COMMENT = '业务元数据表' ;


-- ----------------------------
-- Table structure for dc_meta_db_schema
-- ----------------------------
DROP TABLE IF EXISTS dc_meta_db_schema;
CREATE TABLE dc_meta_db_schema  (
    schema_id int(11) NOT NULL AUTO_INCREMENT COMMENT '库模式元数据库id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    schema_name varchar(50) DEFAULT '' COMMENT '库名称',
    schema_type  varchar(10) DEFAULT '' COMMENT '数据库类型:mysql,dameng,oracle,kingbase',
    schema_driver  varchar(100) DEFAULT '' COMMENT '连接的驱动',
    schema_url  varchar(200) DEFAULT '' COMMENT '连接的驱动',
    schema_user  varchar(20) DEFAULT '' COMMENT '连接的驱动',
    schema_pass  varchar(40) DEFAULT '' COMMENT '连接的驱动',
    info_desc text COMMENT '描述信息',

    PRIMARY KEY (schema_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8  COMMENT = '库模式元数据库管理' ;


-- ----------------------------
-- Table structure for dc_meta_db_table
-- ----------------------------
DROP TABLE IF EXISTS dc_meta_db_table;
CREATE TABLE dc_meta_db_table  (
    table_id int(11) NOT NULL AUTO_INCREMENT COMMENT '库模式元数据表id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    catagroy_id int(11) NOT NULL NULL DEFAULT 0 COMMENT '目录id',
    meta_id int(11) NOT NULL DEFAULT 0 COMMENT '元数据id',
    schema_id int(11) NOT NULL DEFAULT 0 COMMENT '库模式元数据库id',

    table_name varchar(50) DEFAULT '' COMMENT '表名称',
    table_title  varchar(100) DEFAULT '' COMMENT '业务名称',
    table_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',
    table_type varchar(50) DEFAULT '' COMMENT '表类型:"normal(普通表) entity（实体表）relationship（关系表）dictionary（字典表）默认是普通表',
    table_desc varchar(50) DEFAULT '' COMMENT '表描述',
    table_tag varchar(20) DEFAULT '' COMMENT '标签:以逗号分隔形式存储',

    PRIMARY KEY (table_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '库模式元数据表管理' ;



-- ----------------------------
-- Table structure for dc_meta_db_column
-- ----------------------------
DROP TABLE IF EXISTS dc_meta_db_column;
CREATE TABLE dc_meta_db_column  (
    column_id int(11) NOT NULL AUTO_INCREMENT COMMENT '库模式元数据表列id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    table_id int(11) NOT NULL DEFAULT 0 COMMENT '库模式元数据表id',
    column_name varchar(50) DEFAULT '' COMMENT '字段名称',
    column_code varchar(50) DEFAULT '' COMMENT '字段代码',
    column_foreignkey tinyint(1) DEFAULT 0 COMMENT '字段是否外键:0否,1是',
    column_uniquekey tinyint(1) DEFAULT 0 COMMENT '字段是否唯一标识:0否,1是',
    column_primerykey tinyint(1) DEFAULT 0 COMMENT '字段是否唯主键:0否,1是',
    column_default varchar(100) DEFAULT '' COMMENT '字段默认值',
    column_order int(11) DEFAULT 0 COMMENT '字段排序',
    column_desc varchar(200) DEFAULT '' COMMENT '字段描述',
    column_type varchar(50) DEFAULT '' COMMENT '字段数据类型',
    column_length int(11) DEFAULT 0 COMMENT '字段長度',
    column_decimal int(11) DEFAULT 0 COMMENT '字段精度',
    column_index tinyint(1) DEFAULT 0 COMMENT '字段是否索引:0否,1是',

    PRIMARY KEY (column_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8  COMMENT = '库模式元数据列管理' ;


-- ----------------------------
-- Table structure for dc_meta_db_column_rule
-- ----------------------------
DROP TABLE IF EXISTS dc_meta_db_column_rule;
CREATE TABLE dc_meta_db_column_rule  (
    id int(11) NOT NULL AUTO_INCREMENT COMMENT '字段規則id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    column_id int(11) NOT NULL DEFAULT 0 COMMENT '库模式元数据表列id',
    rule_id int(11) NOT NULL DEFAULT 0 COMMENT '规则id',

    PRIMARY KEY (id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '库模式元数据列规则管理' ;


-- ----------------------------
-- Table structure for dc_manager_metadata
-- ----------------------------
DROP TABLE IF EXISTS dc_manager_metadata;
CREATE TABLE dc_manager_metadata  (
    manager_id int(11) NOT NULL AUTO_INCREMENT COMMENT '管控元数据id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    catagroy_id int(11) NOT NULL NULL DEFAULT 0 COMMENT '目录id',
    man_meta_key varchar(50) DEFAULT '' COMMENT '管控元数据项',
    man_meta_value  varchar(200) DEFAULT '' COMMENT '管控元数据值',
    man_meta_type varchar(50) DEFAULT '' COMMENT '管控字段类型',
    man_meta_desc varchar(50) DEFAULT '' COMMENT '管控元数据描述',
    man_meta_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',
    man_meta_tag varchar(200) DEFAULT '' COMMENT '标签:以逗号分隔形式存储',

    PRIMARY KEY (manager_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '管控元数据表' ;


-- ----------------------------
-- Table structure for dc_manager_meta_rule
-- ----------------------------
DROP TABLE IF EXISTS dc_manager_meta_rule;
CREATE TABLE dc_manager_meta_rule  (
    rule_id int(11) NOT NULL AUTO_INCREMENT COMMENT '管控规则id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    rule_type varchar(50) DEFAULT '' COMMENT '规则类型',
    rule_code varchar(50) DEFAULT '' COMMENT '规则編碼',
    rule_desc varchar(200) DEFAULT '' COMMENT '规则描述',
    rule_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',

    PRIMARY KEY (rule_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '管控元数据规则' ;


-- ----------------------------
-- Table structure for dc_manager_meta_report_rule
-- ----------------------------
DROP TABLE IF EXISTS dc_manager_meta_report_rule;
CREATE TABLE dc_manager_meta_report_rule  (
    rule_id int(11) NOT NULL AUTO_INCREMENT COMMENT '管控规则id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    rule_name varchar(50) DEFAULT '' COMMENT '报告规则类型',
    rule_sql varchar(1000) DEFAULT '' COMMENT '报告规则sql:选中发布后的列元数据后生成的sql语句',
    rule_code varchar(100) DEFAULT '' COMMENT '报告规则key',
    rule_desc varchar(200) DEFAULT '' COMMENT '报告规则描述',

    rule_interval varchar(20) DEFAULT '' COMMENT '规则间隔:"default 1d 1day（每天）1hours（每小时）1week（每周）"',
    rule_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',
    manager_id int(11) NOT NULL DEFAULT 0 COMMENT '管控元数据id',
    catagroy_id int(11) NOT NULL DEFAULT 0 COMMENT '目录的id',

    PRIMARY KEY (rule_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '目录管理元数据报告规则表' ;



-- ----------------------------
-- Table structure for dc_dictionary
-- ----------------------------
DROP TABLE IF EXISTS dc_dictionary;
CREATE TABLE dc_dictionary  (
    dictionary_id int(11) NOT NULL AUTO_INCREMENT COMMENT '字典id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    dictionary_key varchar(50) DEFAULT '' COMMENT '字典名称',
    dictionary_value varchar(50) DEFAULT '' COMMENT '字典编码',
    dictionary_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',
    group_id int(11) DEFAULT 0 COMMENT '字典分组id',
    group_key varchar(500) DEFAULT '' COMMENT '字典分组key',

    PRIMARY KEY (dictionary_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '目录字典' ;


-- ----------------------------
-- Table structure for dc_dictionary_group
-- ----------------------------
DROP TABLE IF EXISTS dc_dictionary_group;
CREATE TABLE dc_dictionary_group  (
    group_id int(11) NOT NULL AUTO_INCREMENT COMMENT '字典组id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    group_key varchar(50) DEFAULT '' COMMENT '字典分组key',
    group_name varchar(50) DEFAULT '' COMMENT '字典分组名称',
    group_desc varchar(500) DEFAULT '' COMMENT '字典分组描述',
    group_status varchar(50) DEFAULT '' COMMENT '目录状态:"created（暂存）removed（删除）published（发布）',

    PRIMARY KEY (group_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '目录字典组' ;


-- ----------------------------
-- Table structure for dc_report
-- ----------------------------
DROP TABLE IF EXISTS dc_report;
CREATE TABLE dc_report  (
    report_id int(11) NOT NULL AUTO_INCREMENT COMMENT '报告id',
    disabled tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志,1删除,0正常使用',
    uuid varchar(20) NOT NULL DEFAULT '' COMMENT '系统生成的一个随机码',

    create_user varchar(20) DEFAULT '' COMMENT '创建者',
    create_time datetime NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_user varchar(20) DEFAULT '' COMMENT '更新者',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    report_key varchar(50) DEFAULT '' COMMENT '报告key',
    report_name varchar(50) DEFAULT '' COMMENT '报告名称',
    report_content varchar(500) DEFAULT '' COMMENT '报告内容:json',
    report_data_type varchar(50) DEFAULT '' COMMENT '报告数据类型:text,json,xml',
    report_type varchar(50) DEFAULT '' COMMENT '"data（数据报告）interface（接口报告）quality（质量报告）',
    report_time datetime NOT NULL DEFAULT NOW() COMMENT '报告时间',
    catagroy_id int(11) NOT NULL DEFAULT 0 COMMENT '目录的id',

    PRIMARY KEY (report_id)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8 COMMENT = '目录报告' ;
