package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/20 10:20
 */
@Data
public class UserFav {

    private Long id;
    /** */
    private Long userID;
    /** 标签id,收藏则为0 */
    private Integer tagID;
    /** 收藏名称 */
    private String name;
    /** 收藏路径,tag时则为sourceID */
    private String path;
    /** source/path */
    private String type;
    private Integer sort;
    private Long modifyTime;
    private Long createTime;

    public UserFav(){}
    public UserFav(Long userID, Integer tagID, String name, String path, String type, Integer sort){
        this.userID = userID;
        this.tagID = tagID;
        this.path = path;
        this.name = name;
        this.type = type;
        this.sort = sort;
    }


}
