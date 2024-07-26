package com.svnlan.home.dto;

import lombok.Data;
import net.sf.sevenzipjbinding.ExtractOperationResult;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/5 10:30
 */
@Data
public class CompressFileDto {

    /** 1 解压成功  0 解压失败 2 密码错误 */
    private Integer status;
    private Boolean success;
    private ExtractOperationResult result;
}
