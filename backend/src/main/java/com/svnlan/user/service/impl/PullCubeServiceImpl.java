package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncCubeFileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.EnWechatService;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.PullCubeService;
import com.svnlan.user.tools.AsyncPullingUtil;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.CubeConfigVo;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.utils.JsonUtils;
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
public class PullCubeServiceImpl implements PullCubeService {
    @Resource
    AsyncPullingUtil asyncPullingUtil;
    @Resource
    AsyncCubeFileUtil cubeFileUtil;
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

    @Override
    public void pullAboutInfo(String prefix, LoginUser loginUser, SystemOptionDTO optionDTO, Map<String, Object> resultMap){

        Long tenantId = loginUser.getTenantId();
        CubeConfigVo cubeConfigVo = optionTool.getCubeConfig(tenantId);
        if (ObjectUtils.isEmpty(cubeConfigVo) || ObjectUtils.isEmpty(cubeConfigVo.getAccessKey())
                || ObjectUtils.isEmpty(cubeConfigVo.getSecretKey())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam);
        }
        if (ObjectUtils.isEmpty(cubeConfigVo.getCubeOrgId())
                && ObjectUtils.isEmpty(cubeConfigVo.getDingCorpId())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode(), "魔方组织Id与钉钉组织id需填写其中一个", null);
        }

        if (ObjectUtils.isEmpty(cubeConfigVo.getCubeOrgId())){
            try {
                // 根据钉钉组织id获取
                JSONObject cubeOrg = cubeFileUtil.getCubeOrgByCorpIdApi(cubeConfigVo.getAccessKey(), cubeConfigVo.getSecretKey(), cubeConfigVo.getDingCorpId());
                cubeConfigVo.setCubeOrgId(cubeOrg.getString("cubeOrgId"));
            }catch (Exception e){
                LogUtil.error(e,"根据钉钉corpId获取魔方组织id失败 error");
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam);
            }
        }

        if (ObjectUtils.isEmpty(cubeConfigVo.getCubeOrgId())){
            LogUtil.error("根据钉钉corpId获取魔方组织id失败 null");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam);
        }

        List<JSONObject> list = null;
        list = cubeFileUtil.queryImDeptListByParentId(cubeConfigVo.getAccessKey(), cubeConfigVo.getSecretKey()
                , cubeConfigVo.getCubeOrgId(), "1");

        if (CollectionUtils.isEmpty(list)){
            // 空的组织结构
            resultMap.put("status", 2);
            resultMap.put("progress", 100);
            return;
        }

        String taskId = optionDTO.getTaskID();
        HttpServletRequest request = systemLogTool.getRequest();
        loginUserUtil.setLoginUserAboutUaInfo(request, loginUser);

        // 获取组织下的所有已经同步的id
        List<String> deptList = groupDao.getThirdDeptIds(loginUser.getTenantId(), "cube");

        /** 操作日志 */
        // 异步拉取
        asyncPullingUtil.asyncPullCubeGroup(deptList, list, taskId, tenantId, loginUser, cubeConfigVo);

        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_pull_cube + taskId, "0", 1, TimeUnit.HOURS);

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
        String key = GlobalConfig.async_key_pull_cube + taskId;
        String denyString = stringRedisTemplate.opsForValue().get(key);

        // 进度
        String progressKey = GlobalConfig.progress_key_pull_cube +  taskId;
        String progressString = stringRedisTemplate.opsForValue().get(progressKey);
        resultMap.put("progress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            resultMap.put("progress", progressString);
        }

        LogUtil.info(prefix + " cube taskId=" + taskId + " taskAction key=" + key +"，denyString=" + denyString + "，progressString=" + progressString);
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
