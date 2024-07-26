package com.svnlan.edu.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.svnlan.edu.domain.Course;
import com.svnlan.edu.domain.EduInfo;
import com.svnlan.edu.domain.EduType;
import com.svnlan.edu.dto.EduDto;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PageResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * 之江汇文档
 */
@Component
public class EduInfoUtils {

    @Resource
    RestTemplate restTemplate;

    /**
     * 1.通用-教学分类树 不包含全部选项
     * https://uc.zjedu.com/api/base/eduInfo/teachTypeTree
     * @return
     */
    public List<EduType> getTeachTypeTree(){
        List<EduType> list = null;
        Map<String, Object> paramMap = new HashMap<>();
        try {
            String result = restTemplate.postForEntity(EduHostConfig.teachTypeTree_host, paramMap, String.class).getBody();
            LogUtil.info("getTeachTypeTree result=" + result);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("0".equals(jsonObject.getString("code"))){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.size() > 0) {
                        list = jsonArray.toJavaList(EduType.class);
                    }
                }else {
                    LogUtil.error("getTeachTypeTree 查询数据异常 result=" + result);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "通用-教学分类树 获取失败");
        }

        return list;
    }
    /**
     * 搜索页-教学分类树 每个分类下包含全部
     * https://uc.zjedu.com/api/base/eduInfo/teachType/treeHasAll
     * @return
     */
    public List<EduType> getTeachTypeTreeAll(){
        List<EduType> list = null;
        Map<String, Object> paramMap = new HashMap<>();
        try {
            String result = restTemplate.postForEntity(EduHostConfig.teachTypeTreeAll_host, paramMap, String.class).getBody();
            LogUtil.info("getTeachTypeTreeAll result=" + result);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("0".equals(jsonObject.getString("code"))){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.size() > 0) {
                        list = jsonArray.toJavaList(EduType.class);
                    }
                }else {
                    LogUtil.error("getTeachTypeTreeAll 查询数据异常 result=" + result);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "getTeachTypeTreeAll 搜索页-教学分类树 获取失败");
        }
        return list;
    }


    /**
     * 2.学科课程-目录列表
     * https://uc.zjedu.com/api/base/eduInfo/catalogue
     * @return
     */
    public List<EduInfo> catalogue(EduDto eduDto){
        List<EduInfo> list = null;

        StringBuilder sb = new StringBuilder();
        sb.append("?periodId=" + (!ObjectUtils.isEmpty(eduDto.getPeriodId()) ? eduDto.getPeriodId() : 0));
        sb.append("&gradeId=" + (!ObjectUtils.isEmpty(eduDto.getGradeId()) ? eduDto.getGradeId() : 0));
        sb.append("&subjectId=" + (!ObjectUtils.isEmpty(eduDto.getSubjectId()) ? eduDto.getSubjectId() : 0));
        sb.append("&editionId=" + (!ObjectUtils.isEmpty(eduDto.getEditionId()) ? eduDto.getEditionId() : 0));
        sb.append("&volumeId=" + (!ObjectUtils.isEmpty(eduDto.getVolumeId()) ? eduDto.getVolumeId() : 0));

        try {
            String result = restTemplate.postForEntity(EduHostConfig.catalogue_host + sb.toString(), null, String.class).getBody();
            LogUtil.info("catalogue result=" + result);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("0".equals(jsonObject.getString("code"))){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.size() > 0) {
                        list = jsonArray.toJavaList(EduInfo.class);
                    }
                }else {
                    LogUtil.error("catalogue 查询数据异常 result=" + result);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "catalogue 学科课程-目录列表 获取失败");
        }
        return list;
    }

    /**
     * 8.搜索页-搜索课时
     * https://uc.zjedu.com/api/base/eduInfo/search/course/list
     * @return
     */
    public PageResult searchCourseList(EduDto eduDto){
        PageResult pageResult = new PageResult(0L, new ArrayList());
        pageResult.setHasNextPage(false);
        List<Course> list = null;
        StringBuilder sb = new StringBuilder();
        sb.append("?periodId=" + (!ObjectUtils.isEmpty(eduDto.getPeriodId()) ? eduDto.getPeriodId() : 0));
        sb.append("&gradeId=" + (!ObjectUtils.isEmpty(eduDto.getGradeId()) ? eduDto.getGradeId() : 0));
        sb.append("&subjectId=" + (!ObjectUtils.isEmpty(eduDto.getSubjectId()) ? eduDto.getSubjectId() : 0));
        sb.append("&editionId=" + (!ObjectUtils.isEmpty(eduDto.getEditionId()) ? eduDto.getEditionId() : 0));
        sb.append("&volumeId=" + (!ObjectUtils.isEmpty(eduDto.getVolumeId()) ? eduDto.getVolumeId() : 0));
        sb.append("&searchType=" + (!ObjectUtils.isEmpty(eduDto.getSearchType()) ? eduDto.getSearchType() : 0));

        if (!ObjectUtils.isEmpty(eduDto.getKeyword())) {
            sb.append("&keyword=" + eduDto.getKeyword());
        }
        sb.append("&pageSize=" + (!ObjectUtils.isEmpty(eduDto.getPageSize()) ? eduDto.getPageSize() : 20));
        sb.append("&pageNum=" + (!ObjectUtils.isEmpty(eduDto.getPageNum()) ? eduDto.getPageNum() : 1));

        String url = EduHostConfig.searchCourseList_host + sb.toString();
        try {
            String result = restTemplate.postForEntity(url, null, String.class).getBody();
            LogUtil.info("searchCourseList_host result=" + result + "，url=" + url);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("0".equals(jsonObject.getString("code"))){

                    JSONObject data = (JSONObject)jsonObject.get("data");
                    pageResult.setTotal(data.getLong("total"));
                    pageResult.setHasNextPage(data.getBoolean("hasNext"));

                    JSONArray jsonArray = data.getJSONArray("data");
                    if (jsonArray.size() > 0) {
                        list = jsonArray.toJavaList(Course.class);
                        pageResult.setList(list);
                    }
                }else {
                    LogUtil.error("searchCourseList_host 查询数据异常 result=" + result);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "searchCourseList_host 搜索页-搜索课时 获取失败");
        }
        return pageResult;
    }

    /**
     * 5.拓展课程-拓展课程列表
     * https://uc.zjedu.com/api/base/eduInfo/courseCate/list
     * @return
     */
    public PageResult courseCateList(EduDto eduDto){
        PageResult pageResult = new PageResult(0L, new ArrayList());
        pageResult.setHasNextPage(false);
        List<Course> list = null;
        StringBuilder sb = new StringBuilder();
        sb.append("?periodId=" + (!ObjectUtils.isEmpty(eduDto.getPeriodId()) ? eduDto.getPeriodId() : 0));
        sb.append("&gradeId=" + (!ObjectUtils.isEmpty(eduDto.getGradeId()) ? eduDto.getGradeId() : 0));
        sb.append("&subjectId=" + (!ObjectUtils.isEmpty(eduDto.getSubjectId()) ? eduDto.getSubjectId() : 0));
        sb.append("&editionId=" + (!ObjectUtils.isEmpty(eduDto.getEditionId()) ? eduDto.getEditionId() : 0));
        sb.append("&volumeId=" + (!ObjectUtils.isEmpty(eduDto.getVolumeId()) ? eduDto.getVolumeId() : 0));
        sb.append("&pageSize=" + (!ObjectUtils.isEmpty(eduDto.getPageSize()) ? eduDto.getPageSize() : 20));
        sb.append("&pageNum=" + (!ObjectUtils.isEmpty(eduDto.getPageNum()) ? eduDto.getPageNum() : 1));

        String url = EduHostConfig.courseCateList_host + sb.toString();
        try {
            String result = restTemplate.postForEntity(url, null, String.class).getBody();
            LogUtil.info("courseCateList result=" + result + "，url=" + url);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("0".equals(jsonObject.getString("code"))){

                    JSONObject data = (JSONObject)jsonObject.get("data");
                    pageResult.setTotal(data.getLong("total"));
                    pageResult.setHasNextPage(data.getBoolean("hasNext"));

                    JSONArray jsonArray = data.getJSONArray("data");
                    if (jsonArray.size() > 0) {
                        list = jsonArray.toJavaList(Course.class);
                        pageResult.setList(list);
                    }
                }else {
                    LogUtil.error("courseCateList 查询数据异常 result=" + result);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "courseCateList 拓展课程-拓展课程列表 获取失败");
        }
        return pageResult;
    }
    /**
     * 3.学科课程-标签关联的课时
     * https://uc.zjedu.com/api/base/eduInfo/course/list?courseCateId=1249&tagName=象形字&pageSize=20&pageNum=1
     * @return
     */
    public PageResult tagCourseList(EduDto eduDto){
        PageResult pageResult = new PageResult(0L, new ArrayList());
        pageResult.setHasNextPage(false);
        List<Course> list = null;
        StringBuilder sb = new StringBuilder();
        sb.append("?courseCateId=" + (!ObjectUtils.isEmpty(eduDto.getCourseCateId()) ? eduDto.getCourseCateId() : 0));
        sb.append("&tagName=" + (!ObjectUtils.isEmpty(eduDto.getTagName()) ? eduDto.getTagName() : ""));
        sb.append("&pageSize=" + (!ObjectUtils.isEmpty(eduDto.getPageSize()) ? eduDto.getPageSize() : 20));
        sb.append("&pageNum=" + (!ObjectUtils.isEmpty(eduDto.getPageNum()) ? eduDto.getPageNum() : 1));

        String url = EduHostConfig.tagCourseList_host + sb.toString();
        try {
            String result = restTemplate.postForEntity(url, null, String.class).getBody();
            LogUtil.info("tagCourseList result=" + result + "，url=" + url);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if ("0".equals(jsonObject.getString("code"))){

                    JSONObject data = (JSONObject)jsonObject.get("data");

                    Map<String, Object> re = new HashMap<>();
                    re.put("courseCateId", eduDto.getCourseCateId());
                    re.put("tagName", eduDto.getTagName());
                    re.put("periodId", jsonObject.getLongValue("periodId"));
                    re.put("gradeId", jsonObject.getLongValue("gradeId"));
                    re.put("editionId", jsonObject.getLongValue("editionId"));
                    re.put("subjectId", jsonObject.getLongValue("subjectId"));
                    re.put("volumeId", jsonObject.getLongValue("volumeId"));
                    re.put("gradeName", jsonObject.getString("gradeName"));
                    re.put("editionName", jsonObject.getString("editionName"));
                    re.put("periodName", jsonObject.getString("periodName"));
                    re.put("volumeName", jsonObject.getString("volumeName"));
                    re.put("courseCateName", jsonObject.getString("courseCateName"));
                    re.put("subjectName", jsonObject.getString("subjectName"));
                    pageResult.setResult(re);



                    pageResult.setTotal(data.getLong("total"));
                    pageResult.setHasNextPage(data.getBoolean("hasNext"));

                    JSONArray jsonArray = data.getJSONArray("data");
                    if (jsonArray.size() > 0) {
                        list = jsonArray.toJavaList(Course.class);
                        pageResult.setList(list);
                    }
                }else {
                    LogUtil.error("tagCourseList 查询数据异常 result=" + result);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerDataError);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "tagCourseList 学科课程-标签关联的课时 获取失败");
        }
        return pageResult;
    }
}
