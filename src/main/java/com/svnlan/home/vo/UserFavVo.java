package com.svnlan.home.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/2 14:03
 */
@Data
public class UserFavVo {

    private Long id;
    /** */
    private Long userID;
    /** 标签id,收藏则为0 */
    private Integer tagID;
    /** 收藏名称 */
    private String name;
    /** 收藏路径,tag时则为sourceID */
    private String path;
    private String files;
    private String tags;
    private String style;
    private String tagName;
    private List<CommonLabelVo> favList;
}
