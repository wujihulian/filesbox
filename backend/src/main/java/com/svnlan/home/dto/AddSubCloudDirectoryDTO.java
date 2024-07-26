package com.svnlan.home.dto;

import lombok.Data;

import java.util.List;


/**
 * @Author: sulijuan
 * @Description:
 */
@Data
public class AddSubCloudDirectoryDTO {

    private Long sourceID;
    private String name;
    private String oldName;
    private List<AddSubCloudDirectoryDTO> children;
    private Integer targetType;
    private Long targetID;
    private Long createUser;
    private Long modifyUser;
    private Long parentID;
    private Long fileID;
    private Integer isFolder;
    private String fileType;
    private String parentLevel;
    private Integer storageID;
    private String namePinyin;
    private String namePinyinSimple;
    private Integer isSafe;
}
