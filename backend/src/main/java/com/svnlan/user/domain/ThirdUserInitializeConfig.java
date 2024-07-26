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
    @JSONField(name = "isSyncDing")
    private Integer isSyncDing;
    @JSONField(name = "dingInfo")
    private List<DingInfoDTO> dingInfo;


    @NoArgsConstructor
    @Data
    public static class GroupInfoDTO {
        @JSONField(name = "groupID")
        private Long groupID;
        @JSONField(name = "authID")
        private Integer authID;
        @JSONField(name = "name")
        private String name;
    }

    @NoArgsConstructor
    @Data
    public static class DingInfoDTO {
        // myDoc 我的文档、teamDoc 团队文档、groupFiles群文件
        @JSONField(name = "typeName")
        private String typeName;
        @JSONField(name = "parentId")
        private Long parentId;
        @JSONField(name = "sourceId")
        private Long sourceId;
        @JSONField(name = "path")
        private String path;

        public DingInfoDTO(String typeName,Long parentId,Long sourceId,String path){
            this.typeName = typeName;
            this.parentId = parentId;
            this.sourceId = sourceId;
            this.path = path;
        }
    }

}
