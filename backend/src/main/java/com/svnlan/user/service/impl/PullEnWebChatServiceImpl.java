package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncDingFileUtil;
import com.svnlan.home.utils.AsyncEnWebChatFileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.EnWechatService;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.PullEnWebChatService;
import com.svnlan.user.tools.AsyncPullingUtil;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.user.vo.EnWebChatConfigVo;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.TenantUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PullEnWebChatServiceImpl implements PullEnWebChatService {
    @Resource
    AsyncPullingUtil asyncPullingUtil;
    @Resource
    AsyncEnWebChatFileUtil asyncEnWebChatFileUtil;
    @Resource
    GroupDao groupDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    OptionTool optionTool;
    @Resource
    EnWechatService enWechatService;
    @Resource
    LoginUserUtil loginUserUtil;


    @Override
    public void pullAboutInfo(String prefix, LoginUser loginUser, SystemOptionDTO optionDTO, Map<String, Object> resultMap){

        Long tenantId = loginUser.getTenantId();
        EnWebChatConfigVo dingConfigVo = optionTool.getEnWebChatConfig(loginUser.getTenantId());
        if (ObjectUtils.isEmpty(dingConfigVo) || ObjectUtils.isEmpty(dingConfigVo.getCorpId())
                || ObjectUtils.isEmpty(dingConfigVo.getSecret())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam);
        }
        String accessToken = enWechatService.getAccessToken(tenantId);
        List<JSONObject> list =  asyncEnWebChatFileUtil.getDepartmentList(null, accessToken);
        if (CollectionUtils.isEmpty(list)){
            // 空的组织结构
            resultMap.put("status", 2);
            resultMap.put("progress", 100);
            return;
        }
        String taskId = optionDTO.getTaskID();
        LogUtil.info(prefix + " pullEnWebChat=" + list);


        HttpServletRequest request = systemLogTool.getRequest();
        loginUserUtil.setLoginUserAboutUaInfo(request, loginUser);

        // 获取组织下的所有已经同步的id
        List<Long> deptList = groupDao.getDingDeptIds(loginUser.getTenantId(), "enWechat");


        /** 操作日志 */
        systemLogTool.setSysLog(loginUser, LogTypeEnum.pullEnWebchat.getCode(), null, systemLogTool.getRequest(), taskId);

        // 异步拉取
        asyncPullingUtil.asyncPullEnWebChatGroup(deptList, list, taskId, tenantId, dingConfigVo, accessToken, loginUser);

        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_pull_enwebchat + taskId, "0", 1, TimeUnit.HOURS);

    }

    @Override
    public boolean taskAction(String prefix, SystemOptionDTO optionDTO, Map<String, Object> resultMap, LoginUser loginUser){

        String taskId = optionDTO.getTaskID();
        resultMap.put("taskID", taskId);
        if (ObjectUtils.isEmpty(taskId)){
            resultMap.put("status", 0);
            resultMap.put("progress", 0);
            return false;
        }
        resultMap.put("status", 0);
        String key = GlobalConfig.async_key_pull_enwebchat + taskId;
        String denyString = stringRedisTemplate.opsForValue().get(key);

        // 进度
        String progressKey = GlobalConfig.progress_key_pull_enwebchat +  taskId;
        String progressString = stringRedisTemplate.opsForValue().get(progressKey);
        resultMap.put("progress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            resultMap.put("progress", progressString);
        }

        LogUtil.info(prefix + " taskId=" + taskId + " taskAction key=" + key +"，denyString=" + denyString + "，progressString=" + progressString);
        if (ObjectUtils.isEmpty(denyString)){
            resultMap.put("status", 1);
            resultMap.put("progress", 100);
            return true;
        }
        if ("1".equals(denyString)) {
            resultMap.put("status", 1);
            resultMap.put("progress", 100);
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(progressKey);
        }
        return true;
    }

}
