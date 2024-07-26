package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.user.dao.Impl.SystemOptionDaoImpl;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.CubeConfigVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class AsyncCubeFileUtil {
    @Resource
    OptionTool optionTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemOptionDaoImpl systemOptionDaoImpl;


    public CubeConfigVo getCubeConfig(Long tenantId){
        CubeConfigVo cubeConfigVo = optionTool.getCubeConfig(tenantId);
        if(cubeConfigVo == null){
            return null;
        }
        return cubeConfigVo;
    }
    /**
     * 获取配置信息
     */
    public ThirdUserInitializeConfig getInitializeConfig(SecurityTypeEnum thirdNameEnum) {
        // 查询魔方用户的配置
        String thirdLoginConfig = systemOptionDaoImpl.getSystemConfigByKey("thirdLoginConfig");
        if (!StringUtils.hasText(thirdLoginConfig)) {
            return null;
        }
        LogUtil.info("t魔方同步创建用户 hirdLoginConfig => {}", thirdLoginConfig);
        List<ThirdUserInitializeConfig> thirdUserInitializeConfigList = JSONObject.parseArray(thirdLoginConfig, ThirdUserInitializeConfig.class);
        // 找出钉钉的配置
        return thirdUserInitializeConfigList.stream()
                .filter(it -> thirdNameEnum.getValue().equals(it.getThirdName()))
                .findFirst()
                .orElseThrow(() -> new SvnlanRuntimeException("魔方同步创建用户 未查询到对应平台初始化配置 不能进行登录或相关操作"));
    }


    public JSONObject getCubeOrgByCorpIdApi(String ak, String sk, String dingCorpId){
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        try {
            jsonObj = CubeUtil.getCubeOrgByCorpIdApi(ak, sk, dingCorpId);
        }catch (Exception e){
            LogUtil.error(e, "获取当前钉钉组织信息绑定的魔⽅组织 ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("cube 获取当前钉钉组织信息绑定的魔⽅组织 " + jsonObj);
            String dataStr = jsonObj.getString("result");
            dataJsonObject =  JSONObject.parseObject(dataStr);
        } else {
            LogUtil.info("cube 数据错误 获取当前钉钉组织信息绑定的魔⽅组织 " + jsonObj);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return dataJsonObject;
    }

    public List<JSONObject> getSchoolDepartmentList(String ak, String sk, String cubeOrgId, String parentDeptId) {
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = CubeUtil.getDepartmentSchoolList(ak, sk, cubeOrgId, parentDeptId);
        }catch (Exception e){
            LogUtil.error(e, "getSchoolDepartmentList 获取部门列表失败 ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("cube school 获取部门列表getDepartmentList" + jsonObj);
            String dataStr = jsonObj.getString("result");
            JSONObject dataJsonObject =  JSONObject.parseObject(dataStr);
            String listStr = dataJsonObject.getString("children");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return list;
    }


    public JSONObject getDepartmentUserList(String cubeOrgId, String imDeptId,String ak, String sk, int index, int size) {
        JSONObject jsonObj = null;
        try {
            jsonObj = CubeUtil.getDepartmentUserList(cubeOrgId, imDeptId,ak, sk, index, size);
        }catch (Exception e){
            LogUtil.error(e, "魔⽅组织查询魔⽅组织⼈员列表（分⻚） imDeptId=" + imDeptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("魔⽅组织查询魔⽅组织⼈员列表（分⻚）  getDepartmentUserList" + jsonObj);
            String listStr = jsonObj.getString("result");
            return JSONObject.parseObject(listStr);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
    }

    public List<JSONObject> getDepartmentUserForeachList(String cubeOrgId, String imDeptId,String ak, String sk, int index, int size){
        JSONObject result = this.getDepartmentUserList(cubeOrgId, imDeptId,ak, sk, index, size);
        if (ObjectUtils.isEmpty(result)){
            return null;
        }
        String listStr = result.getString("data");
        List<JSONObject> list = null;
        try {
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        }catch (Exception e){
        }
        if (CollectionUtils.isEmpty(list) || list.size() <= 0){
            return null;
        }
        Integer totalPages = result.getInteger("totalPages");
        if (totalPages > index){
            List<JSONObject> subList = getDepartmentUserForeachList(cubeOrgId, imDeptId,ak, sk, index + 1, size);
            if (!CollectionUtils.isEmpty(subList)) {
                list.addAll(subList);
            }
        }
        return list;
    }


    public List<JSONObject> queryImDeptList(String ak, String sk, String cubeOrgId) {
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = CubeUtil.queryImDeptList(cubeOrgId,ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "queryImDeptList 魔⽅组织下钉钉部⻔列表失败 ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("cube queryImDeptList 魔⽅组织下钉钉部⻔列表" + jsonObj);
            String listStr = jsonObj.getString("result");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        } else {
            LogUtil.error("queryImDeptList 魔⽅组织下钉钉部⻔列表失败  jsonObj" + jsonObj);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return list;
    }

    public List<JSONObject> queryImDeptListByParentId(String ak, String sk, String cubeOrgId, String parentId) {
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = CubeUtil.queryImDeptListByParentId(cubeOrgId, ak, sk, parentId);
        }catch (Exception e){
            LogUtil.error(e, "queryImDeptListByParentId 魔⽅组织部⻔列表失败 ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("cube queryImDeptListByParentId 魔⽅组织部⻔列表 " + jsonObj);
            String listStr = jsonObj.getString("result");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        } else {
            LogUtil.error("queryImDeptListByParentId 魔⽅组织部⻔列表失败   jsonObj" + jsonObj);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return list;
    }
    public List<JSONObject> queryImUserIdList(String cubeOrgId, List<String> cubeUserIds, String ak, String sk) {
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = CubeUtil.queryImUserIdList(cubeOrgId, cubeUserIds, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "queryImUserIdList 据用户ID查询绑定的钉钉id ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("cube queryImUserIdList 据用户ID查询绑定的钉钉id " + jsonObj + "，cubeUserIds=" + JsonUtils.beanToJson(cubeUserIds));
            String listStr = jsonObj.getString("result");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        } else {
            LogUtil.error("queryImUserIdList 据用户ID查询绑定的钉钉id   jsonObj" + jsonObj);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return list;
    }

    public String getCorpToken(String ak, String sk) {
        String suiteTicket = getSuiteTicket();
        String corpId = getCubeTicketCorpId();
        return refreshDingCoreToken(stringRedisTemplate, corpId, suiteTicket, ak, sk);
    }

    /** 刷新钉钉企业内部应用的accessToken*/
    public static String refreshDingCoreToken(StringRedisTemplate stringRedisTemplate, String corpId, String suiteTicket, String ak, String sk) {
        String dingKey = GlobalConfig.ding_core_accessToken_key + ak;

        String accessToken = stringRedisTemplate.opsForValue().get(dingKey);
        if (!ObjectUtils.isEmpty(accessToken)) {
            return accessToken;
        }
        long expires = 7200;
        JSONObject body = CubeUtil.getCorpToken(corpId, suiteTicket,  ak, sk);
        if (ObjectUtils.isEmpty(body)) {
            int i = 3;
            while (i > 0){
                LogUtil.info("refreshCoreDingToken 尝试重新获取 i=" + i);
                body = CubeUtil.getCorpToken(corpId, suiteTicket,  ak, sk);
                if (!ObjectUtils.isEmpty(body)){
                    break;
                }
                i --;
            }
        }
        if (!ObjectUtils.isEmpty(body)) {
            expires = body.getInteger("expiresIn");
            accessToken = body.getString("token");
            stringRedisTemplate.opsForValue().set(dingKey, accessToken, expires, TimeUnit.SECONDS);
        }
        return ObjectUtils.isEmpty(accessToken) ? "" : accessToken;
    }


    public String getSuiteTicket() {
        String suiteTicket = stringRedisTemplate.opsForValue().get(GlobalConfig.SUITE_TICKET_KEY);
        if (StringUtils.hasText(suiteTicket)) {
            return suiteTicket;
        }
        throw new RuntimeException("suiteTicket 已过期，请等待重新推送");
    }

    public String getCubeTicketCorpId() {
        String corpId = stringRedisTemplate.opsForValue().get(GlobalConfig.CUBE_SUITE_TICKET_CORP_ID);
        if (StringUtils.hasText(corpId)) {
            return corpId;
        }
        throw new RuntimeException("第三方企业corpId 已过期，请等待重新推送");
    }
}
