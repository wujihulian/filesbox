package com.svnlan.user.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 三方登录用户初始化配置信息
 *
 * @author lingxu 2023/05/06 10:44
 */
@NoArgsConstructor
@Data
public class ThirdUserInitializeConfig {
    @JSONField(name = "thirdName")
    private String thirdName;
    @JSONField(name = "sizeMax")
    private Double sizeMax;
    @JSONField(name = "roleID")
    private Integer roleID;
    @JSONField(name = "groupInfo")
    private List<GroupInfoDTO> groupInfo;

    @NoArgsConstructor
    @Data
    public static class GroupInfoDTO {
        @JSONField(name = "groupID")
        private Long groupID;
        @JSONField(name = "authID")
        private Integer authID;
    }

}
