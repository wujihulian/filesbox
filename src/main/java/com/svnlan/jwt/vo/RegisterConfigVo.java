package com.svnlan.jwt.vo;

import com.svnlan.user.vo.UserGroupVo;
import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/6 10:25
 */
@Data
public class RegisterConfigVo {

    private Boolean enableRegister;
    private Boolean needReview;
    private Double sizeMax;
    private Integer roleID;
    private List<UserGroupVo> groupInfo;
}
