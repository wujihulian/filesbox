package com.svnlan.user.dto;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.CreateGroup;
import com.svnlan.annotation.UpdateGroup;
import io.jsonwebtoken.lang.Assert;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 存储管理 dto
 *
 * @author lingxu 2023/06/07 17:09
 */
@Data
public class StorageDTO {

    @NotNull(groups = UpdateGroup.class)
    private Integer id;

    private String storageKey;
    /**
     * 名称
     */
    @NotEmpty(groups = CreateGroup.class)
    private String name;
    /**
     * 空间大小
     */
    @NotNull(groups = CreateGroup.class)
    @Min(value = 1L,message = "空间大小最小为1")
    private Integer size;
    /**
     * 存储目录
     */
    @NotEmpty(groups = CreateGroup.class)
    private String location;
    /**
     * 是否默认 1 是  0 否
     */
    private Integer isDefault;
    /**
     * 额外的信息
     */
    private JSONObject extra;
    /**
     * 已占用的大小
     */
    private Long usage;


    public enum StorageEnum {
        LOCAL, ALI_OSS, TENCENT_COS, WEBDAV
    }

    public void checkStorageKeyEnum() {
        Assert.hasText(storageKey, "storageKey 不能为空");
        StorageEnum storageEnum = StorageEnum.valueOf(storageKey);
        Assert.notNull(storageEnum, "storageKey 不合法");
    }
}
