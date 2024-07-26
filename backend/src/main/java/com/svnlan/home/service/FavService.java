package com.svnlan.home.service;

import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;

import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/15 13:30
 */
public interface FavService {

    boolean moveTop(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
    boolean moveBottom(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
    Map<String, Object> getPreviewEditInfo(CheckFileDTO getCloudPreviewDTO);
    Map<String, Object> getPreviewRefreshInfo(CheckFileDTO getCloudPreviewDTO);
    String getPreviewFileUrl(CheckFileDTO getCloudPreviewDTO);
}
