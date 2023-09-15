package com.svnlan.home.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/6 18:02
 */

@Data
public class HomeExplorerResult<E> {
    List<E> fileList;
    List<E> folderList;
    Map<String, Object> current;
    Long total;
    String listType;
    String listIconSize;
    /** 文件夹 排序字段*/
    private String listSortField;
    /** 文件夹 排序升序降序*/
    private String listSortOrder;

}
