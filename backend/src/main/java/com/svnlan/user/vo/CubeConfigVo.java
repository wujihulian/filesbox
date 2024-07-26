package com.svnlan.user.vo;

import lombok.Data;

@Data
public class CubeConfigVo {
    private String accessKey;
    private String secretKey;
    private String cubeOrgId;
    private String dingCorpId;
    private String token;
    private String aesKey;
}
