package com.svnlan.home.service;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.HomeSettingDTO;
import com.svnlan.home.vo.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/6 11:28
 */
public interface HomeExplorerService {

    /**
     * 首页的左侧分类列表  传递parentLevel 这个是子分类
     *
     * @return
     */
    HomeExplorerResult homeExplorer(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser);

    /**
     * 创建文件夹
     *
     * @return
     */
    IOSource createDir(HomeExplorerVO homeExplorerVO, LoginUser loginUser );


    /**
     * 获取文件详情
     *
     * @return
     */
    HomeFileDetailVO getFileDetail(HomeExplorerVO homeExplorerVO);
    Boolean folderSetting(HomeSettingDTO homeExplorerVO, LoginUser loginUser );
    PageResult getPathLog(HomeExplorerDTO homeExplorerVO);

    Map<String, Object> systemOpenInfo();

    HomeExplorerVO getHomeExplorer(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser);
    HomeExplorerVO execImgVideoThumb(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser);
}
