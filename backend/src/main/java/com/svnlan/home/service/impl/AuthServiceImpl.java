package com.svnlan.home.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceAuthDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IoSourceAuth;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.AuthService;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.IoSourceAuthVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.UserGroupDao;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/11 17:17
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    IoSourceAuthDao ioSourceAuthDao;
    @Resource
    UserGroupDao userGroupDao;
    @Resource
    UserAuthTool userAuthTool;
    @Resource
    FileOptionTool fileOptionTool;

    @Override
    public void setAuth(CheckFileDTO checkFileDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceID()) || checkFileDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<IoSourceAuth> sourceAuthList = checkFileDTO.getSourceAuthList();

        if (!CollectionUtils.isEmpty(sourceAuthList)) {
            for (IoSourceAuth auth : sourceAuthList) {
                if (ObjectUtils.isEmpty(auth.getTargetType())
                        || ObjectUtils.isEmpty(auth.getTargetID())) {
                    LogUtil.error("setAuth 参数错误 param=" + JsonUtils.beanToJson(checkFileDTO));
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (ObjectUtils.isEmpty(auth.getAuthID())
                        && ObjectUtils.isEmpty(auth.getAuthDefine())) {
                    LogUtil.error("setAuth 参数错误 param=" + JsonUtils.beanToJson(checkFileDTO));
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                auth.setAuthID(ObjectUtils.isEmpty(auth.getAuthID()) ? 0 : auth.getAuthID());
                auth.setSourceID(checkFileDTO.getSourceID());
                auth.setAuthDefine(ObjectUtils.isEmpty(auth.getAuthDefine()) ? -1 : auth.getAuthDefine());
            }
        }

        CommonSource commonSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), "14", commonSource.getTargetType());

        ioSourceAuthDao.deleteSourceAuth(checkFileDTO.getSourceID());

        if (!CollectionUtils.isEmpty(sourceAuthList)){
            try {
                ioSourceAuthDao.batchInsert(sourceAuthList);
            }catch (Exception e){
                LogUtil.error(e, "setAuth error");
                throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
            }
        }

    }

    @Override
    public List<IoSourceAuthVo> getSourceAuth(CheckFileDTO checkFileDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getSourceID()) || checkFileDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<IoSourceAuthVo> list =  ioSourceAuthDao.getSourceAuthBySourceID(checkFileDTO.getSourceID());

        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }

        List<Long> userIdList = new ArrayList<>();
        List<Long> groupIDList = new ArrayList<>();
        for (IoSourceAuthVo n : list){
            if (!ObjectUtils.isEmpty(n.getTargetType()) && !ObjectUtils.isEmpty(n.getTargetID()) &&
                    n.getTargetType().intValue() == 1 && n.getTargetID() > 0){
                userIdList.add(n.getTargetID());
            }
            if (!ObjectUtils.isEmpty(n.getTargetType()) && !ObjectUtils.isEmpty(n.getParentID()) &&
                    n.getTargetType().intValue() == 2 && n.getParentID() > 0){
                groupIDList.add(n.getParentID());
            }
        }
        List<UserGroupVo> userGroupList = CollectionUtils.isEmpty(userIdList) ? null : userGroupDao.getGroupNameListByUserIds(userIdList);
        Map<Long, String > userMap = !CollectionUtils.isEmpty(userGroupList) ? userGroupList.stream().collect(Collectors.toMap(UserGroupVo::getUserID, UserGroupVo::getGroupName, (v1, v2) -> v2)) : null;

        List<IoSourceAuthVo> groupList = CollectionUtils.isEmpty(groupIDList) ? null : ioSourceAuthDao.getGroupNameListByGID(new ArrayList<>(groupIDList));
        Map<Long, String > groupMap = !CollectionUtils.isEmpty(groupList) ? groupList.stream().collect(Collectors.toMap(IoSourceAuthVo::getTargetID, IoSourceAuthVo::getParentGroupName, (v1, v2) -> v2)) : null;


        for (IoSourceAuthVo authVo : list){

            authVo.setNickname(ObjectUtils.isEmpty(authVo.getNickname()) ? "" : authVo.getNickname());
            authVo.setParentGroupName("");
            if (!ObjectUtils.isEmpty(authVo.getTargetType()) && authVo.getTargetType().intValue() == 1){
                if (!ObjectUtils.isEmpty(userMap) && userMap.containsKey(authVo.getTargetID())){
                    authVo.setParentGroupName(userMap.get(authVo.getTargetID()));
                }
            }else {
                if (!ObjectUtils.isEmpty(authVo.getParentID()) && !ObjectUtils.isEmpty(groupMap) && groupMap.containsKey(authVo.getParentID())){
                    authVo.setParentGroupName(groupMap.get(authVo.getParentID()));
                }
            }

        }
        return list;
    }
}
