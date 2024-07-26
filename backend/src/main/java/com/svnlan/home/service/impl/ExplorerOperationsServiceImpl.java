package com.svnlan.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.MetaEnum;
import com.svnlan.enums.SendOperateTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.ExplorerOperationsDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.ExplorerOperationsService;
import com.svnlan.home.utils.AsyncCountSizeUtil;
import com.svnlan.home.vo.HomeExplorerShareDetailVO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.Email;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.MailService;
import com.svnlan.user.tools.UserTool;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author KingMgg
 * @data 2023/2/7 16:13
 */
@Service
public class ExplorerOperationsServiceImpl implements ExplorerOperationsService {

    @Resource
    ExplorerOperationsDao operationsDao;
    @Resource
    AsyncCountSizeUtil asyncCountSizeUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    JWTService jwtService;
    @Resource
    UserTool userTool;
    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    UserDao userDao;
    @Resource
    MailService mailService;
    @Resource
    SystemOptionDao systemOptionDaoImpl;


    @Override
    public void initSourcePathLevel(Long sourceID) {

        Map<String, Object> paramMap = new HashMap<>(0);
        if (!ObjectUtils.isEmpty(sourceID) && sourceID > 0){
            paramMap.put("sourceID", sourceID);
        }

        List<HomeExplorerShareDetailVO> list = operationsDao.getAllSourceList(paramMap);

        Map<Long, String> levelMap = new HashMap<>(1);
        levelMap.put(1L, ",0,1,");
        levelMap.put(0L, ",0,");

        levelMap = checkList(levelMap, list);

        for (HomeExplorerShareDetailVO detailVO : list){
            if (detailVO.getParentID() <= 0 ){
                continue;
            }
            detailVO.setParentLevel(levelMap.get(detailVO.getParentID()));
        }

        for (List<HomeExplorerShareDetailVO> subList : Partition.ofSize(list, 1500)) {
            try {
               // operationsDao.batchUpdateLevel(subList);
            } catch (Exception e) {
                LogUtil.error(e, "复制出错");
            }
        }

        System.out.println(true);

    }

