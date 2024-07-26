package com.svnlan.manage.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dao.CommonInfoTypeDao;
import com.svnlan.manage.domain.CommonInfoType;
import com.svnlan.manage.dto.CommonInfoTypeDto;
import com.svnlan.manage.service.CommonInfoTypeService;
import com.svnlan.manage.utils.ValidateDUtil;
import com.svnlan.manage.vo.CommonInfoTypeVo;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 16:54
 */
@Service
public class CommonInfoTypeServiceImpl implements CommonInfoTypeService {

    @Resource
    CommonInfoTypeDao commonInfoTypeDao;

    @Override
    public List getInfoTypeList(CommonInfoTypeDto infoTypeDto, LoginUser loginUser){
        List<CommonInfoTypeVo> list = null;
        Map<String, Object> paramMap = new HashMap<>(1);
        if (!ObjectUtils.isEmpty(infoTypeDto.getKeyword())){
            paramMap.put("keyword", infoTypeDto.getKeyword());
        }
        if (!ObjectUtils.isEmpty(infoTypeDto.getShowCount()) && 1 == infoTypeDto.getShowCount()){
            list = commonInfoTypeDao.selectListAndCountByParam(paramMap);
        }else {
            list = commonInfoTypeDao.selectListByParam(paramMap);
        }
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList();
        }

        Map<String, CommonInfoTypeVo> appender = new HashMap<>();
        //遍历一级菜单
        for (CommonInfoTypeVo infoTypeVo : list) {
            if (CollectionUtils.isEmpty(infoTypeVo.getChildren())) {
                infoTypeVo.setChildren(new ArrayList<>());
            }
            addNodes(appender, infoTypeVo.getParentID().toString(), infoTypeVo);
            appender.put(infoTypeVo.getInfoTypeID().toString(), infoTypeVo);
        }

