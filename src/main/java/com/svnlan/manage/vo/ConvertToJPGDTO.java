package com.svnlan.manage.vo;

import lombok.Data;

/**
 * @description: 转换结果
 */
@Data
public class ConvertToJPGDTO {

    private String status;	//状态：0-进行中，1-成功，2-失败
    private String lastImagePath;

    public ConvertToJPGDTO() {
    }

    public ConvertToJPGDTO(String status, String lastImagePath) {
        this.status = status;
        this.lastImagePath = lastImagePath;
    }

}
