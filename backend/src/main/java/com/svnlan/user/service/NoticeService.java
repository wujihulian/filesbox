package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.domain.Notice;
import com.svnlan.user.dto.NoticeDTO;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @author lingxu
 * @description 针对表【notice】的数据库操作Service
 * @createDate 2023-05-16 15:11:07
 */
public interface NoticeService {
    String NOTICE_DELETE_KEY = "notice_delete_key";

    void addNotice(NoticeDTO dto);

    void editNotice(NoticeDTO dto);

    JSONObject detail(Long id);

    List<JSONObject> preview(List<Long> ids);

    void operateEnable(Long id);

    void delete(Long id);

    Long hasNoticeUnread();

    JSONObject pageList(Integer currentPage, Integer pageSize);

    void executeNoticeRead(Long id);

    Pair<List<Notice>, Long> listPage(Integer currentPage, Integer pageSize);
}
