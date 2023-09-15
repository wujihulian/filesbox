package com.svnlan.user.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dto.PluginDto;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.GroupService;
import com.svnlan.user.service.SystemSettingService;
import com.svnlan.user.service.UserService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.tools.ShellExecTool;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.PluginListVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 15:07
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Resource
    SystemOptionDao systemOptionDao;
    @Resource
    OptionTool optionTool;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UserService userService;
    @Resource
    GroupService groupService;
    // 磁盘总大小
    @Value("${disk.default.size}")
    private Long diskDefaultSize;

    @Override
    public Map<String, Object> getSystemSetting(LoginUser loginUser){
        // 校验是否是系统管理员
        //checkAdministrator(loginUser);

        /** 系统用户配置 */
        // 企业网盘
        HomeExplorerVO vo = homeExplorerDao.getEnterpriseSpace();
        Map<String, Object> reMap = optionTool.getSystemConfigMap();
        if (ObjectUtils.isEmpty(reMap)){
            reMap = new HashMap<>(1);
        }
        reMap.put("commonSpaceName", vo.getName());
        return reMap;
    }

    @Override
    public void setSystemSetting(String prefix, SystemOptionDTO optionDTO, LoginUser loginUser){
        // 校验是否是系统管理员
        //checkAdministrator(loginUser);
        // 校验参数是否正确
        checkParam(optionDTO);

        List<String> paramList = Arrays.asList("defaultHome");
        List<String> addList = null;
        List<String> checkList = systemOptionDao.checkSystemConfigByKeyList(paramList);
        if (CollectionUtils.isEmpty(checkList)){
            addList = paramList;
        }else if (checkList.size() != paramList.size()){
            addList = new ArrayList<>();
            for (String k : paramList){
                if (!checkList.contains(k)){
                    addList.add(k);
                }
            }
        }

        if (!CollectionUtils.isEmpty(addList)){
            List<OptionVo> addVoList = new ArrayList<>();
            if (addList.contains("defaultHome")){
                addVoList.add(new OptionVo("defaultHome", optionDTO.getDefaultHome()));
            }
            try {
                systemOptionDao.batchInsert(addVoList);
            } catch (Exception e) {
                LogUtil.error(e, prefix + " systemOptionDao batchInsert error " + JsonUtils.beanToJson(optionDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            }
        }

        List<OptionVo> systemOptionList = changeSystemSetting(optionDTO, addList);
        if (!CollectionUtils.isEmpty(systemOptionList)) {
            try {
                systemOptionDao.updateSystemOption("", systemOptionList);
            } catch (Exception e) {
                LogUtil.error(e, prefix + " updateGroup error " + JsonUtils.beanToJson(optionDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }
    }

    private void checkParam(SystemOptionDTO optionDTO){

    }
    private List<OptionVo> changeSystemSetting(SystemOptionDTO optionDTO, List<String> addList){
        List<OptionVo> list = new ArrayList<>();

        list.add(new OptionVo("browserLogo", ObjectUtils.isEmpty(optionDTO.getBrowserLogo()) ? "" : optionDTO.getBrowserLogo()));
        list.add(new OptionVo("dateFormat", ObjectUtils.isEmpty(optionDTO.getDateFormat()) ? "" : optionDTO.getDateFormat()));
        list.add(new OptionVo("globalIcp", ObjectUtils.isEmpty(optionDTO.getGlobalIcp()) ? "" : optionDTO.getGlobalIcp()));
        list.add(new OptionVo("groupRootName", ObjectUtils.isEmpty(optionDTO.getGroupRootName()) ? "" : optionDTO.getGroupRootName()));
        list.add(new OptionVo("meta", ObjectUtils.isEmpty(optionDTO.getMeta()) ? "" : optionDTO.getMeta()));
        list.add(new OptionVo("needCheckCode", ObjectUtils.isEmpty(optionDTO.getNeedCheckCode()) ? "" : optionDTO.getNeedCheckCode()));
        list.add(new OptionVo("passwordErrorLock", ObjectUtils.isEmpty(optionDTO.getPasswordErrorLock()) ? "" : optionDTO.getPasswordErrorLock()));
        list.add(new OptionVo("passwordRule", ObjectUtils.isEmpty(optionDTO.getPasswordRule()) ? "" : optionDTO.getPasswordRule()));
        list.add(new OptionVo("seo", ObjectUtils.isEmpty(optionDTO.getSeo()) ? "" : optionDTO.getSeo()));
        list.add(new OptionVo("seoDesc", ObjectUtils.isEmpty(optionDTO.getSeoDesc()) ? "" : optionDTO.getSeoDesc()));
        list.add(new OptionVo("shareLinkAllow", ObjectUtils.isEmpty(optionDTO.getShareLinkAllow()) ? "" : optionDTO.getShareLinkAllow()));
        list.add(new OptionVo("shareLinkAllowGuest", ObjectUtils.isEmpty(optionDTO.getShareLinkAllowGuest()) ? "" : optionDTO.getShareLinkAllowGuest()));
        list.add(new OptionVo("shareLinkPasswordAllowEmpty", ObjectUtils.isEmpty(optionDTO.getShareLinkPasswordAllowEmpty()) ? "" : optionDTO.getShareLinkPasswordAllowEmpty()));
        list.add(new OptionVo("showFileLink", ObjectUtils.isEmpty(optionDTO.getShowFileLink()) ? "" : optionDTO.getShowFileLink()));
        list.add(new OptionVo("showFileMd5", ObjectUtils.isEmpty(optionDTO.getShowFileMd5()) ? "" : optionDTO.getShowFileMd5()));
        list.add(new OptionVo("systemDesc", ObjectUtils.isEmpty(optionDTO.getSystemDesc()) ? "" : optionDTO.getSystemDesc()));
        list.add(new OptionVo("systemLogo", ObjectUtils.isEmpty(optionDTO.getSystemLogo()) ? "" : optionDTO.getSystemLogo()));
        list.add(new OptionVo("systemName", ObjectUtils.isEmpty(optionDTO.getSystemName()) ? "" : optionDTO.getSystemName()));
        list.add(new OptionVo("treeOpen", ObjectUtils.isEmpty(optionDTO.getTreeOpen()) ? "" : optionDTO.getTreeOpen()));
        list.add(new OptionVo("needMark", ObjectUtils.isEmpty(optionDTO.getNeedMark()) ? "0" : optionDTO.getNeedMark()));
        /** 水印 */
        list.add(new OptionVo("markType", ObjectUtils.isEmpty(optionDTO.getMarkType()) ? "0" : optionDTO.getMarkType()));
        list.add(new OptionVo("wmTitle", ObjectUtils.isEmpty(optionDTO.getWmTitle()) ? "" : optionDTO.getWmTitle()));
        list.add(new OptionVo("wmColor", ObjectUtils.isEmpty(optionDTO.getWmColor()) ? "#eae0f8" : optionDTO.getWmColor()));
        list.add(new OptionVo("wmFont", ObjectUtils.isEmpty(optionDTO.getWmFont()) ? "SimSun" : optionDTO.getWmFont()));
        list.add(new OptionVo("wmSize", ObjectUtils.isEmpty(optionDTO.getWmSize()) ? "28" : optionDTO.getWmSize()));
        list.add(new OptionVo("wmSubTitle", ObjectUtils.isEmpty(optionDTO.getWmSubTitle()) ? "" : optionDTO.getWmSubTitle()));
        list.add(new OptionVo("wmSubColor", ObjectUtils.isEmpty(optionDTO.getWmSubColor()) ? "#d9d9d9" : optionDTO.getWmSubColor()));
        list.add(new OptionVo("wmSubFont", ObjectUtils.isEmpty(optionDTO.getWmSubFont()) ? "SimSun" : optionDTO.getWmSubFont()));
        list.add(new OptionVo("wmSubSize", ObjectUtils.isEmpty(optionDTO.getWmSubSize()) ? "16" : optionDTO.getWmSubSize()));
        list.add(new OptionVo("wmTransparency", ObjectUtils.isEmpty(optionDTO.getWmTransparency()) ? "50" : optionDTO.getWmTransparency()));
        list.add(new OptionVo("wmRotate", ObjectUtils.isEmpty(optionDTO.getWmRotate()) ? "45" : optionDTO.getWmRotate()));
        list.add(new OptionVo("wmMargin", ObjectUtils.isEmpty(optionDTO.getWmMargin()) ? "" : optionDTO.getWmMargin()));
        list.add(new OptionVo("wmPicPath", ObjectUtils.isEmpty(optionDTO.getWmPicPath()) ? "" : optionDTO.getWmPicPath()));
        list.add(new OptionVo("wmPicSize", ObjectUtils.isEmpty(optionDTO.getWmPicSize()) ? "" : optionDTO.getWmPicSize()));
        list.add(new OptionVo("wmPosition", ObjectUtils.isEmpty(optionDTO.getWmPosition()) ? "" : optionDTO.getWmPosition()));
        list.add(new OptionVo("thirdLoginConfig", ObjectUtils.isEmpty(optionDTO.getThirdLoginConfig()) ? "" : optionDTO.getThirdLoginConfig()));
        list.add(new OptionVo("registerConfig", ObjectUtils.isEmpty(optionDTO.getRegisterConfig()) ? "" : optionDTO.getRegisterConfig()));
        list.add(new OptionVo("needUploadCheck", ObjectUtils.isEmpty(optionDTO.getNeedUploadCheck()) ? "" : optionDTO.getNeedUploadCheck()));
        if (CollectionUtils.isEmpty(addList)) {
            list.add(new OptionVo("defaultHome", ObjectUtils.isEmpty(optionDTO.getDefaultHome()) ? "" : optionDTO.getDefaultHome()));
        }else {
            if (!addList.contains("defaultHome")){
                list.add(new OptionVo("defaultHome", ObjectUtils.isEmpty(optionDTO.getDefaultHome()) ? "" : optionDTO.getDefaultHome()));
            }
        }

        return list;
    }

    /** 校验是否是系统管理员 */
    private void checkAdministrator(LoginUser loginUser){
        if (ObjectUtils.isEmpty(loginUser) || ObjectUtils.isEmpty(loginUser.getUserType()) || 1 != loginUser.getUserType()){
            throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
        }

    }

    @Override
    public List pluginList(PluginDto optionDTO){
        // 插件列表
        return optionTool.pluginList();
    }
    @Override
    public void setPlugin(String prefix, PluginDto optionDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(optionDTO) || ObjectUtils.isEmpty(optionDTO.getPluginID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 校验是否是系统管理员
        OptionVo optionVo = systemOptionDao.getSystemOtherConfigById(optionDTO.getPluginID());
        if (ObjectUtils.isEmpty(optionVo)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        PluginListVo value = JsonUtils.jsonToBean(optionVo.getValue(), PluginListVo.class);
        if (ObjectUtils.isEmpty(value)){
            value = new PluginListVo();
        }
        if (!ObjectUtils.isEmpty(optionDTO.getName())){
            value.setName(optionDTO.getName());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getStatus())) {
            value.setStatus(optionDTO.getStatus());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getDescription())) {
            value.setDescription(optionDTO.getDescription());
        }

        try {
            systemOptionDao.updateSystemOptionById(optionDTO.getPluginID(), JsonUtils.beanToJson(value));
        } catch (Exception e) {
            LogUtil.error(e, prefix + " updateGroup error " + JsonUtils.beanToJson(optionDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }

    @Override
    public PluginListVo getMsgWarningSetting(){

        String key = GlobalConfig.mesWarning_cache_key;
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        String msg = valueOperations.get(key);

        boolean cache = true;
        PluginListVo  pluginListVo = null;
        if (!ObjectUtils.isEmpty(msg)){
            try {
                pluginListVo = JsonUtils.jsonToBean(msg, PluginListVo.class);
                cache = false;
            }catch (Exception e){
                LogUtil.error(e, "消息预警缓存解析失败 msg=" + msg);
            }
        }
        if (ObjectUtils.isEmpty(pluginListVo)){
            List<PluginListVo> list = optionTool.pluginList("System.mesWarning");
            if (CollectionUtils.isEmpty(list)){
                pluginListVo = new PluginListVo();
                pluginListVo.setEnable("0");
            }else {
                pluginListVo = list.get(0);
            }
        }
        if (cache && !ObjectUtils.isEmpty(pluginListVo)){
            valueOperations.set(key, JsonUtils.beanToJson(pluginListVo), GlobalConfig.mesWarning_cache_ttl, TimeUnit.HOURS);
        }

        // 系统信息
        //getSystemInfo(pluginListVo);

        return  pluginListVo;
    }

    @Override
    public Map<String, Object> getSystemInfo(){
        Map<String, Object> reMap = new HashMap<>(3);


        List<String> cpuList = ShellExecTool.callShellByExec("vmstat 1 1");
        List<String> memoryList = ShellExecTool.callShellByExec("free -g");
        /*
         procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 0 21      0 5512068      4 15969484    0    0     1    20    0    0  8  3 41 48  0
           */
        String freeCpu = ShellExecTool.jxCpuValue(cpuList);
        reMap.put("freeCpu",freeCpu);

        /*
              total        used        free      shared  buff/cache   available
Mem:             47          26           4           0          15          18
Swap:             0           0           0
         */
        Double freeMemoryRate = ShellExecTool.jxMemoryValue(memoryList, reMap);
        reMap.put("freeMemoryRate",freeMemoryRate);

        // 空间
        // 用户使用的空间总和
        Long userUsedTotal = userService.sumUserSpaceUsed();
        // 组织机构使用的空间总和
        Long groupUsedTotal = groupService.sumGroupSpaceUsed();
        // 总使用数
        Long totalSpaceUsed = userUsedTotal + groupUsedTotal;

        reMap.put("totalSpace", diskDefaultSize);
        reMap.put("spaceUsed", totalSpaceUsed);
        return reMap;
    }

    @Override
    public void setMsgWarning(String prefix, PluginDto optionDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(optionDTO)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        OptionVo optionVo = null;
        // 校验是否是系统管理员
        if (!ObjectUtils.isEmpty(optionDTO.getPluginID())) {
            optionVo = systemOptionDao.getSystemOtherConfigById(optionDTO.getPluginID());
        }else
        if (!ObjectUtils.isEmpty(optionDTO.getId())) {
            optionVo = systemOptionDao.getSystemOtherConfigByKey("System.mesWarning", "ID-" + optionDTO.getId());
        }
        if (ObjectUtils.isEmpty(optionVo)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        PluginListVo value = JsonUtils.jsonToBean(optionVo.getValue(), PluginListVo.class);
        if (ObjectUtils.isEmpty(value)){
            value = new PluginListVo();
        }
        if (!ObjectUtils.isEmpty(optionDTO.getEnable())){
            value.setEnable(optionDTO.getEnable());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getStatus())) {
            value.setStatus(optionDTO.getStatus());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getWarnType())) {
            value.setWarnType(optionDTO.getWarnType());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getUseRatio())) {
            value.setUseRatio(optionDTO.getUseRatio());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getUseTime())) {
            value.setUseTime(optionDTO.getUseTime());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getSendType())) {
            value.setSendType(optionDTO.getSendType());
        }
        if (!ObjectUtils.isEmpty(optionDTO.getTarget())) {
            value.setTarget(optionDTO.getTarget());
        }
        value.setDingUrl(ObjectUtils.isEmpty(optionDTO.getDingUrl()) ? "" : optionDTO.getDingUrl());
        value.setWechatUrl(ObjectUtils.isEmpty(optionDTO.getWechatUrl()) ? "" : optionDTO.getWechatUrl());

        try {
            systemOptionDao.updateSystemOptionById(optionVo.getId(), JsonUtils.beanToJson(value));
        } catch (Exception e) {
            LogUtil.error(e, prefix + " updateGroup error " + JsonUtils.beanToJson(optionDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }
}
