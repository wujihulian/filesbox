package com.svnlan.user.tools;

import com.svnlan.user.dto.ThirdDataResult;
import com.svnlan.user.dto.ThirdSearchResult;
import com.svnlan.jwt.dto.CubeLogin;
import com.svnlan.jwt.dto.CubeResult;
import com.svnlan.jwt.tool.RestRequestUtil;
import com.svnlan.user.dto.SearchCourseDTO;
import com.svnlan.user.vo.SearchCourseVo;
import com.svnlan.user.vo.TeachTypeTree;
import com.svnlan.user.vo.ThirdSearchVo;
import com.svnlan.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 之江汇搜索
 */
@Component
public class ThirdSearchUtils {

    @Value("${third.search.appId}")
    private String searchAppId;

    @Value("${third.search.appSecretKey}")
    private String searchAppSecretKey;
    @Resource
    private RestTemplate restTemplate;


    private String getSign(Long timestamp){
        return PasswordUtil.MD5(searchAppSecretKey + timestamp);
    }

    private HashMap<String, Object> getHeadSign(Long timestamp){
        HashMap<String, Object> headMap = new HashMap<>();
        headMap.put("appId",searchAppId);
        headMap.put("sign", getSign(timestamp));
        headMap.put("timestamp", timestamp);
        return headMap;
    }

    public ThirdSearchResult searchCourseList(SearchCourseDTO searchDto){
        // 秒级时间戳
        Long timestamp = System.currentTimeMillis() / 1000;
        HashMap<String, Object> headMap = getHeadSign(timestamp);

        ParameterizedTypeReference<ThirdSearchResult<ThirdSearchVo<SearchCourseVo>>> reference =
                new ParameterizedTypeReference<ThirdSearchResult<ThirdSearchVo<SearchCourseVo>>>() {
        };
        String url = ThirdSearchApi.searchCourseList + changeParam(searchDto);
        ThirdSearchResult<ThirdSearchVo<SearchCourseVo>> searchResult = RestRequestUtil.executeGet(restTemplate, url
                , headMap, null, reference);

        Assert.notNull(searchResult, "searchResult 为 null");
        Assert.isTrue("0".equals(searchResult.getCode()) , searchResult.getMsg());

        return searchResult;
    }

    public ThirdDataResult teachTypeTreeHasAll(){
        // 秒级时间戳
        Long timestamp = System.currentTimeMillis() / 1000;
        HashMap<String, Object> headMap = getHeadSign(timestamp);

        ParameterizedTypeReference<ThirdDataResult<TeachTypeTree>> reference =
                new ParameterizedTypeReference<ThirdDataResult<TeachTypeTree>>() {
                };
        String url = ThirdSearchApi.teachTypeTreeHasAll ;
        ThirdDataResult<TeachTypeTree> searchResult = RestRequestUtil.executeGet(restTemplate, url
                , headMap, null, reference);

        Assert.notNull(searchResult, "searchResult 为 null");
        Assert.isTrue("0".equals(searchResult.getCode()) , searchResult.getMsg());

        return searchResult;
    }

    private String changeParam(SearchCourseDTO searchDto){
        StringBuilder str = new StringBuilder();
        str.append(String.format("?keyword=%s&searchType=%s&pageSize=%s&pageNum=%s",
                searchDto.getKeyword(),searchDto.getSearchType()),searchDto.getPageSize(),searchDto.getCurrentPage());
        if (!ObjectUtils.isEmpty(searchDto.getPeriodId())){
            str.append("&periodId=" + searchDto.getPeriodId());
        }
        if (!ObjectUtils.isEmpty(searchDto.getGradeId())){
            str.append("&gradeId=" + searchDto.getGradeId());
        }
        if (!ObjectUtils.isEmpty(searchDto.getSubjectId())){
            str.append("&subjectId=" + searchDto.getSubjectId());
        }
        if (!ObjectUtils.isEmpty(searchDto.getEditionId())){
            str.append("&editionId=" + searchDto.getEditionId());
        }
        if (!ObjectUtils.isEmpty(searchDto.getVolumeId())){
            str.append("&volumeId=" + searchDto.getVolumeId());
        }
        return str.toString();
    }

}
