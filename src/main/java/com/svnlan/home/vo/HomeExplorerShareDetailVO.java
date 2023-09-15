package com.svnlan.home.vo;

import lombok.Data;

/**
 * @author KingMgg
 * @data 2023/2/8 13:53
 */
@Data
public class HomeExplorerShareDetailVO {
    /**
     * 自增id
     */
    Long sourceID;
    Integer isFolder;
    String name;
    String fileType;
    Long createTime;
    Long parentID;
    String parentLevel;

}
