package com.svnlan.user.dao;

import com.svnlan.user.dto.ShareDTO;
import com.svnlan.user.vo.ShareVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingxu
 * @description 针对表【share】的数据库操作Mapper
 * @createDate 2023-04-04 14:52:31
 * @Entity com.svnlan.user.vo.ShareVo
 */
public interface AdminShareDao {

    List<ShareVo> shareList(ShareDTO dto, boolean needAll);

    Integer shareListCount(ShareDTO dto);

    int cancelShare(Long id);

    void cancelMultiShare(List<Long> ids);

    ShareVo getById(Long id);

    List<ShareVo> getByIds(ArrayList<Integer> idList);
}




