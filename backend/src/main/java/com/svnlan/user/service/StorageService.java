package com.svnlan.user.service;

import com.svnlan.user.dto.StorageDTO;

import java.util.List;

/**
 * 存储管理
 *
 * @author lingxu 2023/06/08 13:33
 */
public interface StorageService {

    List<StorageDTO> list();

    StorageDTO getById(Integer id);

    void createOrEdit(StorageDTO storageDTO, boolean isDefaultSet);

    void unloadStorage(Integer id);

    String getDefaultStorageDevicePath();
    String getDefaultStorageDevicePath(Long opTypeId,int type);

    Integer getDefaultStorageDeviceId();

    String getStoragePathById(Integer id);

    Long getTotalSpace();
}
