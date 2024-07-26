package com.svnlan.manage.dao;

import com.svnlan.manage.domain.UserCommonInfo;

/**
 * dao
 *
 * @author lingxu 2023/06/13 11:32
 */
public interface UserCommonInfoDao {

    int updateViewCount(Long id, String ip, Long userId);

    //    @Update("UPDATE user_common_info SET is_like = #{isLike} WHERE id = #{id}")
    void updateLikeById(Long id, Integer isLike);

    int insert(UserCommonInfo userCommonInfoInsert);
}
