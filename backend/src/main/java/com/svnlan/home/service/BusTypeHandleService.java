package com.svnlan.home.service;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.UploadDTO;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 10:15
 */
public interface BusTypeHandleService {

    void doForImage(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource);
    void doForCloud(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource);
    void doForExtraImage(UploadDTO uploadDTO, CommonSource commonSource);
    void checkForCloud(CheckFileDTO checkFileDTO, CommonSource commonSource);

    void doForWareAndAttachment(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource);
}
