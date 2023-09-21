package com.svnlan.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.CreateGroup;
import com.svnlan.annotation.UpdateGroup;
import com.svnlan.common.Result;
import com.svnlan.user.dto.StorageDTO;
import com.svnlan.user.dto.StorageDTO.StorageEnum;
import com.svnlan.user.service.StorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 存储管理
 *
 * @author lingxu 2023/06/07 16:48
 */
@RestController
@RequestMapping("api/storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    /**
     * 列表查询
     */
    @GetMapping("list")
    public Result list() {
        return Result.returnSuccess(storageService.list());
    }

    /**
     * 查询详细信息
     */
    @GetMapping("detail")
    public Result detail(@RequestParam("id") Integer id) {
        return Result.returnSuccess(storageService.getById(id));
    }

    /**
     * 编辑
     */
    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) StorageDTO dto) {
        dto.checkStorageKeyEnum();
        storageService.createOrEdit(dto, false);
        return Result.returnSuccess();
    }

    /**
     * 新增
     */
    @PostMapping("save")
    public Result save(@RequestBody @Validated(CreateGroup.class) StorageDTO dto) {
        dto.checkStorageKeyEnum();
        storageService.createOrEdit(dto, false);
        return Result.returnSuccess();
    }

    /**
     * 设置默认与否
     */
    @PostMapping("set/default")
    public Result setDefault(@RequestBody JSONObject jsonObj) {
        Integer id = Optional.ofNullable(jsonObj.getInteger("id")).orElseThrow(() -> new IllegalArgumentException("id 不能为空"));
        // 该方法 isDefault 必须为1 也即设置为默认
        Integer isDefault = 1;
        StorageDTO dto = new StorageDTO();
        dto.setId(id);
        dto.setIsDefault(isDefault);
        storageService.createOrEdit(dto, true);
        return Result.returnSuccess();
    }

    /**
     * 存储类型列表
     */
    @GetMapping("type/list")
    public Result getStorageTypeList() {
        List<String> list = Arrays.stream(StorageEnum.values()).map(Enum::name).collect(Collectors.toList());
        return Result.returnSuccess(list);
    }

    /**
     * 卸载存储
     */
    @GetMapping("unload")
    public Result unloadStorage(@RequestParam("id") Integer id) {
        storageService.unloadStorage(id);
        return Result.returnSuccess();
    }
}
