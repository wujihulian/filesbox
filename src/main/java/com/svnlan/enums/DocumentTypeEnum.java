package com.svnlan.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 14:25
 */
public enum DocumentTypeEnum {

    // 文档
    doc("doc", 1, "explorer.type.doc", "txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp", "doc"),
    // 图片
    image("image", 2, "explorer.type.image", "jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai", "image"),
    // 音乐
    music("music", 3, "explorer.type.music", "mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape,mpa", "music"),
    // 视频
    movie("movie", 4, "explorer.type.movie", "mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx", "movie"),
    // 压缩包
    zip("zip", 5, "explorer.type.zip", "zip,gz,rar,iso,tar,7z,ar,bz,bz2,xz,arj", "zip"),
    // 其他
    others("other", 6, "explorer.type.others", "swf,html,exe,msi", "other"),
    ;

    private String code;

    private Integer type;

    private String value;

    private String ext;
    private String icon;

    DocumentTypeEnum(String code, Integer type, String value, String ext, String icon) {
        this.type = type;
        this.ext = ext;
        this.code = code;
        this.value = value;
        this.icon = icon;
    }

    private static final Map<String, Integer> fileExtMap = new HashMap<>();

    static {
        for (DocumentTypeEnum typeEnum : DocumentTypeEnum.values()) {
            String[] split = typeEnum.ext.split(",");
            for (String ext : split) {
                fileExtMap.put(ext, typeEnum.type);
            }
        }
    }

    /**
     * 通过文件后缀获取文件类型
     *
     * @param ext 文件后缀
     * @return 文件类型
     */
    public static Integer getTypeByExt(String ext) {
//        for (DocumentTypeEnum documentTypeEnum : DocumentTypeEnum.values()) {
//            if (documentTypeEnum.ext.contains(ext)) {
//                return documentTypeEnum.getType();
//            }
//        }

        // 默认为其他类型
//        return others.getType();
        return Optional.ofNullable(fileExtMap.get(ext)).orElse(others.getType());
    }

    public Integer getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public String getExt() {
        return ext;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
