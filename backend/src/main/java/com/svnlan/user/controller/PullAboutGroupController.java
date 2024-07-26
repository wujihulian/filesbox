package com.svnlan.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.DingCallbackCrypto;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.PullCubeService;
import com.svnlan.user.service.PullEnWebChatService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.CubeConfigVo;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class PullAboutGroupController {
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    PullEnWebChatService pullEnWebChatService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    PullCubeService pullCubeService;
    @Resource
    OptionTool optionTool;
    @Resource
    TenantUtil tenantUtil;

    @RequestMapping(value = "/api/disk/enwebchat/pull", method = RequestMethod.POST)
    public Result pullEnWebChat(@RequestBody SystemOptionDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        String uuid = RandomUtil.getuuid();
        optionDTO.setTaskID(ObjectUtils.isEmpty(optionDTO.getTaskID()) ? uuid : optionDTO.getTaskID());
        String lockKeyTop = "pullAboutGroupEnWebChat";
        Boolean lockTop = stringRedisTemplate.opsForValue().setIfAbsent(lockKeyTop, "1");

        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("status", 0);
        resultMap.put("progress", 0);
        resultMap.put("taskID", optionDTO.getTaskID());
        String lockKey = "pullAboutGroup_" + optionDTO.getTaskID();
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
                return new Result(false, CodeMessageEnum.explorerError.getCode(), "操作频率太快！");
            }
            pullEnWebChatService.pullAboutInfo(prefix,loginUser, optionDTO,resultMap);
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

    @RequestMapping(value = "/api/disk/enwebchat/pull/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String pullEnWebChatTaskAction(SystemOptionDTO updateFileDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = pullEnWebChatService.taskAction(prefix,updateFileDTO, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " enwebchat SvnlanRuntimeException error!");
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/cube/pull", method = RequestMethod.POST)
    public Result pullCube(@RequestBody SystemOptionDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        String uuid = RandomUtil.getuuid();
        optionDTO.setTaskID(ObjectUtils.isEmpty(optionDTO.getTaskID()) ? uuid : optionDTO.getTaskID());
        String lockKeyTop = "pullAboutGroupCube";
        Boolean lockTop = stringRedisTemplate.opsForValue().setIfAbsent(lockKeyTop, "1");

        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("status", 0);
        resultMap.put("progress", 0);
        resultMap.put("taskID", optionDTO.getTaskID());
        String lockKey = "pullAboutGroupCube_" + optionDTO.getTaskID();
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
                return new Result(false, CodeMessageEnum.explorerError.getCode(), "操作频率太快！");
            }
            pullCubeService.pullAboutInfo(prefix,loginUser, optionDTO,resultMap);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " cube Svnlan error!");
            return new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }finally {
            stringRedisTemplate.expire(lockKeyTop, 1, TimeUnit.MILLISECONDS);
            stringRedisTemplate.expire(lockKey, 1, TimeUnit.MILLISECONDS);
        }
        return new Result(CodeMessageEnum.success.getCode(), resultMap);
    }

    @RequestMapping(value = "/api/disk/cube/pull/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String pullCubeTaskAction(SystemOptionDTO updateFileDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = pullCubeService.taskAction(prefix,updateFileDTO, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " cube SvnlanRuntimeException error!");
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }


    @PostMapping(value = "/api/disk/cube/callback")
    public Map<String, String> eventCallback(HttpServletRequest request,
                                             @RequestParam(value = "msg_signature", required = false) String msg_signature,
                                             @RequestParam(value = "timestamp", required = false) String timeStamp,
                                             @RequestParam(value = "nonce", required = false) String nonce,
                                             @RequestBody(required = false) JSONObject json) {
        try {
            CubeConfigVo cubeConfigVo = optionTool.getCubeConfig(tenantUtil.getTenantIdByServerName());
            if (ObjectUtils.isEmpty(cubeConfigVo) || ObjectUtils.isEmpty(cubeConfigVo.getAccessKey())
                    || ObjectUtils.isEmpty(cubeConfigVo.getToken()) || ObjectUtils.isEmpty(cubeConfigVo.getAesKey())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam);
            }

            DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(cubeConfigVo.getToken(), cubeConfigVo.getAesKey(), cubeConfigVo.getAccessKey());
            String encryptMsg = json.getString("encrypt");
            String decryptMsg = callbackCrypto.getDecryptMsg(msg_signature, timeStamp, nonce, encryptMsg);

            // 3. 反序列化回调事件json数据
            JSONObject eventJson = JSON.parseObject(decryptMsg);
            LogUtil.info("魔方回调 eventJson ==> " + eventJson);
            String eventType = eventJson.getString("EventType");
            LogUtil.info("魔方回调 发生了：" + eventType + "事件");
            // 4. 根据EventType分类处理
            if ("check_url".equals(eventType)) {
                // 测试回调url的正确性
                LogUtil.info("测试回调url的正确性");
            } else if ("user_add_org".equals(eventType)) {
                // 处理通讯录用户增加事件
            } else if ("sync_http_push_high".equalsIgnoreCase(eventType)) {
                // Ticket推送事件
                executeHttpPushEvent(eventJson.getJSONArray("bizData"));
            } else {
                // 添加其他已注册的
            }

            // 5. 返回success的加密数据
            Map<String, String> successMap = callbackCrypto.getEncryptedMap("success");
            LogUtil.info(successMap.toString());
            return successMap;
        } catch (DingCallbackCrypto.DingTalkEncryptException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void executeHttpPushEvent(JSONArray jsonArray) {
        if (CollectionUtils.isEmpty(jsonArray)) {
            return;
        }
        String suiteTicket = null; //stringRedisTemplate.opsForValue().get(GlobalConfig.SUITE_TICKET_KEY);
        if (!ObjectUtils.isEmpty(suiteTicket)){
            return;
        }
        String corpId = jsonArray.getJSONObject(0).getString("corp_id");
        String bizDataStr = jsonArray.getJSONObject(0).getString("biz_data");
        JSONObject bizData = JSON.parseObject(bizDataStr);
        // 提取出 suiteTicket
        suiteTicket = bizData.getString("suiteTicket");
        if (StringUtils.hasText(corpId)) {
            // 存入 redis
            stringRedisTemplate.opsForValue().set(GlobalConfig.CUBE_SUITE_TICKET_CORP_ID, corpId, 24, TimeUnit.HOURS);
        }
        if (StringUtils.hasText(suiteTicket)) {
            // 存入 redis
            stringRedisTemplate.opsForValue().set(GlobalConfig.SUITE_TICKET_KEY, suiteTicket, 5, TimeUnit.HOURS);
        }
    }

}
