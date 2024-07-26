package com.svnlan.user.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.PullDingService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.RandomUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  同步钉钉组织及用户
 */
@RestController
public class PullDingController {
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    PullDingService pullDingService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/api/disk/ding/pull", method = RequestMethod.POST)
    public Result pullDing(@RequestBody SystemOptionDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        String uuid = RandomUtil.getuuid();
        optionDTO.setTaskID(ObjectUtils.isEmpty(optionDTO.getTaskID()) ? uuid : optionDTO.getTaskID());
        String lockKeyTop = "pullAboutGroupDing";
        Boolean lockTop = stringRedisTemplate.opsForValue().setIfAbsent(lockKeyTop, "1");

        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("status", 0);
        resultMap.put("progress", 0);
        resultMap.put("taskID", optionDTO.getTaskID());
        String lockKey = "pullDingAction" +  "_" + optionDTO.getTaskID();
        try {
            if (lockTop != null && lockTop) {
                stringRedisTemplate.expire(lockKeyTop, 20, TimeUnit.MINUTES);
            } else {
                return new Result(false, CodeMessageEnum.explorerSuccess.getCode(), "已有同步操作中！");
            }
            Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1");
            if (lock != null && lock) {
                stringRedisTemplate.expire(lockKey, 500, TimeUnit.MILLISECONDS);
            } else {
                return new Result(false, CodeMessageEnum.explorerSuccess.getCode(), "操作频率太快！");
            }
            pullDingService.pullDing(prefix,loginUser, optionDTO,resultMap);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }finally {
            stringRedisTemplate.expire(lockKeyTop, 1, TimeUnit.MILLISECONDS);
            stringRedisTemplate.expire(lockKey, 1, TimeUnit.MILLISECONDS);
        }
        return new Result(CodeMessageEnum.success.getCode(), resultMap);
    }

    @RequestMapping(value = "/api/disk/ding/pull/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String pullDingTaskAction(HttpServletResponse response, SystemOptionDTO updateFileDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = pullDingService.taskAction(prefix,updateFileDTO, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " SvnlanRuntimeException error!");
            result = new Result(false, e.getErrorCode(), e.getMessage(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }

        return JsonUtils.beanToJson(result);
    }
}
