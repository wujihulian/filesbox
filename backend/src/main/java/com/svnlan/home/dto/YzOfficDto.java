package com.svnlan.home.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/7 13:41
 */
@Data
public class YzOfficDto {

    private List<String> userId;
    private String fileId;
    private String filePath;
}
