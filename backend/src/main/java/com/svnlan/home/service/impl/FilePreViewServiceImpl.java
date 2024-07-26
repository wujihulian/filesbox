package com.svnlan.home.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.FilePreViewService;
import com.svnlan.home.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/26 16:42
 */
@Service
public class FilePreViewServiceImpl implements FilePreViewService {
    @Resource
    IoFileDao ioFileDao;

    @Override
    public String getPreviewXml(CheckFileDTO checkFileDTO){
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceID()) || checkFileDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long id = ObjectUtils.isEmpty(checkFileDTO.getF()) ? 0L : checkFileDTO.getF();
        CommonSource commonSource = null;
        if (!ObjectUtils.isEmpty(id) && id > 0){
            commonSource = ioFileDao.getHistoryFileAttachment(id);
        }else {
            commonSource = ioFileDao.getFileAttachment(checkFileDTO.getSourceID());
        }
        if (!ObjectUtils.isEmpty(commonSource) && !ObjectUtils.isEmpty(commonSource.getPath())){

            return FileUtil.getFileContent(commonSource.getPath(), StandardCharsets.UTF_8);
        }
        return "";
    }
}
