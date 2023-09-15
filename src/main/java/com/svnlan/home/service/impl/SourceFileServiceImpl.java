package com.svnlan.home.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.AddCloudDirectoryDTO;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.service.SourceFileService;
import com.svnlan.home.utils.DirectoryUtil;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/3 9:41
 */
@Service
public class SourceFileServiceImpl implements SourceFileService {

    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    DirectoryUtil directoryUtil;
    @Resource
    SystemLogTool systemLogTool;

    @Override
    public List<AddSubCloudDirectoryDTO> addBatchDirectory(AddCloudDirectoryDTO addCloudDirectoryDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(addCloudDirectoryDTO) || CollectionUtils.isEmpty(addCloudDirectoryDTO.getChildren())
                || ObjectUtils.isEmpty(addCloudDirectoryDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        for (AddSubCloudDirectoryDTO dto : addCloudDirectoryDTO.getChildren()){
            if (ObjectUtils.isEmpty(dto) || ObjectUtils.isEmpty(dto.getName())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
        }

        CommonSource commonSource = ioSourceDao.getSourceInfo(addCloudDirectoryDTO.getSourceID());
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }

        List<AddSubCloudDirectoryDTO> list = null;
        try {
            /************批量添加文件夹**********************************/
            list = directoryUtil.addBatchDirectory(addCloudDirectoryDTO.getChildren(), loginUser, commonSource, systemLogTool.getRequest());

        } catch (SvnlanRuntimeException e){
            throw new SvnlanRuntimeException(e.getErrorCode(), e.getMessage());
        } catch (Exception e){
            LogUtil.error(e, "目录添加失败" + JsonUtils.beanToJson(addCloudDirectoryDTO) + "," + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;
    }

    @Override
    public List getImgList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        Map<String, Object> paramMap = new HashMap<>(2);
        List fileTypeList = Arrays.asList("jpg","png","jpeg","JPG","PNG","JPEG");
        paramMap.put("fileTypeList", fileTypeList);
        paramMap.put("parentID", homeExplorerDTO.getSourceID());
        List<HomeExplorerVO> list =  homeExplorerDao.getImgByFolderList(paramMap);
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList();
        }
        for (HomeExplorerVO explorerVO : list){
            explorerVO.setPath(FileUtil.getShowImageUrl(explorerVO.getPath(),  explorerVO.getSourceID()+ "_" + explorerVO.getFileType() +".jpg"));
        }
        return list;
    }
}
