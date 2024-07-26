package com.svnlan.user.service;

import com.svnlan.user.dto.SearchCourseDTO;
import com.svnlan.user.vo.TeachTypeTree;
import com.svnlan.utils.PageResult;

import java.util.List;

public interface ThirdSearchService {

    PageResult searchCourseList(String prefix, SearchCourseDTO optionDTO);
    List<TeachTypeTree> treeHasAll(String prefix, SearchCourseDTO optionDTO);
}
