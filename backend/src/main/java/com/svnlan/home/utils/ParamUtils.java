package com.svnlan.home.utils;

import com.svnlan.enums.DocumentTypeEnum;
import com.svnlan.home.dto.HomeExplorerDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/17 13:33
 */
@Component
public class ParamUtils {

    public void docSearchParam(String fileType, Map<String, Object> hashMap){
        if (fileType.indexOf(DocumentTypeEnum.doc.getExt()) >= 0){
            hashMap.put("documentType", DocumentTypeEnum.doc.getType());
        }else if (fileType.indexOf(DocumentTypeEnum.image.getExt()) >= 0){
            hashMap.put("documentType", DocumentTypeEnum.image.getType());
        }else if (fileType.indexOf(DocumentTypeEnum.music.getExt()) >= 0){
            hashMap.put("documentType", DocumentTypeEnum.music.getType());
        }else if (fileType.indexOf(DocumentTypeEnum.movie.getExt()) >= 0){
            hashMap.put("documentType", DocumentTypeEnum.movie.getType());
        }else if (fileType.indexOf(DocumentTypeEnum.zip.getExt()) >= 0){
            hashMap.put("documentType", DocumentTypeEnum.zip.getType());
        }else if (fileType.indexOf(DocumentTypeEnum.others.getExt()) >= 0){
            hashMap.put("documentType", DocumentTypeEnum.others.getType());
        }else {
            hashMap.put("fileTypeList", Arrays.asList(fileType.split(",")).stream().map(String::valueOf).collect(Collectors.toList()));
        }
    }

    public String getHomeExplorerSecondKey(HomeExplorerDTO homeExplorerDTO){
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getKeyword())){
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getFileType()) && "folder".equals(homeExplorerDTO.getFileType())){
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getFromType()) && homeExplorerDTO.getFromType()) {
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getMinSize()) || !ObjectUtils.isEmpty(homeExplorerDTO.getMaxSize())) {
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getTimeFrom()) || !ObjectUtils.isEmpty(homeExplorerDTO.getTimeTo())) {
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getUserID())) {
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getRepeatName()) || !ObjectUtils.isEmpty(homeExplorerDTO.getRepeatSourceID())) {
            return "";
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getSortField()) || !ObjectUtils.isEmpty(homeExplorerDTO.getSortType())) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())) {
            sb.append(homeExplorerDTO.getSourceID());
        }else {
            sb.append(0);
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getFileType())) {
            sb.append("_" + homeExplorerDTO.getFileType());
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getTagID())) {
            sb.append("_" + homeExplorerDTO.getTagID());
        }
        sb.append("_" + homeExplorerDTO.getCurrentPage());
        sb.append("_" + homeExplorerDTO.getPageSize());
        return sb.toString();
    }
}
