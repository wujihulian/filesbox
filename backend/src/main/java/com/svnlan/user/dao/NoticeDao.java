package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.domain.Notice;

import java.util.List;

/**
* @author lingxu
* @description 针对表【notice】的数据库操作Mapper
* @createDate 2023-05-16 15:11:07
* @Entity com.svnlan.user.domain.Notice
*/
public interface NoticeDao{

    List<JSONObject> querySimpleInfo( List<Long> ids);

//    @Select("SELECT MAX(sort) FROM notice")
    Long selectMaxOrder();

//    @Select("SELECT n.id, n.title, n.send_time sendTime, n.create_time createTime, nu.is_read isRead FROM notice n INNER JOIN notice_user nu ON nu.notice_id = n.id " +
//            "WHERE nu.user_id = #{userId} AND n.status = 1 ORDER BY n.create_time DESC LIMIT #{startIndex}, #{pageSize}")
    List<JSONObject> selectNoticeList(Long userId,int startIndex,Integer pageSize);

//    @Select("SELECT COUNT(n.id) FROM notice n INNER JOIN notice_user nu ON nu.notice_id = n.id " +
//            "WHERE nu.user_id = #{userId} AND n.status = 1")
    Long selectNoticeListTotal(Long userId);

//    @Select("SELECT COUNT(n.id) FROM notice n INNER JOIN notice_user nu ON nu.notice_id = n.id " +
//            "WHERE nu.user_id = #{userId} AND n.status = 1 AND nu.is_read = 0")
    Long selectNoticeUnreadListTotal(Long userId);

    Long save(Notice noticeInsert);

    void updateById(Notice noticeUpdate);
}




