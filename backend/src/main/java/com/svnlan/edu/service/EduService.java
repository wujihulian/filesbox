package com.svnlan.edu.service;

import com.svnlan.edu.domain.CatalogueResult;
import com.svnlan.edu.domain.EduInfo;
import com.svnlan.edu.domain.EduType;
import com.svnlan.edu.dto.EduDto;
import com.svnlan.utils.PageResult;

import java.util.List;

public interface EduService {

    List<EduType> getTeachTypeTree();
    List<EduType> getTeachTypeTreeAll();
    CatalogueResult catalogue(EduDto eduDto);
    PageResult searchCourseList(EduDto eduDto);
    PageResult courseCateList(EduDto eduDto);
    PageResult tagCourseList(EduDto eduDto);
}