    private Map<Long, String> checkList(Map<Long, String> levelMap, List<HomeExplorerShareDetailVO> list){
        List<HomeExplorerShareDetailVO> childList = new ArrayList<>();
        List<String> deList = new ArrayList<>();
        for (HomeExplorerShareDetailVO detailVO : list){
            if (1 == detailVO.getIsFolder()){
                if (detailVO.getParentID() <= 0){
                    levelMap.put(detailVO.getSourceID(), ",0," + detailVO.getSourceID() + ",");
                }else {
                    if (levelMap.containsKey(detailVO.getParentID())){
                        levelMap.put(detailVO.getSourceID(), levelMap.get(detailVO.getParentID()) + detailVO.getSourceID() + ",");
                    }else {

                        if (detailVO.getParentLevel().indexOf("," + detailVO.getParentID() + ",") >= 0) {
                            childList.add(detailVO);
                        }else {
                            deList.add(detailVO.getSourceID() + "");
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(deList)){
            LogUtil.info("deList===========" + deList.stream().collect(Collectors.joining(",", "", "")));
        }
        if (!CollectionUtils.isEmpty(childList)){
            levelMap = checkList(levelMap, childList);
        }
        return levelMap;
    }

    @Override
    public void countSize(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser){

        if (ObjectUtils.isEmpty(updateFileDTO.getTaskID())){
            updateFileDTO.setTaskID(RandomUtil.getuuid());
            resultMap.put("taskID", updateFileDTO);
            asyncCountSizeUtil.asyncCountSize(updateFileDTO);
        }else {
            String a = stringRedisTemplate.opsForValue().get("countSize_taskID_key_" + updateFileDTO.getTaskID() );
            resultMap.put("status", a);
            if ("1".equals(a)){
                stringRedisTemplate.delete("countSize_taskID_key_" + updateFileDTO.getTaskID());
            }
        }

    }

    @Override
    public void setSafePwd(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser){
        if (ObjectUtils.isEmpty(updateFileDTO.getSafePwd())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        HomeExplorerVO homeExplorerVO = ioSourceDao.getMySafeBoxSource(loginUser.getUserID());
        if (ObjectUtils.isEmpty(homeExplorerVO)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long safeSourceId = homeExplorerVO.getSourceID();

        String mySafePwd = ioSourceMetaDao.getValueMetaByKey(safeSourceId, GlobalConfig.safe_meta_code);
        if (!ObjectUtils.isEmpty(updateFileDTO.getOldSafePwd())) {
            // 解析出原密码
            String originPassword = decodePassword(updateFileDTO.getOldSafePwd(), loginUser.getUserID());
            // 原先加过密的密码
            String oldCryptoPassword = PasswordUtil.passwordEncode(originPassword, GlobalConfig.PWD_SALT);

            if (ObjectUtils.isEmpty(mySafePwd)){
                mySafePwd = "";
            }
            if (!oldCryptoPassword.equals(mySafePwd)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
            }
        }else if (!ObjectUtils.isEmpty(updateFileDTO.getCode())) {

            UserVo userVo = userDao.getUserInfo(loginUser.getUserID());
            // 校验邮箱
            if (ObjectUtils.isEmpty(userVo.getEmail()) ){
                LogUtil.error("私密保险箱邮箱找回密码时邮箱为空");
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            UserDTO optionDTO = new UserDTO();
            optionDTO.setCode(updateFileDTO.getCode());
            optionDTO.setValue(userVo.getEmail());
            optionDTO.setOpType(SendOperateTypeEnum.SAFE.getCode());
            userTool.checkEmailCode(optionDTO, true);
        }else {
            if (!ObjectUtils.isEmpty(mySafePwd)){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
        }
        String newPassword = decodePassword(updateFileDTO.getSafePwd(), loginUser.getUserID());
        String pwd = PasswordUtil.passwordEncode(newPassword, GlobalConfig.PWD_SALT);

        if (ObjectUtils.isEmpty(mySafePwd)){
            List<IOSourceMeta> pyList = new ArrayList<>();
            pyList.add(new IOSourceMeta(safeSourceId, GlobalConfig.safe_meta_code, pwd, loginUser.getTenantId()));
            // 添加
            ioSourceMetaDao.batchInsert(pyList);
        }else {
            // 修改
            ioSourceMetaDao.updateMetaByKey(safeSourceId, GlobalConfig.safe_meta_code, pwd);
            stringRedisTemplate.delete(GlobalConfig.user_safe_redis_key + loginUser.getUserID());
        }
    }
    @Override
    public void checkSafePwd(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser){
        if (ObjectUtils.isEmpty(updateFileDTO.getSafePwd())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        HomeExplorerVO homeExplorerVO = ioSourceDao.getMySafeBoxSource(loginUser.getUserID());
        if (ObjectUtils.isEmpty(homeExplorerVO)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long safeSourceId = homeExplorerVO.getSourceID();
        // 解析出原密码
        String originPassword = decodePassword(updateFileDTO.getSafePwd(), loginUser.getUserID());
        // 原先加过密的密码
        String oldCryptoPassword = PasswordUtil.passwordEncode(originPassword, GlobalConfig.PWD_SALT);

        String mySafePwd = ioSourceMetaDao.getValueMetaByKey(safeSourceId, GlobalConfig.safe_meta_code);

        if (ObjectUtils.isEmpty(mySafePwd)){
            mySafePwd = "";
        }
        if (!oldCryptoPassword.equals(mySafePwd)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
        }

        Long t = DateUtil.getTimeDiff();
        stringRedisTemplate.opsForValue().set(GlobalConfig.user_safe_redis_key + loginUser.getUserID(), "1", t, TimeUnit.MINUTES);
    }

    private String decodePassword(String password, Long userId) {
        com.svnlan.jwt.dto.UserDTO userDTO = new com.svnlan.jwt.dto.UserDTO();
        userDTO.setPassword(password);
        userDTO.setName(userId.toString());
        return jwtService.checkPassword(userDTO);
    }

    @Override
    public JSONObject sendEmailFile(UserDTO userDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(userDTO.getEmail()) || ObjectUtils.isEmpty(userDTO.getValue())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<Long> sourceIds = Arrays.asList(userDTO.getValue().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());

        List<IOSourceVo> list = ioSourceDao.copySourcePathList(sourceIds);
        if (CollectionUtils.isEmpty(list)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        String emailTitle = "【" + systemOptionDaoImpl.getSystemConfigByKey("systemName") + "】";
        Email email = new Email();
        email.setEmail(userDTO.getEmail());
        email.setSubject(emailTitle);
        email.setContent("");

        return mailService.sendAtt(email, list);
    }

    @Override
    public JSONObject sendEmailCheck(UserDTO userDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(userDTO.getEmailConfig())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        JSONObject mailObj = null;
        try {
            mailObj = JSONObject.parseObject(userDTO.getEmailConfig());
        }catch (Exception e){
            LogUtil.error("检测发送邮件失败，请先添加邮箱设置 error ", e.getMessage());
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        if (ObjectUtils.isEmpty(mailObj)
                || ObjectUtils.isEmpty(mailObj.getString("server"))
                || ObjectUtils.isEmpty(mailObj.getString("email"))
                || ObjectUtils.isEmpty(mailObj.getString("password"))
                ){
            LogUtil.error("检测发送邮件失败，请先添加邮箱设置 e");
            throw new SvnlanRuntimeException(CodeMessageEnum.invalidEmail);
        }
        String emailTitle = "【" + systemOptionDaoImpl.getSystemConfigByKey("systemName") + "】";
        String emailContent = "success";
        Email email = new Email();
        email.setEmail(mailObj.getString("email"));
        email.setSubject(emailTitle);
        email.setContent(emailTitle + emailContent);

        return mailService.sendCheck(email, mailObj);
    }
}
