package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.AddCloudDirectoryDTO;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.service.SourceFileService;
import com.svnlan.home.utils.DirectoryUtil;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.UserOptionDao;
import com.svnlan.user.domain.UserOption;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/3 9:41
 */
@Service
public class SourceFileServiceImpl implements SourceFileService {

    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    DirectoryUtil directoryUtil;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UserOptionDao userOptionDao;

    @Override
    public List<AddSubCloudDirectoryDTO> addBatchDirectory(AddCloudDirectoryDTO addCloudDirectoryDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(addCloudDirectoryDTO) || CollectionUtils.isEmpty(addCloudDirectoryDTO.getChildren())
                || ObjectUtils.isEmpty(addCloudDirectoryDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        for (AddSubCloudDirectoryDTO dto : addCloudDirectoryDTO.getChildren()){
            if (ObjectUtils.isEmpty(dto) || ObjectUtils.isEmpty(dto.getName())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
        }

        CommonSource commonSource = ioSourceDao.getSourceInfo(addCloudDirectoryDTO.getSourceID());
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }

        List<AddSubCloudDirectoryDTO> list = null;
        try {
            /************批量添加文件夹**********************************/
            list = directoryUtil.addBatchDirectory(addCloudDirectoryDTO.getChildren(), loginUser, commonSource, systemLogTool.getRequest());

        } catch (SvnlanRuntimeException e){
            throw new SvnlanRuntimeException(e.getErrorCode(), e.getMessage());
        } catch (Exception e){
            LogUtil.error(e, "目录添加失败" + JsonUtils.beanToJson(addCloudDirectoryDTO) + "," + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;
    }

    @Override
    public List getImgList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        List<String> fileTypeList = Arrays.asList("jpg","png","jpeg","JPG","PNG","JPEG");
        List<HomeExplorerVO> list =  homeExplorerDao.getImgByFolderList(fileTypeList, homeExplorerDTO.getSourceID());
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList();
        }
        for (HomeExplorerVO explorerVO : list){
            explorerVO.setPath(FileUtil.getShowImageUrl(explorerVO.getPath(),  explorerVO.getSourceID()+ "_" + explorerVO.getFileType() +".jpg"));
        }
        return list;
    }

    @Override
    public void deleteUserUploadRedis(CheckFileDTO updateFileDTO, LoginUser loginUser){
        String uploadUUidKey = GlobalConfig.uploadUUidKey + loginUser.getUserID();
        stringRedisTemplate.delete(uploadUUidKey);
        /*try {

            CloudOperateEnum operateEnum = CloudOperateEnum.getOperateEnum(updateFileDTO.getOperation());
            if (ObjectUtils.isEmpty(operateEnum)) {
                return;
            }
            long sId = 0;
            List<String> sourceIDList = new ArrayList<>();
            for (SourceOpDto data : updateFileDTO.getDataArr()) {
                if ("folder".equals(data.getType())) {
                    if (sId == 0){
                        sId = data.getSourceID();
                    }
                    sourceIDList.add(String.valueOf(data.getSourceID()));
                }
            }
            if (CollectionUtils.isEmpty(sourceIDList)){
                return;
            }
            String uploadUUidKey = GlobalConfig.uploadUUidKey + loginUser.getUserID();
            String uuid = stringRedisTemplate.opsForValue().get(uploadUUidKey);
            // 判断同一用户是否一直在上传
            if (ObjectUtils.isEmpty(uuid)){
                return;
            }
            CommonSource cs = ioSourceDao.getSourceInfo(sId);
            if (ObjectUtils.isEmpty(cs)){
                return;
            }
            LogUtil.info("删除上传缓存 cs=" + JsonUtils.beanToJson(cs));
            Long folderSourceId = cs.getParentID();
            String saveKey ="save:" ;
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            String key = uuid+"::{sourceID:"+folderSourceId+"}";

            List<String> values = operations.multiGet(saveKey+key, sourceIDList);
            if (!CollectionUtils.isEmpty(values)){

                for (String value : values){
                    operations.delete(key, value);
                }
                for (String id : sourceIDList){
                    operations.delete(key, id);
                }
                LogUtil.info("删除上传缓存 sourceIDList=" + JsonUtils.beanToJson(sourceIDList) + "，values=" + JsonUtils.beanToJson(values));
            }
        }catch (Exception e){
            LogUtil.error(e, "删除上传缓存失败");
        }*/
    }

    @Override
    public Long mkDir(AddCloudDirectoryDTO addCloudDirectoryDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(addCloudDirectoryDTO) || ObjectUtils.isEmpty(addCloudDirectoryDTO.getName())
                || ObjectUtils.isEmpty(addCloudDirectoryDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        CommonSource commonSource = ioSourceDao.getSourceInfo(addCloudDirectoryDTO.getSourceID());
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        String uuid = RandomUtil.getuuid();
        String uploadUUidKey = GlobalConfig.uploadUUidKey + loginUser.getUserID();
        // 判断同一用户是否一直在上传
        Boolean isSuccess = stringRedisTemplate.opsForValue().setIfAbsent(uploadUUidKey, uuid);
        if (!isSuccess) {
            String uid = stringRedisTemplate.opsForValue().get(uploadUUidKey);
            LogUtil.info("上传uuid  uuid= " + uuid + "，uid=" +uid);
            if (!ObjectUtils.isEmpty(uid)){
                uuid = uid;
            }
        }
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        List<String> nameList = Arrays.asList(addCloudDirectoryDTO.getName().split("/")).stream().filter(n->!ObjectUtils.isEmpty(n))
                .map(String::valueOf).collect(Collectors.toList());

        String saveKey ="save:" ;
        String key = uuid+"::{sourceID:"+addCloudDirectoryDTO.getSourceID()+"}";
        /*String subValue = key + "/" + nameList.get(0).trim();
        String pathSourerIdStr = operations.get(key, subValue);
        CommonSource s = null;
        if (!ObjectUtils.isEmpty(pathSourerIdStr) && !"0".equals(pathSourerIdStr)){
            // 如果缓存的sourceId被删除
            s = ioSourceDao.getSourceInfo(Long.valueOf(pathSourerIdStr));
            if (ObjectUtils.isEmpty(s)
                    || (!ObjectUtils.isEmpty(s.getIsDelete()) && s.getIsDelete().intValue() == 1)){
                Set<Object> keyList = stringRedisTemplate.opsForHash().keys(saveKey+ key);
                if (!CollectionUtils.isEmpty(keyList)) {
                    List<String> kList = new ArrayList<>();
                    for (Object o : keyList ){
                        LogUtil.info("deleteRedis o subValue=" + o);
                        kList.add(String.valueOf(o));
                    }
                    stringRedisTemplate.delete(kList);
                }
                stringRedisTemplate.opsForHash().getOperations().expire(saveKey+ key, 1, TimeUnit.MILLISECONDS);
                stringRedisTemplate.opsForHash().getOperations().expire(key, 1, TimeUnit.MILLISECONDS);
            }
        }
        */

        Long sourceId = 0L;
        sourceId = addDir(commonSource , nameList, sourceId, 0, loginUser, key, key, operations,saveKey);
        return sourceId.longValue() == 0 ? addCloudDirectoryDTO.getSourceID() : sourceId;
    }

    private Long addDir(CommonSource commonSource , List<String> nameList, Long sourceId, int i, LoginUser loginUser
            , String key, String value, HashOperations<String, String, String> operations, String saveKey){
        if (i >= nameList.size()){
            return sourceId;
        }
        String name = nameList.get(i);
        if (!ObjectUtils.isEmpty(name) && !ObjectUtils.isEmpty(name.trim())){

            String subValue = value + "/" + name.trim();
            String pathSourerIdStr = null;
            CommonSource source = null;
            // 同时多个请求创建时
            Boolean isSuccess = stringRedisTemplate.opsForValue().setIfAbsent(subValue, "1");

            LogUtil.info("查询文件夹是否正在创建：lockRedisKey="+ isSuccess + "，subValue="+subValue);
            if(isSuccess) { //若成功

                source = directoryUtil.createDirSource(name.trim(), loginUser, commonSource);
                sourceId = source.getSourceID();
                this.stringRedisTemplate.expire(subValue, 24, TimeUnit.HOURS);
            }else {
                int retryIndexCheck = 0;
                //失败重试30次
                while (retryIndexCheck < 300) {
                    pathSourerIdStr = operations.get(key, subValue);
                    retryIndexCheck++;
                    if (!ObjectUtils.isEmpty(pathSourerIdStr)) {
                        break;
                    }
                    //若失败，则等待2m后再重试
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!ObjectUtils.isEmpty(pathSourerIdStr)){
                    sourceId = Long.parseLong(pathSourerIdStr);
                    source = ioSourceDao.getSourceInfo(sourceId);
                }
            }
            /////////////////
            if (ObjectUtils.isEmpty(source)
                    || (!ObjectUtils.isEmpty(source.getIsDelete()) && source.getIsDelete().intValue() == 1)){
                source = directoryUtil.createDirSource(name.trim(), loginUser, commonSource);
                sourceId = source.getSourceID();
            }
            operations.put(saveKey+key, String.valueOf(sourceId), subValue);
            operations.put(key, subValue, String.valueOf(sourceId));
            operations.getOperations().expire(saveKey+key, 24, TimeUnit.HOURS);
            operations.getOperations().expire(key, 24, TimeUnit.HOURS);
            if (i < nameList.size()-1){
                return addDir(source, nameList, sourceId, i + 1, loginUser, key, subValue, operations, saveKey);
            }
        }else {
            return addDir(commonSource, nameList, sourceId, i + 1, loginUser, key, value, operations, saveKey);
        }
        return sourceId;
    }

    @Override
    public boolean setCover(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(homeExplorerDTO.getCoverId()) || homeExplorerDTO.getCoverId() < 1){
            homeExplorerDTO.setCoverId(0L);
        }
        try {
            ioSourceDao.updateCoverId(homeExplorerDTO.getSourceID(), homeExplorerDTO.getCoverId());
        } catch (Exception e) {
            LogUtil.error(e, " updateCoverId update error homeExplorerDTO=" + JsonUtils.beanToJson(homeExplorerDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        return true;
    }
    @Override
    public boolean setDesktopCover(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getCoverId()) || homeExplorerDTO.getCoverId() < 1){
            homeExplorerDTO.setCoverId(0L);
        }
        String key = "myDesktopCoverId";
        String value = homeExplorerDTO.getCoverId()+"";
        Long tenantId = loginUser.getTenantId();
        String desktopCoverIdValue = userOptionDao.getUserOtherConfigByKey(loginUser.getUserID(), "", key);
        if (!ObjectUtils.isEmpty(desktopCoverIdValue)) {
            try {
                userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), key, value);
            } catch (Exception e) {
                LogUtil.error(e, " setDesktopCover update error homeExplorerDTO=" + JsonUtils.beanToJson(homeExplorerDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }else {
            List<UserOption> paramList = new ArrayList<>();
            paramList.add(new UserOption(loginUser.getUserID(), "", key, value,tenantId));
            try {
                userOptionDao.batchInsert(paramList);
            } catch (Exception e) {
                LogUtil.error(e, " setDesktopCover insert error homeExplorerDTO=" + JsonUtils.beanToJson(homeExplorerDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }
        return true;
    }
}
