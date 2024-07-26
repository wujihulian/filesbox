package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.PluginDto;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.vo.PluginListVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 15:07
 */
public interface SystemSettingService {

    Map<String, Object> getSystemSetting(LoginUser loginUser);

    void setSystemSetting(String prefix, SystemOptionDTO optionDTO, LoginUser loginUser);

    void setPlugin(String prefix, PluginDto optionDTO, LoginUser loginUser);
    void setMsgWarning(String prefix, PluginDto optionDTO, LoginUser loginUser);

    List pluginList(PluginDto optionDTO);
    PluginListVo getMsgWarningSetting(Long tenantId);
    Map<String, Object> getSystemInfo();
}
