package com.svnlan.home.utils;

import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/25 13:30
 */
@Component
public class SourceHistoryUtil {

    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;

    public void changeCheckSourceHistory(IoSourceHistory newHistory , IoSourceHistory ioSourceHistory){
        IoSourceHistory org = ioSourceHistoryDao.getHistoryInfoByFileId(ioSourceHistory.getSourceID(), ioSourceHistory.getFileID());
        // 添加历史记录
        try {
            if (!ObjectUtils.isEmpty(org)){
                String detail = ObjectUtils.isEmpty(ioSourceHistory.getDetail()) ? "" : ioSourceHistory.getDetail();

                ioSourceHistoryDao.updateSize(org.getId(), ioSourceHistory.getSize(),detail);
            }else {
                ioSourceHistoryDao.insert(ioSourceHistory);
            }
        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 ioSourceHistory=" + JsonUtils.beanToJson(ioSourceHistory));
        }

        if (ObjectUtils.isEmpty(newHistory) || ObjectUtils.isEmpty(newHistory.getFileID())){
            return;
        }
        newHistory.setSize(ObjectUtils.isEmpty(newHistory.getSize()) ? 0L : newHistory.getSize());
        try {
            ioSourceHistoryDao.insert(newHistory.initializefield());
        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 newHistory=" + JsonUtils.beanToJson(ioSourceHistory));
        }
    }
}
