package com.svnlan.user.service.impl;

import com.svnlan.manage.vo.CommonInfoVo;
import com.svnlan.user.dto.ThirdDataResult;
import com.svnlan.user.dto.ThirdSearchResult;
import com.svnlan.user.tools.ThirdSearchUtils;
import com.svnlan.user.dto.SearchCourseDTO;
import com.svnlan.user.service.ThirdSearchService;
import com.svnlan.user.vo.SearchCourseVo;
import com.svnlan.user.vo.TeachTypeTree;
import com.svnlan.user.vo.ThirdSearchVo;
import com.svnlan.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThirdSearchServiceImpl implements ThirdSearchService {
    @Resource
    ThirdSearchUtils thirdSearchUtils;

    @Override
    public PageResult searchCourseList(String prefix, SearchCourseDTO optionDTO){
        Assert.notNull(optionDTO.getKeyword(), "关键词不能为空");
        optionDTO.setSearchType(ObjectUtils.isEmpty(optionDTO.getSearchType()) ? "0": optionDTO.getSearchType());

        PageResult<SearchCourseVo> pageResult = new PageResult<>();
        ThirdSearchResult result = thirdSearchUtils.searchCourseList(optionDTO);
        pageResult.setHasNextPage(result.getHasNext());
        pageResult.setTotal(result.getTotal());
        pageResult.setList(null);
        if (!ObjectUtils.isEmpty(result.getData())) {
            ThirdSearchVo vo = (ThirdSearchVo) result.getData();
            pageResult.setList(vo.getData());
        }
        return pageResult;
    }

    @Override
    public List<TeachTypeTree> treeHasAll(String prefix, SearchCourseDTO optionDTO){

        ThirdDataResult result = thirdSearchUtils.teachTypeTreeHasAll();
        List<TeachTypeTree> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(result.getData())) {
            list = (List<TeachTypeTree>) result.getData();
        }
        return list;
    }
}
