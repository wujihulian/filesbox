package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.SystemLogDto;
import com.svnlan.user.vo.OperateLogExportVO;
import com.svnlan.user.vo.SystemLogExportVO;
import com.svnlan.utils.PageResult;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/24 14:20
 */
public interface SystemLogService {

    PageResult getSystemLogList(SystemLogDto systemLogDto, LoginUser loginUser);

    /**
     * 获取今天用户的登录数
     */
    Long getLoginUserCountToday();

    List<Long> getLoginUserCountDaysBefore(int daysBefore);

    /**
     * 用户登陆设置统计
     */
    Object selectUserLoginDeviceStat(int daysBefore, Long userId);



    List<SystemLogExportVO> getSystemLogExportList(SystemLogDto systemLogDto);
    List<OperateLogExportVO> getOperateLogExportList(SystemLogDto systemLogDto);

}
