package com.svnlan.user.vo;

import com.svnlan.home.vo.LogDescVo;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/24 14:32
 */
@Data
public class SystemLogVo {

    private Long id;
    private String sessionID;
    private Long userID;
    private String type;

    private String desc;
    private Long createTime;
    /** '日期'*/
    private String visitDate;
    /** 1pc , 2h5, 3安卓app, 4 ios-app, 5小程序*/
    private String clientType;
    /** '操作系统'*/
    private String osName;
    private String name;
    private String avatar;
    private String nickname;
    private LogDescVo logDescVo;
    private Long parentID;
    private String parentName;
}
