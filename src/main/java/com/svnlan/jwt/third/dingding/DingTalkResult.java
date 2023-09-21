package com.svnlan.jwt.third.dingding;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 钉钉接口返回
 *
 * @author lingxu 2023/03/29 09:54
 */
@Data
public class DingTalkResult<T> {

    private T result;
    @JSONField(name = "errcode")
    private Integer errCode;
    @JSONField(name = "errmsg")
    private String errMsg;
    @JSONField(name = "request_id")
    private String requestId;
}
