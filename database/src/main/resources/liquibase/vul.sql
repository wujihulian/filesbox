--liquibase formatted sql


--changeset slj:1
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `commentID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `pid` bigint(20) unsigned NOT NULL COMMENT '该评论上级ID',
  `userID` bigint(20) unsigned NOT NULL COMMENT '评论用户id',
  `targetType` smallint(5) unsigned NOT NULL COMMENT '评论对象类型1分享2文件3文章4......',
  `targetID` bigint(20) unsigned NOT NULL COMMENT '评论对象id',
  `content` text NOT NULL COMMENT '评论内容',
  `praiseCount` int(11) unsigned NOT NULL COMMENT '点赞统计',
  `commentCount` int(11) unsigned NOT NULL COMMENT '评论统计',
  `status` tinyint(3) unsigned NOT NULL COMMENT '状态 1正常 2异常 3其他',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`commentID`),
  KEY `pid` (`pid`),
  KEY `userID` (`userID`),
  KEY `targetType` (`targetType`),
  KEY `targetID` (`targetID`),
  KEY `praiseCount` (`praiseCount`),
  KEY `commentCount` (`commentCount`),
  KEY `modifyTime` (`modifyTime`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='通用评论表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `comment_meta`;
CREATE TABLE `comment_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `commentID` bigint(20) unsigned NOT NULL COMMENT '评论id',
  `key` varchar(255) NOT NULL COMMENT '字段key',
  `value` text NOT NULL COMMENT '字段值',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改',
  PRIMARY KEY (`id`),
  UNIQUE KEY `commentID_key` (`commentID`,`key`),
  KEY `commentID` (`commentID`),
  KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='评论表扩展字段' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `comment_praise`;
