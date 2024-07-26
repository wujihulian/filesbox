package com.svnlan.jwt.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Objects;

/**
 * 自动登录 dto
 *
 */
@Data
public class AutoLoginDto {
    /**
     * 授权码
     */
    @NotNull
    @NotEmpty
    private String code;

    /**
     * 组织 id
     */
    @NotNull
    @NotEmpty
    private String corpId;

    public HashMap<String, Object> toHashMap(HashMap<String,Object> extraMap) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", code);
        hashMap.put("corpId", corpId);
        if (Objects.nonNull(extraMap) && extraMap.size() > 0) {
            hashMap.putAll(extraMap);
        }
        return hashMap;
    }

}
