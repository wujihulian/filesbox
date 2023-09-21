package com.svnlan.home.service;

import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.vo.HomeExplorerResult;
import com.svnlan.jwt.domain.LoginUser;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/31 11:28
 */
public interface SourceHistoryService {

    HomeExplorerResult getSourceHistoryList(HomeExplorerDTO homeExplorerDTO);
    void setHistoryDetail(HomeExplorerDTO homeExplorerDTO);
    void deleteHistory(HomeExplorerDTO homeExplorerDTO);
    void setHistoryRevision(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser);
    List parentSourceList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser);
}
