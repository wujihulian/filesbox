package com.svnlan.home.utils;

import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.enums.MetaEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.domain.IoSourceEvent;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.TenantUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: sulijuan
 * @Description:
 */
@Component
public class DirectoryUtil {
    @Resource
    IoSourceMetaDao ioSourceMetaDaoImpl;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    StorageService storageService;
    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    SourceOperateTool sourceOperateTool;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    OptionTool optionTool;



    /** 批量添加文件夹 */
    public List<AddSubCloudDirectoryDTO> addBatchDirectory(List<AddSubCloudDirectoryDTO> list, LoginUser loginUser, CommonSource commonSource, HttpServletRequest request){

        Integer targetType = commonSource.getTargetType();
        Long opUserId = loginUser.getUserID();
        // top第一层
        List<String> sourceNameList =  ioSourceDao.getSourceNameList(commonSource.getSourceID());
        List<IOSourceMeta> sourceMetaList = new ArrayList<>();
        List<Map<String, Object>> paramList = new ArrayList<>();
        List<IoSourceEvent> eventList = new ArrayList<>();
        EventEnum eventEnum = EventEnum.mkdir;
        AddSubCloudDirectoryDTO addSubCloudDirectoryDTO = new AddSubCloudDirectoryDTO();
        addSubCloudDirectoryDTO.setSourceID(commonSource.getSourceID());
        addSubCloudDirectoryDTO.setParentLevel(commonSource.getParentLevel());
        Integer storageID = storageService.getDefaultStorageDeviceId();


        this.addSubDir(list,  addSubCloudDirectoryDTO, targetType,  opUserId
                , sourceNameList, eventEnum,  eventList,  sourceMetaList, storageID, paramList);
        LogUtil.info("addBatchDir list=" + JsonUtils.beanToJson(list));

        if (!CollectionUtils.isEmpty(sourceMetaList)){
            try {
                ioSourceMetaDaoImpl.batchInsert(sourceMetaList);
            }catch (Exception e){
                LogUtil.error(e, " sourceMetaList meta error paramList=" + JsonUtils.beanToJson(list) + "，sourceMetaList=" + JsonUtils.beanToJson(sourceMetaList));
            }
        }
        if (!CollectionUtils.isEmpty(eventList)){
            fileOptionTool.addSourceEventList(eventList);
        }



        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileMkDir.getCode(), paramList, request);
        return list;
    }


    private void addSubDir(List<AddSubCloudDirectoryDTO> list, AddSubCloudDirectoryDTO addSubCloudDirectoryDTO, Integer targetType, Long  opUserId
            , List<String> sourceNameList,EventEnum eventEnum, List<IoSourceEvent> eventList, List<IOSourceMeta> sourceMetaList, Integer storageID
    ,List<Map<String, Object>> paramList){
        int isSafe = optionTool.getSourceIsSafe(opUserId,addSubCloudDirectoryDTO.getParentLevel() + addSubCloudDirectoryDTO.getSourceID() + ",");

        /** 操作日志 */
        Map<String, Object> reMap = null;
        for (AddSubCloudDirectoryDTO firstSource : list){
            if (!CollectionUtils.isEmpty(sourceNameList)){
                firstSource.setOldName(firstSource.getName());
                firstSource.setName(fileOptionTool.checkRepeatName(firstSource.getName(), firstSource.getName(), sourceNameList, 1));
            }
            firstSource.setParentLevel(addSubCloudDirectoryDTO.getParentLevel() + addSubCloudDirectoryDTO.getSourceID() + ",");
            firstSource.setTargetID(opUserId);
            firstSource.setCreateUser(opUserId);
            firstSource.setModifyUser(opUserId);
            firstSource.setIsFolder(1);
            firstSource.setTargetType(targetType);
            firstSource.setFileType("");
            firstSource.setFileID(0L);
            firstSource.setParentID(addSubCloudDirectoryDTO.getSourceID());
            firstSource.setStorageID(storageID);
            firstSource.setNamePinyin(ChinesUtil.getPingYin(firstSource.getName()));
            firstSource.setNamePinyinSimple(ChinesUtil.getFirstSpell(firstSource.getName()));
            firstSource.setIsSafe(isSafe);
            if (firstSource.getParentID() <= 0){
                LogUtil.error("创建文件夹失败，不可创建根目录文件");
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            try {
                homeExplorerDao.createDirectory(firstSource);
            }catch (Exception e){
                LogUtil.error(e, " createDirectory error");
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
            }
            if (ObjectUtils.isEmpty(firstSource.getSourceID()) && firstSource.getSourceID() <= 0){
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
            Long saveTopSourceID = firstSource.getSourceID();
            // 第一层文件夹拼音
            sourceMetaList.add(new IOSourceMeta(saveTopSourceID, MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(firstSource.getName())));
            sourceMetaList.add(new IOSourceMeta(saveTopSourceID, MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(firstSource.getName())));
            // 第一层文件夹动态
            eventList.add(new IoSourceEvent(saveTopSourceID, firstSource.getParentID(), opUserId, eventEnum.getCode(), FileOptionTool.getSourceEventDesc(eventEnum, "", "")));

            // 日志
            reMap = new HashMap<>(4);
            reMap.put("sourceID", firstSource.getSourceID());
            reMap.put("sourceParent", firstSource.getParentID());
            reMap.put("type", "mkdir");
            reMap.put("pathName", firstSource.getName());
            paramList.add(reMap);

            if (!CollectionUtils.isEmpty(firstSource.getChildren())){
                addSubDir(firstSource.getChildren(), firstSource , targetType,  opUserId
                        , null,eventEnum, eventList, sourceMetaList,storageID, paramList);

            }
        }
    }
    public CommonSource createDirSource(String name, LoginUser loginUser, CommonSource parent) {
        IOSource s = createDir(name, loginUser, parent);
        CommonSource source = new CommonSource();
        source.setUserID(loginUser.getUserID());
        source.setName(s.getName());
        source.setSourceID(s.getId());
        source.setParentLevel(s.getParentLevel());
        source.setTargetType(s.getTargetType());
        source.setParentID(s.getParentId());
        return source;
    }
    public IOSource createDir(String name, LoginUser loginUser, CommonSource parent) {
        int isSafe = optionTool.getSourceIsSafe(loginUser.getUserID(),parent.getParentLevel() + parent.getSourceID() + ",");

        List<String> sourceNameList = ioSourceDao.getSourceNameList(parent.getSourceID());

        IOSource source = new IOSource();
        source.setName(fileOptionTool.checkRepeatName(name, name, sourceNameList, 1));
        source.setParentLevel(parent.getParentLevel() + parent.getSourceID() + "," );
        source.setTargetId(loginUser.getUserID());
        source.setCreateUser(loginUser.getUserID());
        source.setModifyUser(loginUser.getUserID());
        source.setIsFolder(1);
        source.setTargetType(parent.getTargetType());
        source.setFileType("");
        source.setFileId(0L);
        source.setParentId(parent.getSourceID());
        source.setStorageId(storageService.getDefaultStorageDeviceId());
        source.setTenantId(tenantUtil.getTenantIdByServerName());
        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        source.setIsSafe(isSafe);

        if (source.getParentId() <= 0){
            LogUtil.error("创建文件夹失败，不可创建根目录文件");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        try {
            homeExplorerDao.createDir(source);
        }catch (Exception e){
            LogUtil.error(e, " createDir error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        if (!ObjectUtils.isEmpty(source.getId()) && source.getId() > 0){
            sourceOperateTool.setSourcePinYin(source.getId(), source.getName());

            if (parent.getTargetType().intValue() == 2) {
                String groupSource = ioSourceMetaDao.getValueMetaByKey(source.getParentId(), "groupSource");
                if (!ObjectUtils.isEmpty(groupSource)) {
                    ioSourceMetaDao.insert(new IOSourceMeta(source.getId(), "groupSource", groupSource));
                }
            }
        }

        // 添加文档操作event
        fileOptionTool.addSourceEvent(source.getId(), source.getParentId(), loginUser.getUserID(), source.getName(), EventEnum.mkdir);


        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", source.getId());
        reMap.put("sourceParent", source.getParentId());
        reMap.put("type", "mkdir");
        reMap.put("pathName", source.getName());
        paramList.add(reMap);

        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileMkDir.getCode(), paramList, systemLogTool.getRequest());
        return source;

    }

}
