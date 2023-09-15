package com.svnlan.home.vo;

import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/27 16:10
 */
@Data
public class ChangeSourceVo {

    private String name;
    private String fileType;
    private String path;
    private String filePath;
    private Integer isFolder;
    private Integer pathLength = 0;
    private Long size;
    private String hashMd5 ;
    public ChangeSourceVo(){}
    public ChangeSourceVo(String name, Integer isFolder, String filePath){
        if (name.lastIndexOf("/") >= 0) {
            this.name = name.substring(name.lastIndexOf("/") + 1, name.length());
        }else {
            this.name = name;
        }
        this.isFolder = isFolder;
        this.fileType = "";
        this.path = "";
        this.filePath = filePath;
        this.pathLength = ObjectUtils.isEmpty(filePath) ? 0 : filePath.length();
    }
    public ChangeSourceVo(String name, Integer isFolder, String fileType, String path, String filePath, Long size, String hashMd5){
        this.isFolder = isFolder;
        this.fileType = fileType;
        this.path = path;
        this.filePath = filePath;
        this.size = size;
        this.hashMd5 = hashMd5;
        this.pathLength = ObjectUtils.isEmpty(filePath) ? 0 : filePath.length();
        this.name = name;
    }
}
