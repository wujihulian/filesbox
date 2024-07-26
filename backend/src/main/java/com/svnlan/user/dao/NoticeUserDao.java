package com.svnlan.user.dao;

import com.svnlan.user.domain.NoticeUser;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author lingxu
* @description 针对表【notice_user】的数据库操作Mapper
* @createDate 2023-05-16 15:20:58
* @Entity com.svnlan.user.domain.NoticeUser
*/
public interface NoticeUserDao {
//    @Select("SELECT MAX(create_time) FROM notice_user WHERE user_id = #{userId}")
    LocalDateTime getMaxCreateTime(Long userId);

    int saveBatch(List<NoticeUser> needToInsertList);
}




