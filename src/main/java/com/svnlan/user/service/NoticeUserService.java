package com.svnlan.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.svnlan.user.domain.NoticeUser;
import com.svnlan.user.dto.NoticeDTO;

import java.time.LocalDateTime;

/**
 * @author lingxu
 * @description 针对表【notice_user】的数据库操作Service
 * @createDate 2023-05-16 15:20:58
 */
public interface NoticeUserService extends IService<NoticeUser> {

    /**
     * 建立一条通知与多个在线用户的关联关系
     *
     * @param id    通知id
     * @param now   当前时间
     * @param dto
     * @param isAll
     */
    void buildNoticeAndUserRelation(Long id, LocalDateTime now, NoticeDTO dto, boolean isAll);

    /**
     * 建立登录用户与多个通知的关联关系
     *
     * @param userId 用户id
     * @param needCheckUnique 是否需要校验唯一性
     */
    void buildRelationAfterLogin(Long userId, boolean needCheckUnique);
}
