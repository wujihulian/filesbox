package com.svnlan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.DocumentTypeEnum;
import com.svnlan.enums.SortEnum;
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
import com.svnlan.utils.DateDUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
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
    UserDao userDao;
    @Resource
    IoFileDao ioFileDao;

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
            Date timeFrom = DateDUtil.strToDate("yyyy-MM-dd", convertDTO.getTimeFrom());
            map.put("minDate", timeFrom.getTime()/1000);
        }
        if (!ObjectUtils.isEmpty(convertDTO.getTimeTo())) {
            Date timeTo = DateDUtil.strToDate( "yyyy-MM-dd HH:mm:ss", convertDTO.getTimeTo() + " 23:59:59");
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
        PageHelper.startPage(convertDTO.getCurrentPage(), convertDTO.getPageSize());
        List<CommonConvertVo> list = this.commonConvertDao.getConvertList(map);

        if (CollectionUtils.isEmpty(list)) {
            return new PageResult(0L, new ArrayList());
        }
        Set<Long> uidList = new HashSet<>();
        list.forEach(vo->uidList.add(ObjectUtils.isEmpty(vo.getUserID()) ? vo.getCreateUser() : vo.getUserID()));


        List<UserVo> userList = userDao.getUserBaseInfo(new ArrayList<>(uidList));
        Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));

        UserVo userVo = null;
        for (CommonConvertVo vo : list){
            vo.setUserID(ObjectUtils.isEmpty(vo.getUserID()) ? vo.getCreateUser() : vo.getUserID());
            vo.setState(ObjectUtils.isEmpty(vo.getState()) ? "0" : vo.getState());
            vo.setConvertID(ObjectUtils.isEmpty(vo.getConvertID()) ? 0L : vo.getConvertID());
            vo.setFrequencyCount(ObjectUtils.isEmpty(vo.getFrequencyCount()) ? 0 : vo.getFrequencyCount());
            vo.setModifyTime(ObjectUtils.isEmpty(vo.getModifyTime()) ? 0L : vo.getModifyTime());
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
        PageInfo<CommonConvertVo> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setList(list);
        pageResult.setTotal(pageInfo.getTotal());
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
