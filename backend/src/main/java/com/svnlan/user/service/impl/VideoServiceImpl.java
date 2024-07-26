package com.svnlan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.DocumentTypeEnum;
import com.svnlan.enums.SourceFieldEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.CommonConvertDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dto.CommonConvertDTO;
import com.svnlan.user.dto.ConvertListDTO;
import com.svnlan.user.service.VideoService;
import com.svnlan.user.vo.CommonConvertVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 15:00
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    CommonConvertDao commonConvertDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    UserDao userDaoImpl;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    TenantUtil tenantUtil;

    @Override
    public PageResult getConvertList(LoginUser loginUser, ConvertListDTO convertDTO){
        Map<String, Object> map = new HashMap<>(2);
        if (!ObjectUtils.isEmpty(convertDTO.getKeyword())) {
            map.put("keyword", convertDTO.getKeyword().toLowerCase());
        }
        if (!ObjectUtils.isEmpty(convertDTO.getState())) {
            map.put("state", convertDTO.getState());
        }
        if (!ObjectUtils.isEmpty(convertDTO.getSourceID())) {
            map.put("sourceID", convertDTO.getSourceID());
        }
        if (!ObjectUtils.isEmpty(convertDTO.getUserID())) {
            map.put("userID", convertDTO.getUserID());
        }
        if (!ObjectUtils.isEmpty(convertDTO.getTimeFrom())) {
            Date timeFrom = DateUtil.strToDate("yyyy-MM-dd", convertDTO.getTimeFrom());
            map.put("minDate", timeFrom.getTime()/1000);
        }
        if (!ObjectUtils.isEmpty(convertDTO.getTimeTo())) {
            Date timeTo = DateUtil.strToDate( "yyyy-MM-dd HH:mm:ss", convertDTO.getTimeTo() + " 23:59:59");
            map.put("maxDate", timeTo.getTime()/1000);
        }

        String sortType = "asc";
        if (!ObjectUtils.isEmpty(convertDTO.getSortType()) && !Arrays.asList("asc","up").contains(convertDTO.getSortType())){
            sortType = "desc";
        }
        map.put("sortType", sortType);
        map.put("sortField", SourceFieldEnum.getConvertSortField(convertDTO.getSortField()));
        map.put("fileTypeList", Arrays.asList(DocumentTypeEnum.movie.getExt().split(",")).stream().map(String::valueOf).collect(Collectors.toList()));
        LogUtil.info("getConvertList  map=" + JsonUtils.beanToJson(map));
        //
//        PageHelper.startPage(convertDTO.getCurrentPage(), convertDTO.getPageSize());
        Long total = this.commonConvertDao.getConvertListCount(map);

        List<CommonConvertVo> list = null;
        if (0 < total) {
            map.put("startIndex", convertDTO.getStartIndex());
            map.put("pageSize", convertDTO.getPageSize());
            list = this.commonConvertDao.getConvertList(map);
        }

        if (CollectionUtils.isEmpty(list)) {
            return new PageResult(0L, new ArrayList());
        }
        Set<Long> uidList = new HashSet<>();
        list.forEach(vo->uidList.add(ObjectUtils.isEmpty(vo.getUserID()) ? vo.getCreateUser() : vo.getUserID()));

        LocalDateTime end = LocalDateTime.now();
        List<UserVo> userList = userDaoImpl.getUserBaseInfo(new ArrayList<>(uidList));
        Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));

        UserVo userVo = null;
        Set<Long> sourceIds = new HashSet<>();
        for (CommonConvertVo vo : list){
            vo.setUserID(ObjectUtils.isEmpty(vo.getUserID()) ? vo.getCreateUser() : vo.getUserID());
            vo.setState(ObjectUtils.isEmpty(vo.getState()) ? "0" : vo.getState());
            vo.setConvertID(ObjectUtils.isEmpty(vo.getConvertID()) ? 0L : vo.getConvertID());
            vo.setFrequencyCount(ObjectUtils.isEmpty(vo.getFrequencyCount()) ? 0 : vo.getFrequencyCount());
            if (!ObjectUtils.isEmpty(vo.getModifyTime()) && "0".equals(vo.getState())){
                // 获取转码中状态大于24小时的视频id
                long t = DateUtil.getTimeDiff(LocalDateTime.ofInstant(Instant.ofEpochMilli(vo.getModifyTime()), ZoneId.systemDefault()), end);
                if (t >= 1440){
                    sourceIds.add(vo.getSourceID());
                }
            }

            vo.setCreateTime(ObjectUtils.isEmpty(vo.getCreateTime()) ? 0L : vo.getCreateTime() / 1000);
            vo.setModifyTime(ObjectUtils.isEmpty(vo.getModifyTime()) ? 0L : vo.getModifyTime() / 1000);
            vo.setScheduleTime(ObjectUtils.isEmpty(vo.getScheduleTime()) ? 0L : vo.getScheduleTime() / 1000);
            vo.setNickname("");
            vo.setAvatar("");
            if (!ObjectUtils.isEmpty(userMap) && userMap.containsKey(vo.getUserID())) {
                userVo = userMap.get(vo.getUserID());
                vo.setNickname(ObjectUtils.isEmpty(userVo.getNickname()) ? userVo.getName() : userVo.getNickname());
                vo.setAvatar(ObjectUtils.isEmpty(userVo.getAvatar()) ? "" : userVo.getAvatar());
            }
            if (vo.getSize() <= 0 && vo.getFileSize() > 0){
                vo.setSize(vo.getFileSize());
            }
        }

        // 转码中状态大于24小时则更改状态为转码失败
        if (!CollectionUtils.isEmpty(sourceIds)){
            long tenantId = tenantUtil.getTenantIdByServerName();
            String videoRedisExecRedisKey = GlobalConfig.videoRedisExecRedisKey + tenantId;
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            // 查询转码中的数据
            Set<String> keySet2 = operations.keys(videoRedisExecRedisKey);
            Set<Long> sourceIdSets = null;
            if (!CollectionUtils.isEmpty(keySet2)){
                sourceIdSets = new HashSet<>();
                for (Long sourceId : sourceIds){
                    if (!keySet2.contains(String.valueOf(sourceId))){
                        sourceIdSets.add(sourceId);
                    }
                }
            }else {
                sourceIdSets = sourceIds;
            }

            if (!CollectionUtils.isEmpty(sourceIdSets)){
                for (CommonConvertVo vo : list){
                    if (sourceIdSets.contains(vo.getSourceID())){
                        vo.setState("2");
                        vo.setStateSort(0);
                        vo.setRemark("执行中断，请手动点击重新转码");
                    }
                }
            }
        }
        PageResult pageResult = new PageResult();
        pageResult.setList(list);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    public boolean removeConvert(String prefix, LoginUser loginUser, CommonConvertDTO convertDTO){

        if (ObjectUtils.isEmpty(convertDTO.getConvertID()) || convertDTO.getConvertID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            commonConvertDao.updateStatus(convertDTO.getConvertID(), convertDTO.getState());
        }catch (Exception e){
            LogUtil.error(e, prefix + " 删除失败");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public boolean removeVideoFilePath(String prefix, LoginUser loginUser, CommonConvertDTO convertDTO){

        if (ObjectUtils.isEmpty(convertDTO.getSourceID()) || convertDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        CommonSource source = ioSourceDao.getSourceInfo(convertDTO.getSourceID());

        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(source.getFileType())
                || !Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(source.getFileType())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(source.getPath())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        if (!ObjectUtils.isEmpty(source.getPath())){
            try {
                //删除文件
                new File(source.getPath()).delete();
                // 修改状态
                ioFileDao.deleteFileOrgPath(source.getFileID());
            } catch (Exception e){
                LogUtil.error(e, prefix + " 删除原文件失败");
                throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
            }
        }
        return true;
    }
}
