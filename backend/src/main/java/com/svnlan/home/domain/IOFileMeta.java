package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 16:21
 */
@Data
public class IOFileMeta {

    private Long id;
    private Long fileID;
    private String key;
    private String value;
    private Long createTime;
    private Long modifyTime;
    private Long tenantId;
}
