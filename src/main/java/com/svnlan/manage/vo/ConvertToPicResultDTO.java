package com.svnlan.manage.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 转换成图片结果
 * @author
 */
@Data
public class ConvertToPicResultDTO {

    private String status;	//状态：0-进行中，1-成功，2-失败
    private List<String> list;

}
