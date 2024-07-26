package com.svnlan.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 魔方登录返回
 *
 */
@NoArgsConstructor
@Data
public class CubeLogin {
    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("cubeUserId")
    private String cubeUserId;
    @JsonProperty("cubeCommonUserId")
    private String cubeCommonUserId;
    @JsonProperty("cubeCorpId")
    private String cubeCorpId;
    @JsonProperty("cubeCorpName")
    private String cubeCorpName;
    @JsonProperty("unionid")
    private String unionid;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("name")
    private String name;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("corpId")
    private String corpId;
    @JsonProperty("corpName")
    private String corpName;
    @JsonProperty("roleStatus")
    private Object roleStatus;
    @JsonProperty("sessionSource")
    private String sessionSource;
    @JsonProperty("isStudent")
    private Boolean isStudent;
    @JsonProperty("platform")
    private String platform;
    @JsonProperty("outUserId")
    private String outUserId;
    @JsonProperty("appId")
    private String appId;
    @JsonProperty("developerRegisterDTO")
    private Object developerRegisterDTO;
    @JsonProperty("developerCorpInstallAuthored")
    private Object developerCorpInstallAuthored;
    @JsonProperty("corpChooseRight")
    private Object corpChooseRight;
    @JsonProperty("userOneId")
    private Object userOneId;
    @JsonProperty("orgId")
    private String orgId;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("authCode")
    private Object authCode;
    private String dingUserId;

}
