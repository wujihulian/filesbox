package com.svnlan.home.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.home.dto.YzOfficDto;
import com.svnlan.home.service.YzService;
import com.svnlan.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/7 13:37
 */
@RestController
public class YzCallbackController {

    @Resource
    YzService yzService;

    @PostMapping(value = "/api/disk/yzwo/office", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String YzCallback(YzOfficDto officDto, MultipartFile file) {


        yzService.yzCallback(officDto, file);
        Result re = new Result(true, CodeMessageEnum.success.getCode(), true);
        return JsonUtils.beanToJson(re);
    }

}
