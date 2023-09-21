package com.svnlan.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.user.domain.NoticeUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
* @author lingxu
* @description 针对表【notice_user】的数据库操作Mapper
* @createDate 2023-05-16 15:20:58
* @Entity com.svnlan.user.domain.NoticeUser
*/
public interface NoticeUserDao extends BaseMapper<NoticeUser> {
    @Select("SELECT MAX(create_time) FROM notice_user WHERE user_id = #{userId}")
    LocalDateTime getMaxCreateTime(@Param("userId") Long userId);
}




