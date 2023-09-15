package com.svnlan.user.vo;

import com.svnlan.user.domain.Group;
import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/13 14:40
 */
@Data
public class SelectAuthVo {

    private List<UserGroupVo> groupList;
    private List<UserVo> userList;
}
