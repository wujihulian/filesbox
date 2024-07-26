package com.svnlan.home.service;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.GetAttachmentDTO;
import com.svnlan.home.dto.SaveUploadDTO;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 9:07
 */
public interface UploadService {

    CommonSourceVO upload(UploadDTO uploadDTO, LoginUser loginUser, CommonSource commonSource);

    CommonSourceVO fileUpload(UploadDTO uploadDTO, LoginUser loginUser, CommonSource commonSource);

    Map<String, Object> checkFile(CheckFileDTO checkFileDTO, LoginUser loginUser);

    Map<String, Object> checkFileReplace(CheckFileDTO checkFileDTO, LoginUser loginUser);

    Map<String, Object> checkChunk(CheckFileDTO checkChunkDTO, LoginUser loginUser);

    String returnSource(CheckFileDTO checkChunkDTO, String passedFileName, HttpServletResponse response);

    Map<String, Object> getPreviewInfo(CheckFileDTO getCloudPreviewDTO);

    boolean updateFile(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);

    String getAttachment(HttpServletResponse response, GetAttachmentDTO getAttachmentDTO, String passedFileName, Map<String, Object> resultMap);

    CommonSource fileSave(SaveUploadDTO uploadDTO, LoginUser loginUser);

    Map<String, Object> getFileContent(CheckFileDTO getCloudPreviewDTO);

    Map<String, Object> getFileContentBlob(CheckFileDTO getCloudPreviewDTO);

    Map<String, Object> getFileContentByte(CheckFileDTO getCloudPreviewDTO);

    /**
     * 1. 上传前的操作
     */
    HomeExplorerVO beforeUpload(CommonSource commonSource, UploadDTO uploadDTO, LoginUser loginUser, BusTypeEnum busTypeEnum);

    /**
     * 2. 验证文件名及后缀
     */
    void validateUploadFileName(String fileName);

    /**
     * 3. 根据业务类型做处理
     */
    void doByBusType(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource, BusTypeEnum busTypeEnum, HomeExplorerVO disk);

    /**
     * 4. 资源相关信息写入数据库
     */
    Long recordSourceDataToDb(CommonSource commonSource, UploadDTO uploadDTO, BusTypeEnum busTypeEnum);

    /**
     * 5. 转码操作
     */
    void tryToConvertFile(CommonSourceVO commonSourceVO, CommonSource commonSource, String busType);
}
