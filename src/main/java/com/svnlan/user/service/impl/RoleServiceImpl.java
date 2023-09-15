package com.svnlan.user.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.RoleDao;
import com.svnlan.user.domain.Role;
import com.svnlan.user.dto.RoleDTO;
import com.svnlan.user.service.RoleService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:34
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleDao roleDao;

    @Override
    public List getRoleList(LoginUser loginUser, RoleDTO paramDto){
        paramDto.setRoleType(ObjectUtils.isEmpty(paramDto.getRoleType()) ? "1" : paramDto.getRoleType());
        paramDto.setEnable(ObjectUtils.isEmpty(paramDto.getEnable()) ? null : paramDto.getEnable());
        List<Role> roleList = roleDao.getRoleList(paramDto);
        return CollectionUtils.isEmpty(roleList) ? new ArrayList() : roleList;
    }

    @Override
    public  void editRole(String prefix, LoginUser loginUser, RoleDTO paramDto){
        /** 校验参数 */
        checkRoleParam(paramDto);

        /** 转换数据*/
        Role role = changeRoleParam(paramDto);

        /** 添加权限 */
        if (ObjectUtils.isEmpty(paramDto.getRoleID()) || paramDto.getRoleID() <= 0 ){
            addRole(prefix, role, paramDto);
        }else {
            updateRole(prefix, role, paramDto);
        }
    }

    public void updateRole(String prefix, Role role, RoleDTO paramDto){
        try {
            roleDao.update(role);
        }catch (Exception e){
            LogUtil.error(e, prefix + " updateRole error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }
    public void addRole(String prefix, Role role, RoleDTO paramDto){
        try {
            roleDao.insert(role);
        }catch (Exception e){
            LogUtil.error(e, prefix + " addRole error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }

    private Role changeRoleParam(RoleDTO paramDto){
        Role role = new Role();
        role.setRoleID(!ObjectUtils.isEmpty(paramDto.getRoleID()) ? paramDto.getRoleID() : null);
        role.setRoleName(paramDto.getRoleName());
        role.setDescription(ObjectUtils.isEmpty(paramDto.getDescription()) ? "" : paramDto.getDescription());
        role.setRoleType(paramDto.getRoleType());
        role.setCode("");
        role.setLabel(paramDto.getLabel());
        role.setAuth(ObjectUtils.isEmpty(paramDto.getAuth()) ? "" : paramDto.getAuth());
        role.setAdministrator(0);
        role.setIsSystem(0);
        role.setStatus(1);
        role.setEnable(paramDto.getEnable());
        role.setIgnoreExt("");
        role.setIgnoreFileSize(!ObjectUtils.isEmpty(paramDto.getIgnoreFileSize()) ? paramDto.getIgnoreFileSize() : 0);
        role.setIgnoreFileSize(role.getIgnoreFileSize() < 0 ? 0 : role.getIgnoreFileSize());

        Integer sort = roleDao.getMaxRoleSort(paramDto.getRoleType());
        role.setSort(ObjectUtils.isEmpty(sort) ? 0 : sort + 1);
        return role;
    }

    private void checkRoleParam(RoleDTO paramDto){
        //必填项目
        if (ObjectUtils.isEmpty(paramDto.getRoleName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerNotNull.getCode());
        }
        if (!ObjectUtils.isEmpty(paramDto.getRoleName()) && paramDto.getRoleName().length() > 32){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(paramDto.getLabel())){
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerNotNull.getCode());
        }
        if (!ObjectUtils.isEmpty(paramDto.getDescription()) && paramDto.getDescription().length() > 128){
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerNotNull.getCode());
        }

    }

    @Override
    public void setRoleStatus(String prefix, LoginUser loginUser, RoleDTO paramDto){
        if (ObjectUtils.isEmpty(paramDto.getRoleID()) || ObjectUtils.isEmpty(paramDto.getStatus()) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList(1,2).contains(paramDto.getStatus())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            roleDao.updateRoleState(paramDto);
        }catch (Exception e){
            LogUtil.error(e, prefix + " 删除权限  error" + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }
    @Override
    public void setRoleSort(String prefix, LoginUser loginUser, RoleDTO paramDto){
        if (ObjectUtils.isEmpty(paramDto.getIds())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<Integer> roleIds = Arrays.asList(paramDto.getIds().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());

        int sort = 0;
        List<Role> roleList = new ArrayList<>();
        for (Integer id : roleIds){
            roleList.add(new Role(id, sort));
            sort ++ ;
        }
        try {
            roleDao.updateRoleSort(roleList, paramDto.getRoleType());
        }catch (Exception e){
            LogUtil.error(e, prefix + " 权限排序 error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }
}
