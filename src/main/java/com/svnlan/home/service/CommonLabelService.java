package com.svnlan.home.service;

import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.LabelDto;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jwt.domain.LoginUser;

import java.util.List;
import java.util.Map;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/2 10:53
 */
public interface CommonLabelService {

    void addTag(LabelDto labelDto, LoginUser loginUser);
    void editTag(LabelDto labelDto, LoginUser loginUser);
    void delTag(LabelDto labelDto, LoginUser loginUser);
    List<CommonLabelVo> getTagList(LabelDto labelDto, LoginUser loginUser);
    void fileTag(LabelDto labelDto, LoginUser loginUser);


    boolean moveTop(CheckFileDTO labelDto, Map<String, Object> resultMap, LoginUser loginUser);
    boolean moveBottom(CheckFileDTO labelDto, Map<String, Object> resultMap, LoginUser loginUser);


    boolean tagTop(LabelDto labelDto, LoginUser loginUser);
    boolean tagBottom(LabelDto labelDto, LoginUser loginUser);
    List<CommonLabelVo> getInfoLabelList(LabelDto labelDto, LoginUser loginUser);
}
