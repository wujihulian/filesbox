-- 2023-04-07
create table `visit_count_record`
(
    id         BIGINT UNSIGNED auto_increment comment '主键id',
    visitCount BIGINT UNSIGNED not null comment '用户访问次数',
    deviceType TINYINT default null comment '设备类型 1 pc , 2 h5, 3 安卓app, 4 ios-app, 5 小程序, 6 电脑app, 7 其他',
    visitDay   DATE    DEFAULT NULL comment '访问的日期',
    modifyTime BIGINT UNSIGNED not null comment '最后修改时间',
    createTime BIGINT UNSIGNED not null comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='访问次数记录表';

-- 2023-04-11 io_source 添加 文件类型 fileType
ALTER TABLE io_source
    ADD COLUMN `type` TINYINT(3) UNSIGNED default null comment '文档类型 1 文档 2 图片 3 音乐 4 视频 5 压缩包 6 其他' AFTER sourceHash;

-- 2023-04-11 根据 fileType 设置文件类型 type
UPDATE io_source
SET `type` = (
    CASE
        WHEN find_in_set(fileType,
                         'txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp')
            THEN 1
        WHEN find_in_set(fileType, 'jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai')
            THEN 2
        WHEN find_in_set(fileType, 'mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape,mpa') THEN 3
        WHEN find_in_set(fileType,
                         'mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx')
            THEN 4
        WHEN find_in_set(fileType, 'zip,gz,rar,iso,tar,7z,ar,bz,bz2,xz,arj') THEN 5
        ELSE 6
        END
    )
WHERE isFolder = 0
  AND `type` IS NULL;

-- 2023-04-11 设置文件夹 type 类型为 NULL
UPDATE io_source
SET `type` = 0
WHERE isFolder = 1;

-- 2023-04-12
ALTER TABLE `visit_count_record`
    ADD COLUMN osName VARCHAR(20) DEFAULT NULL COMMENT '操作系统' AFTER deviceType;
ALTER TABLE `visit_count_record`
    ADD COLUMN `type` TINYINT(3) DEFAULT 1 COMMENT '1 客户端访问 2 用户访问 3 操作系统访问 4 各操作系统总访问' AFTER osName;

-- 2023-04-20
ALTER TABLE `user`
    ADD COLUMN dingOpenId VARCHAR(64) DEFAULT NULL COMMENT '钉钉 openId' AFTER `status`;
ALTER TABLE `user`
    ADD COLUMN wechatOpenId VARCHAR(64) DEFAULT NULL COMMENT '微信 openId' AFTER wechatOpenId;
ALTER TABLE `user`
    ADD COLUMN alipayOpenId VARCHAR(64) DEFAULT NULL COMMENT '支付宝 openId' AFTER alipayOpenId;

-- 2023-05-10
ALTER TABLE `user`
    ADD COLUMN enWechatOpenId VARCHAR(64) DEFAULT NULL COMMENT '企业微信 openId' AFTER wechatOpenId;

-- 2023-05-16
drop table if exists notice;
create table notice
(
    id          BIGINT UNSIGNED auto_increment comment '通知id' primary key,
    title       VARCHAR(128)                                                                 not null comment '标题',
    level       TINYINT UNSIGNED default 0 comment '0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。',
    status      TINYINT UNSIGNED default 0 comment '状态，0暂存，1已发送，2已删除',
    enable      TINYINT UNSIGNED default 0 comment '是否启用，0未启用，1启用',
    send_time   BIGINT UNSIGNED                                                              not null comment '通知发送时间',
    sender_id   BIGINT UNSIGNED                                                              not null comment '通知发送者id',
    sender_ip   VARCHAR(64)                                                                  not null comment '发送通知的IP地址，json(222.22.22.22,杭州)',
    notice_type TINYINT UNSIGNED default 1 comment '消息类型，1通知2消息3私信',
    create_time DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'                NOT NULL,
    modify_time DATETIME         DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' NULL
);
ALTER TABLE notice
    COMMENT = '消息表';

drop table if exists notice_detail_copy2;
create table notice_detail
(
    id          BIGINT UNSIGNED                                                              not null comment '主键id' primary key,
    notice_id   BIGINT UNSIGNED                                                              not null comment '通知id',
    content     TEXT                                                                         not null comment '消息内容',
    is_all      TINYINT UNSIGNED default 1 comment '是否为所有用户 1 是 0 否',
    target_ids  JSON                                                                         not null comment '接收者',
    `status`    TINYINT UNSIGNED default 0 comment '状态，0未读，1已读',
    create_time DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'                NOT NULL,
    modify_time DATETIME         DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' NULL
);
ALTER TABLE notice_detail
    COMMENT = '消息详情表';

CREATE TABLE notice_user
(
    id          BIGINT UNSIGNED auto_increment comment '主键' primary key,
    notice_id   BIGINT UNSIGNED                                                      not null comment '通知id',
    user_id     BIGINT UNSIGNED                                                      not null comment '用户id',
    is_read     tinyint  default 0 comment '是否已读 0 未读 1 已读',
    `year`      year                                                                 not null comment '年份',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'                NOT NULL,
    modify_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' NULL
);
ALTER TABLE notice_detail
    COMMENT = '消息用户表';

-- target_ids
-- {"u": [12312312,52345234,32342345],"g": [2341324,123123,345345],"r": [3252345,346134,132646345,6756354]}

-- 2023-05-17
ALTER TABLE notice_user
    ADD COLUMN last_notice_time DATETIME DEFAULT NULL COMMENT '最后一条通知的时间' AFTER is_read;
ALTER TABLE notice_user
    DROP COLUMN notice_id;
ALTER TABLE notice_user
    ADD COLUMN notice_id JSON DEFAULT NULL COMMENT '通知id集合' AFTER user_id;
ALTER TABLE notice
    ADD COLUMN send_type TINYINT DEFAULT 1 COMMENT '推送方式 1 立即推送 2 计划推送' AFTER `enable`;
ALTER TABLE notice
    DROP COLUMN send_time;
ALTER TABLE notice
    ADD COLUMN send_time DATETIME DEFAULT NULL COMMENT '通知发送时间' AFTER send_type;
ALTER TABLE notice
    ADD COLUMN sort BIGINT DEFAULT 0 COMMENT '排序 越大越靠前' AFTER send_time;
ALTER TABLE notice_detail
    DROP COLUMN `status`;
ALTER TABLE notice_detail
    DROP COLUMN target_ids;
ALTER TABLE notice_detail
    ADD COLUMN target_user_ids JSON DEFAULT NULL COMMENT '目标用户id集合' AFTER is_all;
ALTER TABLE notice_detail
    ADD COLUMN target_dept_ids JSON DEFAULT NULL COMMENT '目标部门id集合' AFTER target_user_ids;
ALTER TABLE notice_detail
    ADD COLUMN target_role_ids JSON DEFAULT NULL COMMENT '目标角色id集合' AFTER target_dept_ids;
ALTER TABLE notice_detail
    ADD COLUMN notice_detail_id BIGINT DEFAULT NULL COMMENT '通知详情表id' AFTER target_role_ids;

-- 2023-05-19
alter table notice
    modify sender_ip VARCHAR(64) null comment '发送通知的IP地址，json(222.22.22.22,杭州)';
alter table notice
    modify create_time DATETIME(19) default CURRENT_TIMESTAMP null comment '创建时间';
alter table notice_detail
    modify create_time DATETIME default CURRENT_TIMESTAMP null comment '创建时间';

-- 2023-05-22
ALTER TABLE notice_user
    ADD COLUMN total BIGINT UNSIGNED DEFAULT 0 COMMENT '总数' AFTER is_read;

-- 2023-05-23
drop table if exists notice;
drop table if exists notice_detail_copy;
drop table if exists notice_detail;

create table notice
(
    id          BIGINT UNSIGNED auto_increment comment '通知id'
        primary key,
    title       VARCHAR(128)    not null comment '标题',
    level       TINYINT UNSIGNED default 0 comment '0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。',
    status      TINYINT UNSIGNED default 0 comment '状态，0暂存，1已发送，2已删除',
    enable      TINYINT UNSIGNED default 0 comment '是否启用，0未启用，1启用',
    send_type   TINYINT          default 1 comment '推送方式 1 立即推送 2 计划推送',
    send_time   DATETIME        null comment '通知发送时间',
    sort        BIGINT           default 0 comment '排序 越大越靠前',
    sender_id   BIGINT UNSIGNED not null comment '通知发送者id',
    sender_ip   VARCHAR(64)      default null comment '发送通知的IP地址，json(222.22.22.22,杭州)',
    notice_type TINYINT UNSIGNED default 1 comment '消息类型，1通知2消息3私信',
    create_time DATETIME        not null comment '创建时间',
    modify_time DATETIME         default null comment '更新时间'
);

create table notice_detail
(
    id               BIGINT UNSIGNED auto_increment comment '主键id'
        primary key,
    notice_id        BIGINT UNSIGNED not null comment '通知id',
    content          TEXT            not null comment '消息内容',
    is_all           TINYINT UNSIGNED default 1 comment '是否为所有用户 1 是 0 否',
    target_user_ids  JSON            null comment '目标用户id集合',
    target_dept_ids  JSON            null comment '目标部门id集合',
    target_role_ids  JSON            null comment '目标角色id集合',
    notice_detail_id BIGINT          null comment '通知详情表id',
    create_time      DATETIME        not null comment '创建时间',
    modify_time      DATETIME         default null comment '更新时间'
);

drop table if exists notice_user;
create table notice_user
(
    id          BIGINT UNSIGNED auto_increment comment '主键'
        primary key,
    user_id     BIGINT UNSIGNED not null comment '用户id',
    notice_id   BIGINT UNSIGNED not null comment '通知id集合',
    is_read     TINYINT  default 0 comment '是否已读 0 未读 1 已读',
    create_time DATETIME        not null comment '创建时间',
    modify_time DATETIME default null comment '更新时间'
);

ALTER TABLE notice
    COMMENT = '通知';
ALTER TABLE notice_detail
    COMMENT = '通知详情';
ALTER TABLE notice_user
    COMMENT = '通知用户关联';

-- 2023-05-25
ALTER TABLE notice_user
    ADD UNIQUE INDEX user_notice_key (user_id, notice_id);
ALTER TABLE notice_detail
    ADD COLUMN dr TINYINT DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除' AFTER modify_time;

-- 2023-05-26
DROP TABLE IF EXISTS share_report;
create table share_report_copy
(
    id          BIGINT UNSIGNED auto_increment comment '自增id'
        primary key,
    share_id    BIGINT UNSIGNED  not null comment '分享id',
    title       VARCHAR(255)     not null comment '分享标题',
    source_id   BIGINT UNSIGNED  not null comment '举报资源id',
    file_id     BIGINT UNSIGNED  not null comment '举报文件id,文件夹则该处为0',
    user_id     BIGINT UNSIGNED  not null comment '举报用户id',
    report_type TINYINT UNSIGNED not null comment '举报类型 (1-侵权,2-色情,3-暴力,4-政治,5-其他)',
    reason      VARCHAR(255)     not null comment '举报原因（其他）描述',
    status      TINYINT UNSIGNED not null comment '处理状态(0-未处理,1-已处理,2-允许分享,3-禁止分享)',
    create_time DATETIME         not null comment '创建时间',
    modify_time DATETIME DEFAULT null comment '最后修改时间'
);
ALTER TABLE share_report_copy
    COMMENT = '分享链接举报';
ALTER TABLE share_report
    ADD INDEX create_time_index (user_id);

-- 2023-05-30
ALTER TABLE share
    ADD COLUMN `status` TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 1 正常 2 取消分享 3 禁止分享' AFTER isLink;
ALTER TABLE io_source
    ADD COLUMN canShare TINYINT UNSIGNED DEFAULT 1 COMMENT '是否可以分享 1 正常 0 禁止分享' AFTER `sort`;
ALTER TABLE share_report
    MODIFY status TINYINT UNSIGNED DEFAULT 0 COMMENT '处理状态(0-未处理,1-已处理)';
-- 2023-05-31
alter table share_report
    modify reason VARCHAR(255) DEFAULT null comment '举报原因（其他）描述';
alter table share
    modify status TINYINT(3) UNSIGNED default 1 null comment '状态 1 正常 3 禁止分享 4 取消分享';

-- 2023-06-08
INSERT INTO system_option (`type`, `key`, `value`, createTime, modifyTime)
values ('Storage', 'LOCAL', '{"name":"local1","size":"1024","location":"/uploads","storageKey":"LOCAL"}', 1682386870,
        1682386870);

-- 2023-06-13
ALTER TABLE common_info
    ADD COLUMN likeCount INT(10) DEFAULT 0 COMMENT '点赞数' AFTER actualViewCount;
ALTER TABLE common_info
    ADD COLUMN isLogin TINYINT DEFAULT 0 COMMENT '是否需要登录 0 否 1 是' AFTER isHide;

create table user_common_info
(
    id         BIGINT UNSIGNED auto_increment
        primary key,
    user_id    BIGINT UNSIGNED not null comment '用户id',
    info_id    BIGINT UNSIGNED not null comment '资讯id',
    view_count INT UNSIGNED default 0 comment '阅读数',
    is_like    TINYINT UNSIGNED default 0 comment '是否点赞 0 否 1 是',
    create_time DATETIME         DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    modify_time DATETIME         DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP comment '最后修改时间',
    constraint userId_infoId_unique
        unique (user_id, info_id)
)ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '用户资讯表';


ALTER TABLE user_common_info DROP INDEX userId_infoId_unique;
ALTER TABLE user_common_info ADD UNIQUE INDEX  infoId_userId_unique(info_id, user_id);
ALTER TABLE user_common_info ADD COLUMN ip VARCHAR(30) DEFAULT NULL COMMENT '用户ip' AFTER info_id;
ALTER TABLE user_common_info ADD UNIQUE INDEX  infoId_ip_unique(info_id, ip);
ALTER TABLE user_common_info MODIFY user_id BIGINT UNSIGNED DEFAULT NULL  comment '用户id';

-- 2023-08-09
create index file_name_index on io_file (fileName(8));
drop index path on io_file;

-- 2023-08-23
create table t_tenant
(
    id                  BIGINT auto_increment primary key,
    tenant_name         VARCHAR(64)          not null comment '名称',
    second_level_domain VARCHAR(64)          not null comment '二级域名',
    user_id             BIGINT           not null comment '超级管理员，用户id',
    status              TINYINT(3) default 0 not null comment '状态，0停用，1启用，2删除',
    start_time          DATETIME         not null comment '生效时间',
    expire_time         DATETIME         not null comment '失效时间',
    size_use            BIGINT default 0 not null comment '已使用大小(byte)',
    group_count         BIGINT default 0 not null comment '组织数',
    user_count          BIGINT default 0 not null comment '用户数',
    remark              VARCHAR(255)         null comment '备注',
    create_time         DATETIME         not null comment '创建时间',
    modify_time         DATETIME         not null comment '最后修改'
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '用户租户表';