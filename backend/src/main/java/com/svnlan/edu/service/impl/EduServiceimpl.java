package com.svnlan.edu.service.impl;

import com.svnlan.edu.domain.CatalogueResult;
import com.svnlan.edu.domain.EduInfo;
import com.svnlan.edu.domain.EduType;
import com.svnlan.edu.dto.EduDto;
import com.svnlan.edu.service.EduService;
import com.svnlan.edu.tool.EduInfoUtils;
import com.svnlan.utils.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EduServiceimpl implements EduService {

    @Resource
    EduInfoUtils eduInfoUtils;

    @Override
    public List<EduType> getTeachTypeTree(){
        return eduInfoUtils.getTeachTypeTree();
    }

    @Override
    public List<EduType> getTeachTypeTreeAll(){
        return eduInfoUtils.getTeachTypeTreeAll();
    }

    @Override
    public CatalogueResult catalogue(EduDto eduDto){
        CatalogueResult re= new CatalogueResult();
        List<EduInfo> list =  eduInfoUtils.catalogue(eduDto);
        re.setCatalogueList(list);
        re.setEditionName(eduDto.getEditionName());
        re.setVolumeName(eduDto.getVolumeName());
        re.setSubjectName(eduDto.getSubjectName());
        re.setGradeName(eduDto.getGradeName());
        re.setPeriodName(eduDto.getPeriodName());
        return re;
    }

    @Override
    public PageResult searchCourseList(EduDto eduDto){
        return eduInfoUtils.searchCourseList(eduDto);
    }
    @Override
    public PageResult courseCateList(EduDto eduDto){
        return eduInfoUtils.courseCateList(eduDto);
    }

    @Override
    public PageResult tagCourseList(EduDto eduDto){
        return eduInfoUtils.tagCourseList(eduDto);
    }
}
