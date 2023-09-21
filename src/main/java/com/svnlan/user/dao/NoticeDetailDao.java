package com.svnlan.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.user.domain.NoticeDetail;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lingxu
 * @description 针对表【notice_detail】的数据库操作Mapper
 * @createDate 2023-05-16 15:14:04
 * @Entity com.svnlan.user.domain.NoticeDetail
 */
public interface NoticeDetailDao extends BaseMapper<NoticeDetail> {

    List<NoticeDetail> selectUnSyncList(@Param("startTime") LocalDateTime startQueryTime);
}




