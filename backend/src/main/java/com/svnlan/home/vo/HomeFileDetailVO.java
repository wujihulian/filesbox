package com.svnlan.home.vo;

import lombok.Data;

/**
 * @author KingMgg
 * @data 2023/2/7 10:30
 */
@Data
public class HomeFileDetailVO {
    Long sourceID;
    Long fileID;
    /**
     * 文件名
     */
    String name;
    /**
     * 文件大小
     */
    Long size;
    /**
     * io的id
     */
    Integer ioType;
    /**
     * 文件路径
     */

    String path;
    /**
     * 文件简易hash(不全覆盖)；hashSimple
     */
    String hashSimple;
    /**
     * 文件hash, md5
     */

    String hashMd5;
    /**
     * 引用次数;0则定期删除
     */

    Long linkCount;
    /**
     * 修改时间
     */

    Long createTime;
    /**
     * 最后修改时间
     */
    Long modifyTime;
}
