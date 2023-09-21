package com.svnlan.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.manage.domain.UserCommonInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * dao
 *
 * @author lingxu 2023/06/13 11:32
 */
public interface UserCommonInfoDao extends BaseMapper<UserCommonInfo> {

    int updateViewCount(@Param("id") Long id, @Param("ip") String ip, @Param("userId") Long userId);

    @Update("UPDATE user_common_info SET is_like = #{isLike} WHERE id = #{id}")
    void updateLikeById(@Param("id") Long id, @Param("isLike") Integer isLike);
}