CREATE TABLE `comment_praise` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `commentID` bigint(20) unsigned NOT NULL COMMENT '评论ID',
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `commentID_userID` (`commentID`,`userID`),
  KEY `commentID` (`commentID`),
  KEY `userID` (`userID`),
  KEY `modifyTime` (`modifyTime`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='评论点赞表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `groupID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '群组id',
  `name` varchar(255) NOT NULL COMMENT '群组名',
  `parentID` bigint(20) unsigned NOT NULL COMMENT '父群组id',
  `parentLevel` varchar(1000) NOT NULL COMMENT '父路径id; 例如:  ,2,5,10,',
  `extraField` varchar(100) DEFAULT NULL COMMENT '扩展字段',
  `sort` int(11) unsigned NOT NULL COMMENT '排序',
  `sizeMax` double unsigned NOT NULL COMMENT '群组存储空间大小(GB) 0-不限制',
  `sizeUse` bigint(20) unsigned NOT NULL COMMENT '已使用大小(byte)',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `status` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '启用状态 0-未启用 1-启用 2-删除',
  PRIMARY KEY (`groupID`),
  KEY `name` (`name`),
  KEY `parentID` (`parentID`),
  KEY `createTime` (`createTime`),
  KEY `modifyTime` (`modifyTime`),
  KEY `order` (`sort`),
  KEY `parentLevel` (`parentLevel`(333))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='群组表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `group_meta`;
CREATE TABLE `group_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `groupID` bigint(20) unsigned NOT NULL COMMENT '部门id',
  `key` varchar(255) NOT NULL COMMENT '存储key',
  `value` text NOT NULL COMMENT '对应值',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupID_key` (`groupID`,`key`),
  KEY `groupID` (`groupID`),
  KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='用户数据扩展表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_file`;
CREATE TABLE `io_file` (
  `fileID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) NOT NULL COMMENT '文件名',
  `size` bigint(20) unsigned NOT NULL COMMENT '文件大小',
  `ioType` bigint(20) unsigned NOT NULL COMMENT 'io的id',
  `path` varchar(255) NOT NULL COMMENT '文件路径',
  `hashSimple` varchar(100) NOT NULL COMMENT '文件简易hash(不全覆盖)；hashSimple',
  `hashMd5` varchar(100) NOT NULL COMMENT '文件hash, md5',
  `linkCount` int(11) unsigned NOT NULL COMMENT '引用次数;0则定期删除',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `is_preview` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否支持预览,0否，1是',
  `app_preview` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否支持APP上的文档预览，主要指doc转成pdf后的html5预览',
  `is_h264_preview` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '视频转h264是否成功, 0未成功,1成功,2失败',
  `is_m3u8` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '源文件为视频类型时,表示是否已经切片处理成m3u8格式,0否1是;文档类型(doc,ppt等)时,表示是否转成flash,0否1是',
  PRIMARY KEY (`fileID`),
  KEY `size` (`size`),
  KEY `path` (`path`),
  KEY `hash` (`hashSimple`),
  KEY `linkCount` (`linkCount`),
  KEY `createTime` (`createTime`),
  KEY `ioType` (`ioType`),
  KEY `hashMd5` (`hashMd5`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档存储表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_file_contents`;
CREATE TABLE `io_file_contents` (
  `fileID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `content` mediumtext NOT NULL COMMENT '文本文件内容,最大16M',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`fileID`),
  KEY `createTime` (`createTime`),
  FULLTEXT KEY `content` (`content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文件id' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_file_meta`;
CREATE TABLE `io_file_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `fileID` bigint(20) unsigned NOT NULL COMMENT '文件id',
  `key` varchar(255) NOT NULL COMMENT '存储key',
  `value` text NOT NULL COMMENT '对应值',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fileID_key` (`fileID`,`key`),
  KEY `fileID` (`fileID`),
  KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文件扩展表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_source`;
CREATE TABLE `io_source` (
  `sourceID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sourceHash` varchar(20) NOT NULL COMMENT ' id的hash',
  `targetType` tinyint(3) unsigned NOT NULL COMMENT '文档所属类型 (0-sys,1-user,2-group)',
  `targetID` bigint(20) unsigned NOT NULL COMMENT '拥有者对象id',
  `createUser` bigint(20) unsigned NOT NULL COMMENT '创建者id',
  `modifyUser` bigint(20) unsigned NOT NULL COMMENT '最后修改者',
  `isFolder` tinyint(4) unsigned NOT NULL COMMENT '是否为文件夹(0否,1是)',
  `name` varchar(256) NOT NULL COMMENT '文件名',
  `fileType` varchar(10) NOT NULL COMMENT '文件扩展名，文件夹则为空',
  `parentID` bigint(20) unsigned NOT NULL COMMENT '父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建',
  `parentLevel` varchar(2000) NOT NULL COMMENT '父路径id; 例如:  ,2,5,10,',
  `fileID` bigint(20) unsigned NOT NULL COMMENT '对应存储资源id,文件夹则该处为0',
  `isDelete` tinyint(4) unsigned NOT NULL COMMENT '是否删除(0-正常 1-已删除)',
  `size` bigint(20) unsigned NOT NULL COMMENT '占用空间大小',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `viewTime` bigint(20) unsigned NOT NULL COMMENT '最后访问时间',
	`sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '排序/置顶',
  PRIMARY KEY (`sourceID`),
  KEY `targetType` (`targetType`),
  KEY `targetID` (`targetID`),
  KEY `createUser` (`createUser`),
  KEY `isFolder` (`isFolder`),
  KEY `fileType` (`fileType`),
  KEY `parentID` (`parentID`),
  KEY `parentLevel` (`parentLevel`(333)),
  KEY `fileID` (`fileID`),
  KEY `isDelete` (`isDelete`),
  KEY `size` (`size`),
  KEY `modifyTime` (`modifyTime`),
  KEY `createTime` (`createTime`),
  KEY `viewTime` (`viewTime`),
  KEY `modifyUser` (`modifyUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档数据表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_source_auth`;
CREATE TABLE `io_source_auth` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '文档资源id',
  `targetType` tinyint(4) unsigned NOT NULL COMMENT '分享给的对象,1用户,2部门',
  `targetID` bigint(20) unsigned NOT NULL COMMENT '所属对象id',
  `authID` int(11) unsigned NOT NULL COMMENT '权限组id；自定义权限则为0',
  `authDefine` int(11) NOT NULL COMMENT '自定义权限，4字节占位',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `sourceID` (`sourceID`),
  KEY `userID` (`targetType`),
  KEY `groupID` (`targetID`),
  KEY `auth` (`authID`),
  KEY `authDefine` (`authDefine`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档权限表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_source_event`;
CREATE TABLE `io_source_event` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '文档id',
  `sourceParent` bigint(20) unsigned NOT NULL COMMENT '文档父文件夹id',
  `userID` bigint(20) unsigned NOT NULL COMMENT '操作者id',
  `type` varchar(255) NOT NULL COMMENT '事件类型',
  `desc` text NOT NULL COMMENT '数据详情，根据type内容意义不同',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `sourceID` (`sourceID`),
  KEY `sourceParent` (`sourceParent`),
  KEY `userID` (`userID`),
  KEY `eventType` (`type`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档事件表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_source_history`;
CREATE TABLE `io_source_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '文档资源id',
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id, 对部门时此id为0',
  `fileID` bigint(20) unsigned NOT NULL COMMENT '当前版本对应存储资源id',
  `size` bigint(20) NOT NULL COMMENT '文件大小',
  `detail` varchar(1024) NOT NULL COMMENT '版本描述',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `sourceID` (`sourceID`),
  KEY `userID` (`userID`),
  KEY `fileID` (`fileID`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档历史记录表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_source_meta`;
CREATE TABLE `io_source_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '文档id',
  `key` varchar(255) NOT NULL COMMENT '存储key',
  `value` text NOT NULL COMMENT '对应值',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sourceID_key` (`sourceID`,`key`),
  KEY `sourceID` (`sourceID`),
  KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档扩展表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `io_source_recycle`;
CREATE TABLE `io_source_recycle` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `targetType` tinyint(3) unsigned NOT NULL COMMENT '文档所属类型 (0-sys,1-user,2-group)',
  `targetID` bigint(20) unsigned NOT NULL COMMENT '拥有者对象id',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '文档id',
  `userID` bigint(20) unsigned NOT NULL COMMENT '操作者id',
  `parentLevel` varchar(1000) NOT NULL COMMENT '文档上层关系;冗余字段,便于统计回收站信息',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `sourceID` (`sourceID`),
  KEY `userID` (`userID`),
  KEY `createTime` (`createTime`),
  KEY `parentLevel` (`parentLevel`(333)),
  KEY `targetType` (`targetType`),
  KEY `targetID` (`targetID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='文档回收站' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `share`;
CREATE TABLE `share` (
  `shareID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `title` varchar(255) NOT NULL COMMENT '分享标题',
  `shareHash` varchar(50) NOT NULL COMMENT 'shareid',
  `userID` bigint(20) unsigned NOT NULL COMMENT '分享用户id',
  `sourceID` bigint(20) NOT NULL COMMENT '用户数据id',
  `sourcePath` varchar(1024) NOT NULL COMMENT '分享文档路径',
  `url` varchar(255) NOT NULL COMMENT '分享别名,替代shareHash',
  `isLink` tinyint(4) unsigned NOT NULL COMMENT '是否外链分享；默认为0',
  `isShareTo` tinyint(4) unsigned NOT NULL COMMENT '是否为内部分享；默认为0',
  `password` varchar(255) NOT NULL COMMENT '访问密码,为空则无密码',
  `timeTo` bigint(20) unsigned NOT NULL COMMENT '到期时间,0-永久生效',
  `numView` int(11) unsigned NOT NULL COMMENT '预览次数',
  `numDownload` int(11) unsigned NOT NULL COMMENT '下载次数',
  `options` varchar(1000) NOT NULL COMMENT 'json 配置信息;是否可以下载,是否可以上传等',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`shareID`),
  KEY `userID` (`userID`),
  KEY `createTime` (`createTime`),
  KEY `modifyTime` (`modifyTime`),
  KEY `path` (`sourceID`),
  KEY `sid` (`shareHash`),
  KEY `public` (`isLink`),
  KEY `timeTo` (`timeTo`),
  KEY `numView` (`numView`),
  KEY `numDownload` (`numDownload`),
  KEY `isShareTo` (`isShareTo`),
  KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='分享数据表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `share_report`;
CREATE TABLE `share_report` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `shareID` bigint(20) unsigned NOT NULL COMMENT '分享id',
  `title` varchar(255) NOT NULL COMMENT '分享标题',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '举报资源id',
  `fileID` bigint(20) unsigned NOT NULL COMMENT '举报文件id,文件夹则该处为0',
  `userID` bigint(20) unsigned NOT NULL COMMENT '举报用户id',
  `type` tinyint(3) unsigned NOT NULL COMMENT '举报类型 (1-侵权,2-色情,3-暴力,4-政治,5-其他)',
  `desc` text NOT NULL COMMENT '举报原因（其他）描述',
  `status` tinyint(3) unsigned NOT NULL COMMENT '处理状态(0-未处理,1-已处理,2-禁止分享)',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `shareID` (`shareID`),
  KEY `sourceID` (`sourceID`),
  KEY `fileID` (`fileID`),
  KEY `userID` (`userID`),
  KEY `type` (`type`),
  KEY `modifyTime` (`modifyTime`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='分享举报表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `share_to`;
CREATE TABLE `share_to` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `shareID` bigint(20) unsigned NOT NULL COMMENT '分享id',
  `targetType` tinyint(4) unsigned NOT NULL COMMENT '分享给的对象,1用户,2部门',
  `targetID` bigint(20) unsigned NOT NULL COMMENT '所属对象id',
  `authID` int(11) unsigned NOT NULL COMMENT '权限组id；自定义权限则为0',
  `authDefine` int(11) NOT NULL COMMENT '自定义权限，4字节占位',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `shareID` (`shareID`),
  KEY `userID` (`targetType`),
  KEY `targetID` (`targetID`),
  KEY `authDefine` (`authDefine`),
  KEY `authID` (`authID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='分享给指定用户(协作)' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sessionID` varchar(128) NOT NULL COMMENT 'session识别码，用于登陆时记录ip,UA等信息',
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `type` varchar(255) NOT NULL COMMENT '日志类型',
  `desc` text NOT NULL COMMENT '详情',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `visit_date` date NOT NULL COMMENT '日期',
	`client_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '1pc , 2h5, 3安卓app, 4 ios-app, 5小程序',
	`osName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '操作系统',
  PRIMARY KEY (`id`),
  KEY `userID` (`userID`),
  KEY `type` (`type`),
  KEY `createTime` (`createTime`),
  KEY `sessionID` (`sessionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='系统日志表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `system_option`;
CREATE TABLE `system_option` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL COMMENT '配置类型',
  `key` varchar(255) NOT NULL,
  `value` text NOT NULL,
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_type` (`key`,`type`),
  KEY `createTime` (`createTime`),
  KEY `modifyTime` (`modifyTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='系统配置表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `system_session`;
CREATE TABLE `system_session` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sign` varchar(128) NOT NULL COMMENT 'session标识',
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `content` text NOT NULL COMMENT 'value',
  `expires` bigint(20) unsigned NOT NULL COMMENT '过期时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sign` (`sign`),
  KEY `userID` (`userID`),
  KEY `expires` (`expires`),
  KEY `modifyTime` (`modifyTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='session' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) NOT NULL COMMENT '登陆用户名',
  `roleID` int(11) unsigned NOT NULL COMMENT '用户角色',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) NOT NULL COMMENT '手机',
  `nickname` varchar(255) NOT NULL COMMENT '昵称',
  `avatar` varchar(255) NOT NULL COMMENT '头像',
  `sex` tinyint(4) unsigned NOT NULL COMMENT '性别 (0女1男)',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `sizeMax` double unsigned NOT NULL COMMENT '群组存储空间大小(GB) 0-不限制',
  `sizeUse` bigint(20) unsigned NOT NULL COMMENT '已使用大小(byte)',
  `status` tinyint(3) unsigned NOT NULL COMMENT '用户启用状态 0-未启用 1-启用',
  `is_system` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否是系统用户',
  `lastLogin` bigint(20) unsigned NOT NULL COMMENT '最后登陆时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`userID`),
  KEY `name` (`name`),
  KEY `email` (`email`),
  KEY `status` (`status`),
  KEY `modifyTime` (`modifyTime`),
  KEY `lastLogin` (`lastLogin`),
  KEY `createTime` (`createTime`),
  KEY `nickname` (`nickname`),
  KEY `phone` (`phone`),
  KEY `sizeUse` (`sizeUse`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='用户表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

DROP TABLE IF EXISTS `user_fav`;
CREATE TABLE `user_fav` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `tagID` int(11) unsigned NOT NULL COMMENT '标签id,收藏则为0',
  `name` varchar(255) NOT NULL COMMENT '收藏名称',
  `path` varchar(2048) NOT NULL COMMENT '收藏路径,tag时则为sourceID',
  `type` varchar(20) NOT NULL COMMENT 'source/path',
  `sort` int(11) unsigned NOT NULL COMMENT '排序',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `createTime` (`createTime`),
  KEY `userID` (`userID`),
  KEY `name` (`name`),
  KEY `sort` (`sort`),
  KEY `tagID` (`tagID`),
  KEY `path` (`path`(333)),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='用户文档标签表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `groupID` bigint(20) unsigned NOT NULL COMMENT '群组id',
  `authID` int(11) unsigned NOT NULL COMMENT '在群组内的权限',
  `sort` int(11) unsigned NOT NULL COMMENT '在该群组的排序',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userID_groupID` (`userID`,`groupID`),
  KEY `userID` (`userID`),
  KEY `groupID` (`groupID`),
  KEY `groupRole` (`authID`),
  KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='用户群组关联表(一对多)' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `user_meta`;
CREATE TABLE `user_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `key` varchar(255) NOT NULL COMMENT '存储key',
  `value` text NOT NULL COMMENT '对应值',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userID_metaKey` (`userID`,`key`),
  KEY `userID` (`userID`),
  KEY `metaKey` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='用户数据扩展表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


DROP TABLE IF EXISTS `user_option`;
CREATE TABLE `user_option` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `type` varchar(50) NOT NULL COMMENT '配置类型,全局配置类型为空,编辑器配置type=editor',
  `key` varchar(255) NOT NULL COMMENT '配置key',
  `value` text NOT NULL COMMENT '配置值',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  `modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userID_key_type` (`userID`,`key`,`type`),
  KEY `userID` (`userID`),
  KEY `key` (`key`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='用户数据配置表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

CREATE TABLE IF NOT EXISTS notice (
	`notice_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知id',
	`title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
	`detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '内容',
	`level` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态，0暂存，1已发送，2已删除',
	`enable` tinyint UNSIGNED NOT NULL COMMENT '是否启用，0未启用，1启用',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '通知创建时间',
	`sendTime` bigint(20) unsigned NOT NULL COMMENT '通知发送时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
	`userID` bigint(20) unsigned NOT NULL COMMENT '通知发送者',
	`user_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发送通知的IP地址，json(222.22.22.22,杭州)',
	`notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '消息类型，1通知2消息3私信',
	`receiver` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '收信人json格式，{all:0否1是，user用户:用户id逗号隔开，group，role}',
	PRIMARY KEY (`notice_id`),
	KEY `notice_user_id_Idx` (`userID`),
	KEY `notice_title_Idx` (`title`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '通知表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

CREATE TABLE IF NOT EXISTS notice_detail (
	`notice_id` bigint UNSIGNED NOT NULL COMMENT '通知id',
	`userID` bigint(20) unsigned NOT NULL COMMENT '接收者id',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态，0未读，1已读',
	`user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名称',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '修改时间',
	PRIMARY KEY (`notice_id`, `userID`),
	KEY `notice_detail_user_id_Idx` (`userID`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '通知任务表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

CREATE TABLE IF NOT EXISTS common_label (
	`label_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签id',
	`userID` bigint(20) unsigned NOT NULL COMMENT '用户id',
	`label_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标签名称',
	`label_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '英文名',
	`en_name_simple` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '英文名',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态，正常，删除',
	`style` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '',
	`sort` int(11) unsigned NOT NULL COMMENT '排序',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '修改时间',
	PRIMARY KEY (`label_id`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '标签表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

CREATE TABLE IF NOT EXISTS role (
	`roleID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
	`role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
	`code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'code',
	`description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '描述',
	`label` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'label',
	`auth` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
	`deleteTime` bigint(20) unsigned NOT NULL COMMENT '删除时间',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态，1正常，2删除',
	`enable` tinyint UNSIGNED NOT NULL COMMENT '是否启用，0未启用，1启用',
	`is_system` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否系统配置',
	`administrator` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否系统管理员',
	`ignoreFileSize` double NOT NULL DEFAULT '0' COMMENT '上传文件大小限制',
	`ignoreExt` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '',
	`sort` int(11) unsigned NOT NULL COMMENT '排序',
	`role_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色类型，1用户角色，2文档角色',
	PRIMARY KEY (`roleID`),
	KEY `role_role_name_Idx` (`role_name`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '角色表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


CREATE TABLE IF NOT EXISTS `group_source` (
  `groupID` bigint(20) unsigned NOT NULL COMMENT '群组id',
  `sourceID` bigint(20) unsigned NOT NULL COMMENT '文件夹id',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
  UNIQUE KEY `groupID_sourceID` (`groupID`,`sourceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_bin COMMENT='群组、文件关系表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

INSERT INTO `role` (`roleID`, `role_name`, `code`, `description`, `label`, `auth`, `createTime`, `modifyTime`, `deleteTime`, `status`, `enable`, `is_system`, `administrator`, `ignoreFileSize`, `ignoreExt`, `sort`, role_type)
 VALUES
(1,	'系统管理员', 'admin.role.administrator',	'超级管理员,拥有服务器管理权限; 所有文件文件夹设置权限对该用户无效!',	'label-green-deep', 'explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav,admin.index.dashboard,admin.index.setting,admin.index.loginLog,admin.index.log,admin.index.server,admin.role.list,admin.role.edit,admin.job.list,admin.job.edit,admin.member.list,admin.member.userEdit,admin.member.groupEdit,admin.auth.list,admin.auth.edit,admin.plugin.list,admin.plugin.edit,admin.storage.list,admin.storage.edit,admin.autoTask.list,admin.autoTask.edit',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 1, 0, '', 0, '1'),
(2,	'部门管理员', 'admin.role.group',	'',	'label-blue-deep', 'explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav,admin.index.loginLog,admin.index.log,admin.member.list,admin.member.userEdit,admin.member.groupEdit,admin.auth.list',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()),1, 1, 1, 0, 0, '', 1, '1'),
(3,	'普通用户', 'admin.role.default', 	'',	'label-blue-normal', 'explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 2, '1'),
(4,	'编辑者', 'admin.auth.editor', 	'',	'label-blue-deep', '1,2,3,4,5,6,7,8,9,10,11,12',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 1, '2'),
(5,	'编辑/上传者', 'admin.auth.editUploader', 	'',	'label-blue-normal', '1,2,3,4,5,6,7,8,9,10,11,12',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 2, '2'),
(6,	'预览者', 'admin.auth.previewer', 	'',	'label-yellow-deep', '1,2',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 3, '2'),
(7,	'上传者', 'admin.auth.uploader', 	'',	'label-yellow-light', '1,4',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 4, '2'),
(8,	'拥有者', 'admin.auth.owner', 	'',	'label-green-normal', '1,2,3,4,5,6,7,8,9,10,11,12',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 5, '2'),
(9,	'查看者', 'admin.auth.viewer', 	'',	'label-blue-light', '1',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 6, '2'),
(10,	'不可见', 'admin.auth.invisible', 	'',	'label-grey-light', '',	unix_timestamp(now()), unix_timestamp(now()), unix_timestamp(now()), 1, 1, 1, 0, 0, '', 7, '2');

INSERT INTO `user` (`userID`, `name`, `roleID`, `email`, `phone`, `nickname`, `avatar`, `sex`, `password`, `sizeMax`, `sizeUse`, `status`, `is_system`, `lastLogin`, `modifyTime`, `createTime`) VALUES
(1,	'admin',	1,	'',	'',	'系统管理员',	'',	1,	'27EA0ED68E00F9F34ACBF89B499BF465',	0,	0,	1, 1,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now()));

INSERT INTO `group` (`groupID`, `name`, `parentID`, `parentLevel`, `extraField`, `sort`, `sizeMax`, `sizeUse`, `modifyTime`, `createTime`,`status`) VALUES
(1,	'企业网盘',	0,	',0,',	NULL,	1,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()), 1);


INSERT INTO `user_group` (`id`, `userID`, `groupID`, `authID`, `sort`, `createTime`, `modifyTime`) VALUES
(1,	1,	1,	4,	0,	unix_timestamp(now()),	unix_timestamp(now()));

INSERT INTO `user_meta` (`id`, `userID`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(1,	1,	'passwordSalt',	'eHd27KPvuX',	unix_timestamp(now()),	unix_timestamp(now())),
(2,	1,	'namePinyin',	'xitongguanliyuan',	unix_timestamp(now()),	unix_timestamp(now())),
(3,	1,	'namePinyinSimple',	'xtgly',	unix_timestamp(now()),	unix_timestamp(now()));

INSERT INTO `group_meta` (`id`, `groupID`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(1,	1,	'namePinyin',	'qiyewangpan',	unix_timestamp(now()),	unix_timestamp(now())),
(2,	1,	'namePinyinSimple',	'qywp',	unix_timestamp(now()),	unix_timestamp(now())),
(3,	1,	'systemGroupSource',	'1',	unix_timestamp(now()),	unix_timestamp(now()));

INSERT INTO `io_source` (`sourceID`, `sourceHash`, `targetType`, `targetID`, `createUser`, `modifyUser`, `isFolder`, `name`, `fileType`, `parentID`, `parentLevel`, `fileID`, `isDelete`, `size`, `createTime`, `modifyTime`, `viewTime`) VALUES
(1,	'',	2,	1,	1,	1,	1,	'企业网盘',	'',	0,	',0,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(2,	'',	2,	1,	1,	4,	1,	'共享资源',	'',	1,	',0,1,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(3,	'',	2,	1,	1,	1,	1,	'文档',	'',	1,	',0,1,',	0,	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now())),
(4,	'',	2,	1,	1,	1,	1,	'其他',	'',	1,	',0,1,',	0,	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now())),
(5,	'',	1,	1,	1,	1,	1,	'admin',	'',	0,	',0,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(6,	'',	1,	1,	1,	1,	1,	'桌面',	'',	5,	',0,5,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(7,	'',	1,	1,	1,	1,	1,	'我的文档',	'',	5,	',0,5,',	0,	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now())),
(8,	'',	1,	1,	1,	1,	1,	'我的图片',	'',	5,	',0,5,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(9,	'',	1,	1,	1,	1,	1,	'我的音乐',	'',	5,	',0,5,',	0,	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now())),
(12,	'',	0,	0,	1,	1,	1,	'systemPath',	'',	0,	',0,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(13,	'',	0,	0,	1,	0,	1,	'systemSource',	'',	12,	',0,12,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(14,	'',	0,	0,	1,	1,	1,	'systemTemp',	'',	12,	',0,12,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(15,	'',	0,	0,	1,	1,	1,	'systemRecycle',	'',	12,	',0,12,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now())),
(16,	'',	0,	0,	0,	0,	1,	'attachmentTemp',	'',	13,	',0,12,13,',	0,	0,	0,	unix_timestamp(now()),	unix_timestamp(now()),	unix_timestamp(now()));

INSERT INTO `io_source_event` (`id`, `sourceID`, `sourceParent`, `userID`, `type`, `desc`, `createTime`) VALUES
(1,	2,	1,	1,	'create',	'{\n    \"createType\": \"mkdir\",\n    \"name\": \"共享资源\"\n}',	unix_timestamp(now())),
(2,	3,	1,	1,	'create',	'{\n    \"createType\": \"mkdir\",\n    \"name\": \"文档\"\n}',	unix_timestamp(now())),
(3,	4,	1,	1,	'create',	'{\n    \"createType\": \"mkdir\",\n    \"name\": \"其他\"\n}',	unix_timestamp(now())),
(4,	6,	5,	1,	'create',	'{\n    \"createType\": \"mkdir\",\n    \"name\": \"桌面\"\n}',	unix_timestamp(now()));

INSERT INTO `system_option` (`id`, `type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(1,	'',	'systemLogo',	'./static/images/common/logo.png',	unix_timestamp(now()),	unix_timestamp(now())),
(2,	'',	'systemName',	'视频中心',	unix_timestamp(now()),	unix_timestamp(now())),
(3,	'',	'systemDesc',	'资源管理器',	unix_timestamp(now()),	unix_timestamp(now())),
(4,	'',	'groupRootName',	'企业网盘',	unix_timestamp(now()),	unix_timestamp(now())),
(5,	'',	'browserLogo',	'./static/images/common/logo-kod.png',	unix_timestamp(now()),	unix_timestamp(now())),
(6,	'',	'globalIcp',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(7,	'',	'seo',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(8,	'',	'seoDesc',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(9,	'',	'meta',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(10,	'',	'passwordErrorLock',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(11,	'',	'needCheckCode',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(12,	'',	'passwordRule',	'none',	unix_timestamp(now()),	unix_timestamp(now())),
(13,	'',	'showFileLink',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(14,	'',	'showFileMd5',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(15,	'',	'shareLinkAllow',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(16,	'',	'shareLinkPasswordAllowEmpty',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(17,	'',	'shareLinkAllowGuest',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(18,	'',	'treeOpen',	'my,myFav,rootGroup,recentDoc,fileType,fileTag,shareLink',	unix_timestamp(now()),	unix_timestamp(now())),
(19,	'',	'dateFormat',	'Y-m-d',	unix_timestamp(now()),	unix_timestamp(now())),
(20,	'',	'newUserFolder',	'视频,音乐,文档,图片,压缩包,其他',	unix_timestamp(now()),	unix_timestamp(now()))
;

INSERT INTO `user_option` (`id`, `userID`, `type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(1,	1,	'',	'listType',	'icon',	unix_timestamp(now()),	unix_timestamp(now())),
(2,	1,	'',	'listSortField',	'name',	unix_timestamp(now()),	unix_timestamp(now())),
(3,	1,	'',	'listSortOrder',	'up',	unix_timestamp(now()),	unix_timestamp(now())),
(4,	1,	'',	'fileIconSize',	'80',	unix_timestamp(now()),	unix_timestamp(now())),
(5,	1,	'',	'fileOpenClick',	'dbclick',	unix_timestamp(now()),	unix_timestamp(now())),
(6,	1,	'',	'fileShowDesc',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(7,	1,	'',	'animateOpen',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(8,	1,	'',	'soundOpen',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(9,	1,	'',	'theme',	'auto',	unix_timestamp(now()),	unix_timestamp(now())),
(10,	1,	'',	'themeImage',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(11,	1,	'',	'wall',	'4',	unix_timestamp(now()),	unix_timestamp(now())),
(12,	1,	'',	'listTypeKeep',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(13,	1,	'',	'listSortKeep',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(14,	1,	'',	'fileRepeat',	'replace',	unix_timestamp(now()),	unix_timestamp(now())),
(15,	1,	'',	'recycleOpen',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(16,	1,	'',	'kodAppDefault',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(17,	1,	'',	'fileIconSizeDesktop',	'70',	unix_timestamp(now()),	unix_timestamp(now())),
(18,	1,	'',	'fileIconSizePhoto',	'120',	unix_timestamp(now()),	unix_timestamp(now())),
(19,	1,	'',	'photoConfig',	'',	unix_timestamp(now()),	unix_timestamp(now())),
(20,	1,	'',	'resizeConfig',	'{\"filename\":250,\"filetype\":80,\"filesize\":80,\"filetime\":215,\"editorTreeWidth\":200,\"explorerTreeWidth\":200}',	unix_timestamp(now()),	unix_timestamp(now())),
(21,	1,	'',	'imageThumb',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(22,	1,	'',	'fileSelect',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(23,	1,	'',	'displayHideFile',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
(24,	1,	'',	'filePanel',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(25,	1,	'',	'shareToMeShowType',	'list',	unix_timestamp(now()),	unix_timestamp(now())),
(26,	1,	'',	'messageSendType',	'enter',	unix_timestamp(now()),	unix_timestamp(now())),
(27,	1,	'',	'loginDevice',	'',	unix_timestamp(now()),	unix_timestamp(now()));

INSERT INTO `io_source_meta` (`id`, `sourceID`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(1,	2,	'namePinyin',	'gongxiangziyuan',	unix_timestamp(now()),	unix_timestamp(now())),
(2,	2,	'namePinyinSimple',	'gxzy',	unix_timestamp(now()),	unix_timestamp(now())),
(3,	3,	'namePinyin',	'wendang',	unix_timestamp(now()),	unix_timestamp(now())),
(4,	3,	'namePinyinSimple',	'wd',	unix_timestamp(now()),	unix_timestamp(now())),
(5,	4,	'namePinyin',	'qita',	unix_timestamp(now()),	unix_timestamp(now())),
(6,	4,	'namePinyinSimple',	'qt',	unix_timestamp(now()),	unix_timestamp(now())),
(10,	5,	'desktopSource',	'6',	unix_timestamp(now()),	unix_timestamp(now())),
(7,	6,	'namePinyin',	'zhuomian',	unix_timestamp(now()),	unix_timestamp(now())),
(8,	6,	'namePinyinSimple',	'zm',	unix_timestamp(now()),	unix_timestamp(now())),
(9,	6,	'desktop',	'1',	unix_timestamp(now()),	unix_timestamp(now())),
(11,	7,	'namePinyin',	'wodewendang',	unix_timestamp(now()),	unix_timestamp(now())),
(12,	7,	'namePinyinSimple',	'wdwd',	unix_timestamp(now()),	unix_timestamp(now())),
(13,	8,	'namePinyin',	'wodetupian',	unix_timestamp(now()),	unix_timestamp(now())),
(14,	8,	'namePinyinSimple',	'wdtp',	unix_timestamp(now()),	unix_timestamp(now())),
(15,	9,	'namePinyin',	'wodeyinle',	unix_timestamp(now()),	unix_timestamp(now())),
(16,	9,	'namePinyinSimple',	'wdyl',	unix_timestamp(now()),	unix_timestamp(now()));


INSERT INTO `group_source` (`groupID`, `sourceID`, `createTime`) VALUES
(1,	1, unix_timestamp(now()));

--changeset slj:2
INSERT INTO `system_option` (`type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
('',	'needMark',	'0',	unix_timestamp(now()),	unix_timestamp(now()));
--changeset slj:3
update system_option SET `value`='图片,文档,视频,音乐,其他' where `key` = 'newUserFolder' and type = '';
--changeset slj:4
update system_option SET `value`='' where `key` = 'systemLogo' and type = '';
update system_option SET `value`='' where `key` = 'browserLogo' and type = '';

--changeset slj:5
CREATE TABLE IF NOT EXISTS common_schedule (
	`common_schedule_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '定时任务标识',
	`schedule_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '定时任务名称',
	`gmt_modified` datetime NOT NULL COMMENT '任务重置时间',
	`frequency` int UNSIGNED NOT NULL COMMENT '执行频率(秒)',
	`api_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '手动执行接口地址',
	PRIMARY KEY (`common_schedule_id`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '定时任务表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

CREATE TABLE IF NOT EXISTS log_schedule (
	`log_schedule_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '任务执行日志主键',
	`common_schedule_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '定时任务标识',
	`gmt_start` datetime NOT NULL COMMENT '任务执行开始时间',
	`gmt_end` datetime NOT NULL COMMENT '任务执行结束时间',
	`state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '状态，0开始，1执行成功，2执行失败',
	`remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '备注，如有执行异常，记录异常信息',
	PRIMARY KEY (`log_schedule_id`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '任务执行记录表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

INSERT INTO common_schedule(common_schedule_id, schedule_name, gmt_modified, frequency, api_url)
VALUES ('video_convert_time', '转码时长监控', '2023-03-18 15:34:59', 86390, '');


--changeset slj:6
CREATE TABLE IF NOT EXISTS common_convert (
	`convertID` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '转码主键',
	`sourceID` bigint(20) unsigned NOT NULL COMMENT '对应存储资源id',
	`fileID` bigint(20) unsigned NOT NULL COMMENT '对应存储资源fileID',
	`userID` bigint(20) unsigned NOT NULL COMMENT '执行人ID',
	`name` varchar(256) NOT NULL COMMENT '文件名',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`frequencyCount` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '执行次数',
	`state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '状态，0开始，1执行成功，2执行失败',
	`remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '备注，如有执行异常，记录异常信息',
	UNIQUE KEY `convertID`  (`convertID`),
	KEY `sourceID` (`sourceID`),
  KEY `fileID` (`fileID`),
  KEY `userID` (`userID`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '转码表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

--changeset slj:7
ALTER TABLE common_convert ADD COLUMN `scheduleFrequencyCount` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '自动执行次数';
--changeset slj:8
ALTER TABLE common_convert ADD COLUMN `scheduleTime` bigint(20) unsigned NOT NULL COMMENT '定时任务执行修改时间';


--changeset slj:9
DROP TABLE IF EXISTS `common_convert`;
CREATE TABLE `common_convert` (
	`convertID` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '转码主键',
	`sourceID` bigint(20) unsigned NOT NULL COMMENT '对应存储资源id',
	`fileID` bigint(20) unsigned NOT NULL COMMENT '对应存储资源fileID',
	`userID` bigint(20) unsigned NOT NULL COMMENT '执行人ID',
	`name` varchar(256) NOT NULL COMMENT '文件名',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
  `createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`frequencyCount` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '执行次数',
	`state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '状态，0开始，1执行成功，2执行失败',
	`remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '备注，如有执行异常，记录异常信息',
	`scheduleFrequencyCount` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '自动执行次数',
	`scheduleTime` bigint(20) unsigned NOT NULL COMMENT '定时任务执行修改时间',
	UNIQUE KEY `convertID`  (`convertID`),
	KEY `sourceID` (`sourceID`),
	KEY `state` (`state`),
  KEY `userID` (`userID`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '转码表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

--changeset slj:10
ALTER TABLE io_file ADD COLUMN `isExistFile` tinyint(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '原始文件是否存在 0 否 1 是';
--changeset slj:11
UPDATE role SET auth = '1,2,3,4,5,6,7,8,9,10,11,12,13,14' where roleID = 8;
UPDATE role SET auth = '1,2,3,4,5,6,7,8,9,10,12,13' where roleID = 5;
UPDATE role SET auth = '1,2,3,4,5,6,7,8,9,10,11,12,13' where roleID = 4;
UPDATE role SET auth = '1,5' where roleID = 7;
UPDATE role SET auth = '1,3' where roleID = 6;
UPDATE role SET auth = '1,3,4,13' where roleID = 9;

--changeset slj:12
create table `visit_count_record`
(
    id         BIGINT UNSIGNED auto_increment comment '主键id',
    visitCount BIGINT UNSIGNED not null comment '用户访问次数',
    deviceType TINYINT default null comment '设备类型 1 pc , 2 h5, 3 安卓app, 4 ios-app, 5 小程序, 6 电脑app, 7 其他',
    visitDay   DATE            not null comment '访问的日期',
    modifyTime BIGINT UNSIGNED not null comment '最后修改时间',
    createTime BIGINT UNSIGNED not null comment '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='访问次数记录表';

--changeset slj:13
UPDATE role SET auth = '1,2,3,4,5,6,7,8,9,12,13' where roleID = 5;

--changeset slj:14
ALTER TABLE io_source
    ADD COLUMN `type` TINYINT(3) UNSIGNED default null comment '文档类型 1 文档 2 图片 3 音乐 4 视频 5 压缩包 6 其他' AFTER sourceHash;

UPDATE io_source SET `type` = (
    CASE
    WHEN find_in_set(fileType, 'txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp') THEN 1
    WHEN find_in_set(fileType,'jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai') THEN 2
    WHEN find_in_set(fileType,'mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape') THEN 3
    WHEN find_in_set(fileType,'mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx,mpa') THEN 4
    WHEN find_in_set(fileType,'zip,gz,rar,iso,tar,7z,ar,bz,bz2,xz,arj') THEN 5
    ELSE 6
    END
    ) WHERE isFolder = 0;

UPDATE io_source SET `type` = NULL WHERE isFolder = 1;

--changeset slj:15
UPDATE io_source SET `type` = 0 WHERE isFolder = 1;

--changeset slj:16
ALTER TABLE `visit_count_record` ADD COLUMN osName VARCHAR(20) DEFAULT NULL COMMENT '操作系统' AFTER deviceType;
ALTER TABLE `visit_count_record` ADD COLUMN `type` TINYINT(3) DEFAULT 1 COMMENT '1 客户端访问 2 用户访问 3 操作系统访问 4 各操作系统总访问' AFTER osName;

--changeset slj:17
INSERT INTO common_schedule(common_schedule_id, schedule_name, gmt_modified, frequency, api_url)
VALUES ('cloud_delete_download', '删除下载临时文件', '2023-04-15 12:00:59', 86390, '');

--changeset slj:18
UPDATE role SET label = '#533eb4' where roleID = 1;
UPDATE role SET label = '#108ee9' where roleID = 2;
UPDATE role SET label = '#3dbd7d' where roleID = 3;

UPDATE role SET label = '#f46e65' where roleID = 4;
UPDATE role SET label = '#fdd8e7' where roleID = 5;
UPDATE role SET label = '#a7e1c4' where roleID = 6;
UPDATE role SET label = '#ffce3d' where roleID = 7;
UPDATE role SET label = '#533eb4' where roleID = 8;
UPDATE role SET label = '#3dbd7d' where roleID = 9;
UPDATE role SET label = '#fff' where roleID = 10;

--changeset slj:19
INSERT INTO `system_option` (`type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
('System.pluginList',	'ID-1',	'{"name":"OfficeWeb365","status":1,"id":1,"description":"offic文件在线查看","type":"ow365"}',	unix_timestamp(now()),	unix_timestamp(now())),
('System.pluginList',	'ID-2',	'{"name":"永中Office","status":1,"id":2,"description":"offic文件在线编辑","type":"yzow"}',	unix_timestamp(now()),	unix_timestamp(now()))
;
--changeset slj:20
UPDATE system_option SET `value` = '{"name":"OfficeWeb365","status":1,"id":1,"description":"office文件在线查看","type":"ow365"}'
 WHERE `type` = 'System.pluginList' and `key` = 'ID-1';
UPDATE system_option SET `value` = '{"name":"永中Office","status":1,"id":2,"description":"office文件在线编辑","type":"yzow"}'
 WHERE `type` = 'System.pluginList' and `key` = 'ID-2';

--changeset slj:21
ALTER TABLE `user` ADD COLUMN dingOpenId VARCHAR(64) DEFAULT NULL COMMENT '钉钉 openId' AFTER `status`;
ALTER TABLE `user` ADD COLUMN wechatOpenId VARCHAR(64) DEFAULT NULL COMMENT '微信 openId' AFTER dingOpenId;
ALTER TABLE `user` ADD COLUMN alipayOpenId VARCHAR(64) DEFAULT NULL COMMENT '支付宝 openId' AFTER wechatOpenId;

--changeset slj:22
INSERT INTO `system_option` (`type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(	'',	'markType',	'0',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmTitle',	'FilesBox',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmColor',	'#eae0f8',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmFont',	'SimSun',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmSize',	'28',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmSubTitle',	'{userLoginTime} - {userNickName}',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmSubColor',	'#d9d9d9',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmSubFont',	'SimSun',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmSubSize',	'15',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmTransparency',	'50',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmRotate',	'45',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmMargin',	'',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmPicPath',	'',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmPicSize',	'',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'wmPosition',	'',	unix_timestamp(now()),	unix_timestamp(now()))
;

--changeset slj:23
update system_option SET `value`='私有云盘' where `key` = 'systemName' and type = '';
update system_option SET `value`='视频和数字资产文件系统' where `key` = 'systemDesc' and type = '';

--changeset slj:24
ALTER TABLE io_file ADD COLUMN `fileName` varchar(125) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名';

--changeset slj:25
INSERT INTO `system_option` (`type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(	'',	'thirdLoginConfig',	'',	unix_timestamp(now()),	unix_timestamp(now())),
('',	'registerConfig',	'{"enableRegister":true,"needReview":false,"sizeMax":2,"roleID":3,"groupInfo":[{"groupID":1,"authID":6}]}',	unix_timestamp(now()),	unix_timestamp(now()));

--changeset slj:26
update system_option SET `value`='[]' where `key` = 'thirdLoginConfig' and type = '';

--changeset slj:27
ALTER TABLE io_source ADD COLUMN `convertSize` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '转码文件占用空间大小';
ALTER TABLE io_source ADD COLUMN `thumbSize` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '缩略图占用空间';

--changeset slj:28
update system_option SET `value`='{userName} - {userLoginTime}' where `key` = 'wmSubTitle' and type = '';

--changeset slj:29
ALTER TABLE `user` ADD COLUMN enWechatOpenId VARCHAR(64) DEFAULT NULL COMMENT '企业微信 openId' AFTER wechatOpenId;

--changeset slj:30
drop table  if exists notice;
create table notice
(
    id   BIGINT UNSIGNED auto_increment comment '通知id' primary key,
    title       VARCHAR(128)    not null comment '标题',
    level       TINYINT UNSIGNED default 0 comment '0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。',
    status      TINYINT UNSIGNED default 0 comment '状态，0暂存，1已发送，2已删除',
    enable      TINYINT UNSIGNED default 0 comment '是否启用，0未启用，1启用',
    send_time   BIGINT UNSIGNED not null comment '通知发送时间',
    sender_id   BIGINT UNSIGNED not null comment '通知发送者id',
    sender_ip   VARCHAR(64)     not null comment '发送通知的IP地址，json(222.22.22.22,杭州)',
    notice_type TINYINT UNSIGNED default 1 comment '消息类型，1通知2消息3私信',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' NOT NULL,
    modify_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' NULL
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '消息表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

drop table  if exists notice_detail;
create table notice_detail
(
    id  BIGINT UNSIGNED not null comment '主键id' primary key,
    notice_id  BIGINT UNSIGNED not null comment '通知id',
    content    TEXT            not null comment '消息内容',
    is_all     TINYINT UNSIGNED default 1 comment '是否为所有用户 1 是 0 否',
    target_ids JSON            not null comment '接收者',
    `status`   TINYINT UNSIGNED default 0 comment '状态，0未读，1已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' NOT NULL,
    modify_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' NULL
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '消息详情表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

CREATE TABLE notice_user
(
  id BIGINT UNSIGNED auto_increment comment '主键' primary key,
  notice_id  BIGINT UNSIGNED not null comment '通知id',
  user_id  BIGINT UNSIGNED not null comment '用户id',
  is_read tinyint default 0 comment '是否已读 0 未读 1 已读',
  `year` year not null comment '年份',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' NOT NULL,
  modify_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' NULL
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '消息用户表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


--changeset slj:31

CREATE TABLE IF NOT EXISTS common_info_type (
	`infoTypeID` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '类型id',
	`typeName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型名称',
	`parentID` int unsigned NOT NULL COMMENT '父ID',
	`parentLevel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '父路径id; 例如:  ,2,5,10,',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态 0禁用，1启用',
	`sort` smallint NOT NULL DEFAULT '0' COMMENT '排序',
	`namePinyin` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拼音全称',
	`namePinyinSimple` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拼音首字母',
	`createUser` bigint(20) unsigned NOT NULL COMMENT '创建者id',
	PRIMARY KEY (`infoTypeID`),
	KEY `type_name_Idx` (`typeName`),
	KEY `type_sequence_Idx` (`parentLevel`),
	KEY `sort_Idx` (`sort`)
) ENGINE = InnoDB  CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '资讯类型表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


CREATE TABLE IF NOT EXISTS common_info (
	`infoID` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主页资讯id',
	`title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资讯标题',
	`computerPicPath` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资讯图片地址，电脑端',
	`mobilePicPath` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资讯图片地址，手机端',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态 0禁用，1启用',
	`detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资讯内容',
	`fileDetail` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资讯附件地址，最多5个附件，json格式(文件id，文件名，文件类型，地址)',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
	`createUser` bigint(20) unsigned NOT NULL COMMENT '创建者id',
	`modifyUser` bigint(20) unsigned NOT NULL COMMENT '最后修改者',
	`userIp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编辑资讯的IP地址，json(222.22.22.22,杭州)',
	`infoTypeID` int UNSIGNED NOT NULL COMMENT '资讯类型id',
	`sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
	`introduce` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资讯摘要',
	`isTop` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否置顶',
	`topTime` bigint(20) unsigned NOT NULL COMMENT '置顶时间',
	`seo` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'keyword, description json',
	`infoSource` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '资讯来源json, name,author,url',
	`isApplyOriginal` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否申请原创',
	`videoID` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '短视频id',
	`thumb` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '缩略图',
	`previewUrl` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '视频播放地址',
	`infoType` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '0资讯, 1短视频',
	`isVertical` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否竖版',
	`thumbVertical` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '缩略图竖版',
	`computerPicPathVertical` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '原图横板',
	`isVideoExists` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否存在视频',
	`gmtPage` bigint(20) unsigned NOT NULL COMMENT '生成页面时间',
	`userAgent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'ua',
	`isUrlInfo` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否url链接资讯',
	`infoUrl` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'url资讯的地址',
	`attachmentCount` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '附件数',
	`showAttachment` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '前3个附件图',
	`remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备注, 审核拒绝原因等',
	`isTransport` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否转载, 1是',
	`rightFlag` tinyint UNSIGNED NOT NULL DEFAULT '1' COMMENT '权限标识：0-私有，1-公开',
	`sourceID` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '文件id',
	`isHide` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否隐藏 0 默认否  1 是',
	`actualViewCount` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '实际浏览数',
	`viewCount` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '浏览数',
	`namePinyin` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拼音全称',
	`namePinyinSimple` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拼音首字母',
	PRIMARY KEY (`infoID`),
	KEY `common_info_title_Idx` (`title`),
	KEY `common_info_sort_Idx` (`sort`),
	KEY `common_info_sid_sgm_id_Idx` (`status`, `sort`, `modifyTime`, `infoID`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '资讯表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

--changeset slj:32
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
    send_time   DATETIME    null comment '通知发送时间',
    sort        BIGINT          default 0 comment '排序 越大越靠前',
    sender_id   BIGINT UNSIGNED not null comment '通知发送者id',
    sender_ip   VARCHAR(64)         default null comment '发送通知的IP地址，json(222.22.22.22,杭州)',
    notice_type TINYINT UNSIGNED default 1 comment '消息类型，1通知2消息3私信',
    create_time DATETIME        not null comment '创建时间',
    modify_time DATETIME            default null comment '更新时间'
)ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '通知表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

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
    notice_detail_id BIGINT      null comment '通知详情表id',
    create_time      DATETIME        not null comment '创建时间',
    modify_time      DATETIME            default null comment '更新时间'
)ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '通知详情表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

drop table if exists notice_user;
create table notice_user
(
    id          BIGINT UNSIGNED auto_increment comment '主键'
        primary key,
    user_id     BIGINT UNSIGNED not null comment '用户id',
    notice_id   BIGINT UNSIGNED not null comment '通知id集合',
    is_read     TINYINT default 0 comment '是否已读 0 未读 1 已读',
    create_time DATETIME        not null comment '创建时间',
    modify_time DATETIME   default null comment '更新时间'
)ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '通知用户关联表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

--changeset slj:33
ALTER TABLE notice_user ADD UNIQUE INDEX user_notice_key(user_id, notice_id);

--changeset slj:34
ALTER TABLE notice_detail ADD COLUMN dr TINYINT DEFAULT 0 COMMENT '逻辑删除 0 未删除 1 已删除' AFTER modify_time;


--changeset slj:35
drop table if exists share_report;
create table share_report
(
    id          BIGINT UNSIGNED auto_increment comment '自增id'
        primary key,
    share_id    BIGINT UNSIGNED     not null comment '分享id',
    title       VARCHAR(255)        not null comment '分享标题',
    source_id   BIGINT UNSIGNED     not null comment '举报资源id',
    file_id     BIGINT UNSIGNED     not null comment '举报文件id,文件夹则该处为0',
    user_id     BIGINT UNSIGNED     not null comment '举报用户id',
    report_type TINYINT(3) UNSIGNED not null comment '举报类型 (1-侵权,2-色情,3-暴力,4-政治,5-其他)',
    reason      VARCHAR(255)        not null comment '举报原因（其他）描述',
    status      TINYINT(3) UNSIGNED not null comment '处理状态(0-未处理,1-已处理,2-允许分享,3-禁止分享)',
    create_time DATETIME        not null comment '创建时间',
    modify_time DATETIME        null comment '最后修改时间'
)ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '分享举报表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;


--changeset slj:36
ALTER TABLE share_report MODiFY file_id BIGINT UNSIGNED  default 0 comment '举报文件id,文件夹则该处为0';
ALTER TABLE share_report MODiFY `status`      TINYINT UNSIGNED default 0 comment '处理状态(0-未处理,1-已处理,2-允许分享,3-禁止分享)';

--changeset slj:37
ALTER TABLE common_info MODiFY `status` tinyint(3) unsigned NOT NULL COMMENT '状态 0草稿，1启用，2删除';

--changeset slj:38
ALTER TABLE share ADD COLUMN `status` TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 1 正常 2 取消分享 3 禁止分享' AFTER isLink;
ALTER TABLE io_source ADD COLUMN canShare TINYINT UNSIGNED DEFAULT 1 COMMENT '是否可以分享 1 正常 0 禁止分享' AFTER `sort`;
ALTER TABLE share_report MODIFY status TINYINT UNSIGNED DEFAULT 0 COMMENT '处理状态(0-未处理,1-已处理)';

--changeset slj:39
alter table share_report modify reason VARCHAR(255) DEFAULT null comment '举报原因（其他）描述';

--changeset slj:40
INSERT INTO `system_option` (`type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
('',	'needUploadCheck',	'1',	unix_timestamp(now()),	unix_timestamp(now()));


--changeset slj:41
ALTER TABLE io_file ADD COLUMN `convertSize` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '转码文件占用空间大小';
ALTER TABLE io_file ADD COLUMN `thumbSize` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '缩略图占用空间';

--changeset slj:42
alter table share modify status TINYINT(3) UNSIGNED default 1 comment '状态 1 正常 3 禁止分享 4 取消分享';

--changeset slj:43
ALTER TABLE common_label ADD COLUMN `tagType` tinyint(3) unsigned DEFAULT 1 COMMENT '类型 1网盘标签，2资讯标签';
alter table user_fav modify `type` varchar(20) NOT NULL COMMENT 'source/path/info';

--changeset slj:44
update system_option SET `value`='1' where `key` = 'showFileMd5' and type = '';

--changeset slj:45

CREATE TABLE IF NOT EXISTS common_design (
	`common_design_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编辑器id',
	`title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
	`offset` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '偏移量，坐标{x,y}',
	`size` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '尺寸大小，长宽{length,height}',
	`pic` bigint UNSIGNED NOT NULL COMMENT '缩略图，文件id',
	`gmt_create` datetime NOT NULL COMMENT '创建时间',
	`gmt_modified` datetime NOT NULL COMMENT '修改时间',
	`detail` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '页面内容',
	`design_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编辑器类型类型，1主页，2二级页',
	`client_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端类型，1PC，2手机端，3APP，4小程序',
	`is_used` tinyint UNSIGNED NOT NULL COMMENT '标识是否当前使用中的设置，是或否',
	`url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端访问路径，网校域名',
	`file_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文件存放路径',
	`fk_common_design_id` bigint UNSIGNED NOT NULL COMMENT '编辑器id，用于二级页面',
	`foot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '底部',
	`head` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '头部',
	`setting` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '样式设置',
	`applet` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '小程序的配置信息',
	`source_design_id` bigint UNSIGNED NOT NULL DEFAULT '0' COMMENT '来源装扮id',
	`approval_state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '审核状态，0待审核，1通过，2不通过',
	`approval_detail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '审核备注',
	`gmt_approval` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
	`sort` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '排序字段',
	`state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '状态',
	`is_paste` tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否复制',
	`seo` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'seo. keyword, description的json',
	`mb_design_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联h5编辑器ID',
	PRIMARY KEY (`common_design_id`),
	KEY `common_design_title_Idx` (`title`),
	KEY `common_design_source_design_id_Idx` (`source_design_id`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '页面编辑器表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

--changeset slj:46
update system_option SET `value`='my,myFav,rootGroup,recentDoc,fileType,fileTag,shareLink,information' where `key` = 'treeOpen' and type = '';


--changeset slj:47
INSERT INTO `system_option` (`type`, `key`, `value`, `createTime`, `modifyTime`) VALUES
(	'System.mesWarning',	'ID-1',	'{"name": "msgWarning","status": 1,"id": 1,"enable": "0","warnType": "cpu,mem,du","useRatio": "80","useTime": "20","sendType": "","target": "","dingUrl": "","wechatUrl": "","createTime": 1686187149,"modifyTime": 1686187149}',	unix_timestamp(now()),	unix_timestamp(now()));


--changeset slj:48
INSERT INTO system_option (`type`, `key`, `value`, createTime, modifyTime)
values ('Storage', 'LOCAL','{"name":"local1","size":"1024","location":"/uploads","storageKey":"LOCAL"}',	unix_timestamp(now()),	unix_timestamp(now()));

UPDATE role SET auth = 'explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav,admin.index.dashboard,admin.index.setting,admin.index.loginLog,admin.index.log,admin.index.server,admin.role.list,admin.role.edit,admin.job.list,admin.job.edit,admin.member.list,admin.member.userEdit,admin.member.groupEdit,admin.auth.list,admin.auth.edit,admin.plugin.list,admin.plugin.edit,admin.storage.list,admin.storage.edit,admin.autoTask.list,admin.autoTask.edit,admin.index.information,explorer.informationView' where roleID = 1;
UPDATE role SET auth = 'explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav,admin.index.loginLog,admin.index.log,admin.member.list,admin.member.userEdit,admin.member.groupEdit,admin.auth.list,explorer.informationView' where roleID = 2;
UPDATE role SET auth = 'explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav,explorer.informationView' where roleID = 3;


--changeset slj:49
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
)ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '用户资讯表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

--changeset slj:50
ALTER TABLE user_common_info DROP INDEX userId_infoId_unique;
ALTER TABLE user_common_info ADD UNIQUE INDEX  infoId_userId_unique(info_id, user_id);
ALTER TABLE user_common_info ADD COLUMN ip VARCHAR(30) DEFAULT NULL COMMENT '用户ip' AFTER info_id;
ALTER TABLE user_common_info ADD UNIQUE INDEX  infoId_ip_unique(info_id, ip);
ALTER TABLE user_common_info MODIFY user_id BIGINT UNSIGNED DEFAULT NULL  comment '用户id';


--changeset slj:51
INSERT INTO common_schedule(common_schedule_id, schedule_name, gmt_modified, frequency, api_url)
VALUES ('cloud_msg_warning', '消息预警', '2023-06-19 15:34:59', 19, '');


--changeset slj:52
CREATE TABLE IF NOT EXISTS common_design_classify (
	`designClassifyID` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '类型id',
	`typeName` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型名称',
	`parentID` int unsigned NOT NULL COMMENT '父ID',
	`parentLevel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '父路径id; 例如:  ,2,5,10,',
	`createTime` bigint(20) unsigned NOT NULL COMMENT '创建时间',
	`modifyTime` bigint(20) unsigned NOT NULL COMMENT '最后修改时间',
	`status` tinyint(3) unsigned NOT NULL COMMENT '状态 0禁用，1启用',
	`sort` smallint NOT NULL DEFAULT '0' COMMENT '排序',
	`namePinyin` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拼音全称',
	`namePinyinSimple` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '拼音首字母',
	`createUser` bigint(20) unsigned NOT NULL COMMENT '创建者id',
	PRIMARY KEY (`designClassifyID`),
	KEY `type_name_Idx` (`typeName`),
	KEY `type_sequence_Idx` (`parentLevel`),
	KEY `sort_Idx` (`sort`)
) ENGINE = InnoDB  CHARSET = utf8mb4 COLLATE = utf8mb4_bin COMMENT '装扮目录分类表' /*T! SHARD_ROW_ID_BITS=5 PRE_SPLIT_REGIONS=5 */;

ALTER TABLE common_design ADD COLUMN `designClassifyID` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '装扮分类id';

--changeset slj:53
ALTER TABLE common_info ADD COLUMN `pathPre` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '路径前缀';
ALTER TABLE common_design ADD COLUMN `pathPre` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '路径前缀';

--changeset slj:54
ALTER TABLE io_source ADD COLUMN `pathPre` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '路径前缀';

--changeset slj:55
UPDATE common_info SET pathPre = '/uploads' ;
UPDATE common_design SET pathPre = '/uploads' ;
UPDATE io_source SET pathPre = '/uploads' ;

--changeset slj:56
ALTER TABLE io_source DROP pathPre;
ALTER TABLE io_source ADD COLUMN `storageID` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '磁盘ID';


--changeset slj:57
update system_option SET `value`='my,myFav,rootGroup,recentDoc,fileType,fileTag,shareLink,information,toolbox' where `key` = 'treeOpen' and type = '';

