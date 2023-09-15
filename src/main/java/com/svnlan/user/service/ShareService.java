package com.svnlan.user.service;

import com.github.pagehelper.PageInfo;
import com.svnlan.user.dto.ShareDTO;
import com.svnlan.user.vo.ShareVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享相关
 *
 * @author lingxu 2023/04/04 14:36
 */
public interface ShareService {


    PageInfo<ShareVo> shareListPage(ShareDTO dto);

    List<ShareVo> shareList(ShareDTO dto);
    void cancelShare(Long id);

    void cancelShare(ArrayList<Integer> idList);
}
