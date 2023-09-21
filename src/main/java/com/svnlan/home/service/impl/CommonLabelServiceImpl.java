package com.svnlan.home.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.CommonLabelDao;
import com.svnlan.home.domain.CommonLabel;
import com.svnlan.home.domain.UserFav;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.LabelDto;
import com.svnlan.home.service.CommonLabelService;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/2 10:53
 */
@Service
public class CommonLabelServiceImpl implements CommonLabelService {

    @Resource
    CommonLabelDao commonLabelDao;
    @Resource
    UserFavDao userFavDao;


    @Override
    public void addTag(LabelDto labelDto, LoginUser loginUser){

        if (ObjectUtils.isEmpty(labelDto) || ObjectUtils.isEmpty(labelDto.getLabelName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (labelDto.getLabelName().trim().length() > 32){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        Integer tagType = !ObjectUtils.isEmpty(labelDto.getTagType()) ? labelDto.getTagType() : 1;

        Long userID = loginUser.getUserID();
        if (2 == tagType){
            userID = 0L;
        }

        List<Long> repeatList = commonLabelDao.checkLabelNameRepeat(labelDto.getLabelName().trim(), userID, tagType);
        if (!CollectionUtils.isEmpty(repeatList)){
            throw new SvnlanRuntimeException(CodeMessageEnum.repeatError.getCode());
        }

        CommonLabel commonLabel = new CommonLabel();
        commonLabel.setLabelName(labelDto.getLabelName().trim());
        commonLabel.setLabelEnName(ChinesUtil.getPingYin(labelDto.getLabelName()));
        commonLabel.setEnNameSimple(ChinesUtil.getFirstSpell(labelDto.getLabelName()));
        commonLabel.setUserID(2 == tagType ? 0 : loginUser.getUserID());
        commonLabel.setStyle(ObjectUtils.isEmpty(labelDto.getStyle()) ? "" : labelDto.getStyle());
        commonLabel.setSort(0);
        commonLabel.setTagType(tagType);
        try {
            commonLabelDao.insert(commonLabel);
        }catch (Exception e){
            LogUtil.error(e, "添加标签失败， dto=" + JsonUtils.beanToJson(labelDto) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }
    @Override
    public void editTag(LabelDto labelDto, LoginUser loginUser){
        if (ObjectUtils.isEmpty(labelDto) || ObjectUtils.isEmpty(labelDto.getLabelId()) || ObjectUtils.isEmpty(labelDto.getLabelName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (labelDto.getLabelName().trim().length() > 32){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Integer tagType = !ObjectUtils.isEmpty(labelDto.getTagType()) ? labelDto.getTagType() : 1;

        Long userID = loginUser.getUserID();
        if (2 == tagType){
            userID = 0L;
        }
        List<Long> repeatList = commonLabelDao.checkLabelNameRepeat(labelDto.getLabelName().trim(), userID, tagType);
        if (!CollectionUtils.isEmpty(repeatList)){
            if (repeatList.size() == 1 && repeatList.contains(labelDto.getLabelId())) {
                // 排除
            }else {
                throw new SvnlanRuntimeException(CodeMessageEnum.repeatError.getCode());
            }
        }

        CommonLabel commonLabel = new CommonLabel();
        commonLabel.setLabelId(labelDto.getLabelId());
        commonLabel.setLabelName(labelDto.getLabelName().trim());
        commonLabel.setLabelEnName(ChinesUtil.getPingYin(labelDto.getLabelName()));
        commonLabel.setEnNameSimple(ChinesUtil.getFirstSpell(labelDto.getLabelName()));
        commonLabel.setStyle(ObjectUtils.isEmpty(labelDto.getStyle()) ? "" : labelDto.getStyle());
        try {
            commonLabelDao.update(commonLabel);
        }catch (Exception e){
            LogUtil.error(e, "编辑标签失败， dto=" + JsonUtils.beanToJson(labelDto) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }

    @Override
    public List<CommonLabelVo> getTagList(LabelDto labelDto, LoginUser loginUser){
        List<CommonLabelVo> list = commonLabelDao.getUserLabelList(loginUser.getUserID());
        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;
    }

    @Override
    public void fileTag(LabelDto labelDto, LoginUser loginUser){
        if (ObjectUtils.isEmpty(labelDto.getLabelId()) || ObjectUtils.isEmpty(labelDto.getFiles()) || ObjectUtils.isEmpty(labelDto.getBlock())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> files = Arrays.asList(labelDto.getFiles().split(",")).stream().map(String::valueOf).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(files)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if ("add".equals(labelDto.getBlock())){
            List<UserFav> list = new ArrayList<>();
            UserFav userFav = null;
            Integer sort = userFavDao.getFavMaxSort(loginUser.getUserID());
            sort = ObjectUtils.isEmpty(sort) ? 0 : sort;
            int i = 1;
            for (String sourseID : files){

                userFav = new UserFav(loginUser.getUserID(), labelDto.getLabelId().intValue(), "", sourseID, "source", sort + i);
                i ++ ;
                list.add(userFav);
            }
            try {
                userFavDao.batchInsert(list);
            }catch (Exception e){
                LogUtil.error(e, " 关联标签失败");
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }else {
            try {
                userFavDao.removeFileTag(labelDto.getLabelId().intValue(), files);
            }catch (Exception e){
                LogUtil.error(e, " 取消关联标签失败");
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }
    }
    @Override
    public void delTag(LabelDto labelDto, LoginUser loginUser){
        if (ObjectUtils.isEmpty(labelDto) || ObjectUtils.isEmpty(labelDto.getLabelId())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            commonLabelDao.deleteTag(labelDto.getLabelId());

            if (!ObjectUtils.isEmpty(labelDto.getTagType()) && 2 == labelDto.getTagType()) {
                // 删除资讯关联关系
                userFavDao.removeInfoTag(labelDto.getLabelId().intValue());
            }else {
                userFavDao.removeUserTag(labelDto.getLabelId().intValue(), loginUser.getUserID());
            }
        }catch (Exception e){
            LogUtil.error(e, "编辑标签失败， dto=" + JsonUtils.beanToJson(labelDto) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }

    @Override
    public boolean moveTop(CheckFileDTO labelDto, Map<String, Object> resultMap, LoginUser loginUser) {

        if (ObjectUtils.isEmpty(labelDto.getSourceID()) || labelDto.getSourceID() <=0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Integer sort = userFavDao.getTagMaxSort(loginUser.getUserID());
        sort = ObjectUtils.isEmpty(sort) ? 1 : sort;
        try {
            // 先减所有再修改当前sourceID
            userFavDao.subtractTagSortAll(loginUser.getUserID());
            userFavDao.updateTagSort(loginUser.getUserID(), labelDto.getSourceID().toString(), sort);
        } catch (Exception e){
            LogUtil.error(e, "标签内的文件置顶失败" + JsonUtils.beanToJson(labelDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public boolean moveBottom(CheckFileDTO labelDto, Map<String, Object> resultMap, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(labelDto.getSourceID()) || labelDto.getSourceID() <=0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            // 先加所有再修改当前sourceID
            userFavDao.addTagSortAll(loginUser.getUserID());
            userFavDao.updateTagSort(loginUser.getUserID(), labelDto.getSourceID().toString(), 0);
        } catch (Exception e){
            LogUtil.error(e, "标签内的文件置底失败" + JsonUtils.beanToJson(labelDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public boolean tagTop(LabelDto labelDto, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(labelDto.getTagID()) || labelDto.getTagID() <=0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Integer tagType = !ObjectUtils.isEmpty(labelDto.getTagType()) ? labelDto.getTagType() : 1;
        Integer sort = commonLabelDao.getMaxSort(loginUser.getUserID(), tagType);
        sort = ObjectUtils.isEmpty(sort) ? 1 : sort + 1;
        try {
            commonLabelDao.updateSort(labelDto.getTagID(), sort);
        } catch (Exception e){
            LogUtil.error(e, "标签置顶失败" + JsonUtils.beanToJson(labelDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public boolean tagBottom(LabelDto labelDto, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(labelDto.getTagID()) || labelDto.getTagID() <=0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            commonLabelDao.updateSort(labelDto.getTagID(), 0);
        } catch (Exception e){
            LogUtil.error(e, "标签置底失败" + JsonUtils.beanToJson(labelDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return true;
    }

    @Override
    public List<CommonLabelVo> getInfoLabelList(LabelDto labelDto, LoginUser loginUser){
        List<CommonLabelVo> list = commonLabelDao.getInfoLabelList(loginUser.getUserID());
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        list.forEach(n->{
            n.setLabelColor(n.getStyle());
            n.setStyle(null);
        });
        return list;
    }
}
