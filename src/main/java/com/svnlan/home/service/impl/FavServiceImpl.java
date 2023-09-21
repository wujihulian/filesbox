package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.enums.CloudOperateEnum;
import com.svnlan.home.service.FavService;
import com.svnlan.home.utils.ConvertUtil;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.utils.HttpUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/15 13:30
 */
@Service
public class FavServiceImpl implements FavService {

    @Resource
    UserFavDao userFavDao;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    IoFileDao ioFileDao;

    @Override
    public boolean moveTop(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser) {

        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID()) || updateFileDTO.getSourceID() <=0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Integer sort = userFavDao.getFavMaxSort(loginUser.getUserID());
        sort = ObjectUtils.isEmpty(sort) ? 1 : sort;
        try {
            // 先减所有再修改当前sourceID
            userFavDao.subtractSortAll(loginUser.getUserID());
            userFavDao.updateFavSort(loginUser.getUserID(), updateFileDTO.getSourceID().toString(), sort);
        } catch (Exception e){
            LogUtil.error(e, "收藏夹内置顶失败" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public boolean moveBottom(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID()) || updateFileDTO.getSourceID() <=0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            // 先加所有再修改当前sourceID
            userFavDao.addSortAll(loginUser.getUserID());
            userFavDao.updateFavSort(loginUser.getUserID(), updateFileDTO.getSourceID().toString(), 0);
        } catch (Exception e){
            LogUtil.error(e, "收藏夹内置顶失败" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public Map<String, Object> getPreviewEditInfo(CheckFileDTO getCloudPreviewDTO){

        CommonSource cloudFile = fileOptionTool.getFileAttachment(getCloudPreviewDTO.getSourceID());
        if (cloudFile == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        Map<String, Object> reMap = new HashMap<>(1);
        reMap.put("yzEditData", "");

        LoginUser loginUser = getCloudPreviewDTO.getLoginUser();
        cloudFile.setUserID(loginUser.getUserID());
        // 永中
        boolean isYongZhong = Arrays.asList(GlobalConfig.yongzhong_doc_excel_ppt_type).contains(cloudFile.getFileType());
        if (isYongZhong){
            // 预览
            cloudFile.setDomain(HttpUtil.getRequestRootUrl(null));
            convertUtil.yongZhongPre(cloudFile, false);
            reMap.put("yzEditData", ObjectUtils.isEmpty(cloudFile.getYzEditData()) ? "" : cloudFile.getYzEditData());

        }

        return reMap;
    }
    @Override
    public Map<String, Object> getPreviewRefreshInfo(CheckFileDTO getCloudPreviewDTO){

        CommonSource cloudFile = fileOptionTool.getFileAttachment(getCloudPreviewDTO.getSourceID());
        if (cloudFile == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        Map<String, Object> reMap = new HashMap<>(1);
        reMap.put("yzViewData", "");
        // 永中
        boolean isYongZhong = Arrays.asList(GlobalConfig.yongzhong_doc_excel_ppt_type).contains(cloudFile.getFileType());
        if (isYongZhong){
            // 预览
            cloudFile.setDomain(HttpUtil.getRequestRootUrl(null));
            convertUtil.yongZhongPre(cloudFile, true);
            reMap.put("yzViewData", ObjectUtils.isEmpty(cloudFile.getYzViewData()) ? "" : cloudFile.getYzViewData());

        }

        return reMap;
    }

    @Override
    public String getPreviewFileUrl(CheckFileDTO getCloudPreviewDTO){
        if (ObjectUtils.isEmpty(getCloudPreviewDTO.getSourceID()) || getCloudPreviewDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long id = ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) ? 0L : getCloudPreviewDTO.getF();
        CommonSource commonSource = null;
        if (!ObjectUtils.isEmpty(id) && id > 0){
            commonSource = ioFileDao.getHistoryFileAttachment(id);
        }else {
            commonSource = ioFileDao.getFileAttachment(getCloudPreviewDTO.getSourceID());
        }
        if (!ObjectUtils.isEmpty(commonSource) && !ObjectUtils.isEmpty(commonSource.getPath())){
            String firstPath = FileUtil.getFirstStorageDevicePath(commonSource.getPath());
            return commonSource.getPath().replace(firstPath + "/private/cloud", firstPath + "/common/cloud");
        }
        return "";
    }
}