        CommonInfoTypeVo node = (appender.get(""));
        if (CollectionUtils.isEmpty(node.getChildren())){
            return new ArrayList();
        }
        return node.getChildren();
    }

    private void addNodes(Map<String, CommonInfoTypeVo> appender, String parentIDStr, CommonInfoTypeVo node) {
        if (appender.containsKey(parentIDStr)) {
            appender.get(parentIDStr).getChildren().add(node);
            appender.get(parentIDStr).getChildren().stream().sorted(Comparator.comparing(CommonInfoTypeVo::getSort).thenComparing(CommonInfoTypeVo::getCreateTime).reversed()).collect(Collectors.toList());
        } else {
            // 根节点
            CommonInfoTypeVo nodeRoot = new CommonInfoTypeVo(node.getParentID(), node.getParentLevel(), new ArrayList<>());
            nodeRoot.getChildren().add(node);
            appender.put("", nodeRoot);
            appender.put(parentIDStr, nodeRoot);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveInfoType(CommonInfoTypeDto commonInfoTypeDto, LoginUser loginUser) {

        int id = 0;
        //资讯类别对象
        CommonInfoType commonInfoType = new CommonInfoType();
        //新增资讯的上级id
        Integer parentID = ObjectUtils.isEmpty(commonInfoTypeDto.getParentID()) ? 0 : commonInfoTypeDto.getParentID();
        String parentLevel = null;
        if (parentID > 0){
            CommonInfoType infoType = commonInfoTypeDao.selectById(parentID);
            if (ObjectUtils.isEmpty(infoType)){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            parentLevel = this.setParentLevel(infoType);
        }else {
            parentLevel = ",0,";
        }
        //校验类别名称
        ValidateDUtil.validateTextLen(commonInfoTypeDto.getTypeName(), "typeName", 1, 10);
        String typeName = commonInfoTypeDto.getTypeName().trim();
        // 校验名称是否重复
        List<CommonInfoType> checkList = commonInfoTypeDao.checkNameIsExist(typeName);
        if (!CollectionUtils.isEmpty(checkList)){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathExists.getCode());
        }


        int sort = 0;
        commonInfoType.setTypeName(typeName);
        commonInfoType.setParentID(parentID);
        commonInfoType.setParentLevel(parentLevel);
        commonInfoType.setStatus(1);
        commonInfoType.setSort(sort);
        commonInfoType.setCreateUser(loginUser.getUserID());
        commonInfoType.setNamePinyin(ChinesUtil.getPingYin(typeName));
        commonInfoType.setNamePinyinSimple(ChinesUtil.getFirstSpell(typeName));
        commonInfoTypeDao.insert(commonInfoType);
        if(null != commonInfoType && !ObjectUtils.isEmpty(commonInfoType.getInfoTypeID())) {
            id = commonInfoType.getInfoTypeID();
        }
        return id;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editInfoType(CommonInfoTypeDto commonInfoTypeDto, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(commonInfoTypeDto.getInfoTypeID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        ValidateDUtil.validateTextLen(commonInfoTypeDto.getTypeName(), "typeName", 1, 10);

        String typeName = commonInfoTypeDto.getTypeName().trim();
        // 校验名称是否重复
        List<CommonInfoType> checkList = commonInfoTypeDao.checkNameIsExist(typeName);
        if (!CollectionUtils.isEmpty(checkList)){
            for (CommonInfoType checkVo : checkList){
                if (checkVo.getInfoTypeID().intValue() != commonInfoTypeDto.getInfoTypeID().intValue()){
                    throw new SvnlanRuntimeException(CodeMessageEnum.pathExists.getCode());
                }
            }
        }

        List<CommonInfoType> copyList = null;
        String parentLevel = "";
        CommonInfoType commonInfoType = commonInfoTypeDao.selectById(commonInfoTypeDto.getInfoTypeID());
        String oldParentLevel = commonInfoType.getParentLevel();
        commonInfoType.setInfoTypeID(commonInfoTypeDto.getInfoTypeID());
        commonInfoType.setTypeName(typeName);
        commonInfoType.setNamePinyin(ChinesUtil.getPingYin(typeName));
        commonInfoType.setNamePinyinSimple(ChinesUtil.getFirstSpell(typeName));
        if (!ObjectUtils.isEmpty(commonInfoTypeDto.getParentID())
                && commonInfoType.getParentID().intValue() != commonInfoTypeDto.getParentID().intValue()){
            // 修改层级
            commonInfoType.setParentID(commonInfoTypeDto.getParentID());
            CommonInfoType parentInfoType = commonInfoTypeDao.selectById(commonInfoTypeDto.getParentID());
            parentLevel = this.setParentLevel(parentInfoType);
            commonInfoType.setParentLevel(parentLevel);
        }
        try {
            commonInfoTypeDao.updateById(commonInfoType);
        }catch (Exception e){
            LogUtil.error(e, "保存资讯类型失败！");
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        if (!ObjectUtils.isEmpty(parentLevel)) {
            copyList = commonInfoTypeDao.copyInfoTypeListByLevel(parentLevel);
            if (!CollectionUtils.isEmpty(copyList)) {
                for (CommonInfoType type : copyList) {
                    type.setParentLevel(type.getParentLevel().replace(oldParentLevel, parentLevel));
                }
                try {
                    commonInfoTypeDao.batchUpdateParent(copyList);
                }catch (Exception e){
                    LogUtil.error(e, "保存资讯更新上级失败！");
                }
            }
        }

        return commonInfoTypeDto.getInfoTypeID();
    }


    private String setParentLevel(CommonInfoType parentInfoType){
        return parentInfoType.getParentLevel() + parentInfoType.getInfoTypeID()+ "," ;
    }
    private String setParentLevel(String level, Integer id){
        return level  + id + ",";
    }

    @Override
    public void deleteInfoType(CommonInfoTypeDto commonInfoTypeDto, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(commonInfoTypeDto.getInfoTypeID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 分类下有东西不可删除
        CommonInfoType commonInfoType = new CommonInfoType();
        commonInfoType.setInfoTypeID(commonInfoTypeDto.getInfoTypeID());
        commonInfoType.setStatus(0);


        CommonInfoType infoType =  commonInfoTypeDao.selectById(commonInfoTypeDto.getInfoTypeID());
        if (ObjectUtils.isEmpty(infoType)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List num =  commonInfoTypeDao.checkChild(this.setParentLevel(infoType), commonInfoTypeDto.getInfoTypeID());
        if (!CollectionUtils.isEmpty(num) && num.size() > 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.infoTypeDelError.getCode());
        }

        try {
            commonInfoTypeDao.updateStatusById(commonInfoType);
        }catch (Exception e){
            LogUtil.error(e, "删除失败！");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
    }
}
