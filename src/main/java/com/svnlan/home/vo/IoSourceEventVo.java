package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/22 15:44
 */
@Data
public class IoSourceEventVo {

    private Long id;
    private Long sourceID;
    private Long sourceParent;
    private Long userID;
    private String type;
    private String desc;
    private Long createTime;

    private String name;
    private String avatar;
    private String nickname;
    private Integer sex;
    private Integer status;
    private String parentName;
    private Integer isFolder;
    private Long ppID;
    private Integer parentTargetType;
    private Integer isChildren;

}
