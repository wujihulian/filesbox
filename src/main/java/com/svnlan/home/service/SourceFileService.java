package com.svnlan.home.service;

import com.svnlan.home.dto.AddCloudDirectoryDTO;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.jwt.domain.LoginUser;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/3 9:40
 */
public interface SourceFileService {

    List<AddSubCloudDirectoryDTO> addBatchDirectory(AddCloudDirectoryDTO addCommonPicClassifyDTO, LoginUser loginUser);


    List getImgList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser);
}
