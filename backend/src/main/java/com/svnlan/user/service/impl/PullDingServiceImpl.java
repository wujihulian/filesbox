package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncDingFileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.PullDingService;
import com.svnlan.user.tools.AsyncPullingUtil;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PullDingServiceImpl implements PullDingService {
    @Resource
    AsyncPullingUtil asyncPullingUtil;
    @Resource
    AsyncDingFileUtil asyncDingFileUtil;
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
    LoginUserUtil loginUserUtil;
    @Value("${enterprise.type}")
    String enterpriseType;


    @Override
    public void pullDing(String prefix, LoginUser loginUser, SystemOptionDTO optionDTO, Map<String, Object> resultMap){
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(loginUser.getTenantId());
        if (ObjectUtils.isEmpty(dingConfigVo) || ObjectUtils.isEmpty(dingConfigVo.getAppKey())
                || ObjectUtils.isEmpty(dingConfigVo.getAppSecret())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam);
        }
        List<JSONObject> list = null;
        Long tenantId = loginUser.getTenantId();
        //if (!ObjectUtils.isEmpty(enterpriseType) && "2".equals(enterpriseType)){
            // 家校通
           // list = asyncDingFileUtil.getSchoolDepartmentForeachList(null, tenantId, 1L, 30L);
         //}else {
            // 企业类型
            list =  asyncDingFileUtil.getDepartmentList(null, tenantId);
        //}

        if (ObjectUtils.isEmpty(list)){
            // 空的组织结构
            resultMap.put("status", 2);
            resultMap.put("progress", 100);
            return;
        }
        String taskId = optionDTO.getTaskID();
        LogUtil.info(prefix + " pullDingEnterprise=" + list);
        // 获取组织下的所有已经同步的id
        List<Long> deptList = groupDao.getDingDeptIds(loginUser.getTenantId(), "ding");

        HttpServletRequest request = systemLogTool.getRequest();
        loginUserUtil.setLoginUserAboutUaInfo(request, loginUser);

        /** 操作日志 */
        systemLogTool.setSysLog(loginUser, LogTypeEnum.pullDing.getCode(), null, null, taskId);
        // 异步拉取
        asyncPullingUtil.asyncPullDingGroup(deptList, list, taskId, tenantId, enterpriseType, loginUser );

        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_pull_ding + taskId, "0", 1, TimeUnit.HOURS);

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
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String error = valueOperations.get(GlobalConfig.pull_error_key + taskId);
        if (!ObjectUtils.isEmpty(error)){
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), error, null);
        }
        resultMap.put("status", 0);
        String key = GlobalConfig.async_key_pull_ding + taskId;
        String denyString = valueOperations.get(key);

        // 进度
        String progressKey = GlobalConfig.progress_key_pull_ding +  taskId;
        String progressString = valueOperations.get(progressKey);
        resultMap.put("progress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            resultMap.put("progress", progressString);
        }

        LogUtil.info(prefix + "taskAction key=" + key +"，denyString=" + denyString + "，progressString=" + progressString);
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
