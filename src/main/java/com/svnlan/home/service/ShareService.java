package com.svnlan.home.service;

import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.ShareDTO;
import com.svnlan.home.vo.HomeExplorerResult;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.vo.SelectAuthVo;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/3 13:33
 */
public interface ShareService {

    void saveShare(ShareDTO shareDTO, LoginUser loginUser);
    void cancelShare(ShareDTO shareDTO, LoginUser loginUser);
    ShareVo getShare(ShareDTO shareDTO, LoginUser loginUser);
    HomeExplorerResult getShareList(HomeExplorerDTO shareDTO, LoginUser loginUser);
    HomeExplorerResult shareToMeList(HomeExplorerDTO shareDTO, LoginUser loginUser);
    ShareVo getShareFile(ShareDTO shareDTO, LoginUser loginUser);
    HomeExplorerResult getLinkShareList(HomeExplorerDTO shareDTO);
    SelectAuthVo userGroupList(HomeExplorerDTO shareDTO, LoginUser loginUser);
    Map<String, Object> previewNum(ShareDTO shareDTO);
    PageResult getUserList(LoginUser loginUser, UserDTO shareDTO);
}
