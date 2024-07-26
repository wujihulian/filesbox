package com.svnlan.home.enums;

/**
 * @Author:
 * @Description: 上传返回状态enum
 */
public enum UploadEnum {
    uploadSuccess(0, "上传成功"),
    uploadFailed(1, "上传失败"),
    fileEmpty(2, "文件为空"),
    fileExists(0, "文件已存在"),
    fileNotExists(0, "文件不存在"),
    fileUploading(0, "相同文件正在上传");

    private Integer code;
    private String msg;
    UploadEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }
}
