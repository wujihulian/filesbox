package com.svnlan.manage.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.manage.domain.CommonDesign;
import com.svnlan.manage.domain.CommonInfo;
import com.svnlan.manage.vo.CommonInfoVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.Map;

/**
 * @Author:
 * @Description:
 */
@Component
public class DesignUtil {
    private static final String designBasePath = "/design/home/";



    /**
       * @Description: 获取客户端类型目录
       * @params:  [clientType]
       * @Return:  java.lang.String
       * @Modified:
       */
    public static String getClientPath(String clientType) {
        String clientPath = "";
        switch (clientType) {
            case "1":
                clientPath = "/pc/";
                break;
            case "2":
                clientPath = "/mb/";
                break;
            case "3":
                clientPath = "/app/";
                break;
            default:
                return null;
        }
        return clientPath;
    }


    /**
       * @Description: 从json解析SEO
       * @params:  [seo]
       * @Return:  java.lang.String[]
       * @Modified:
       */
    public static String[] getSEOInfoByString(String seo) {
        String[] seoInfo = new String[]{"", ""};
        if (StringUtil.isEmpty(seo)){
            return seoInfo;
        } else {
            try {
                Map<String, Object> map = JsonUtils.jsonToMap(seo);
                seoInfo[0] = map.getOrDefault("keyword", "").toString();
                seoInfo[1] = map.getOrDefault("description", "").toString();
            } catch (Exception e){
                LogUtil.error(e, "seo解析失败, " + seo);
            }
        }
        return seoInfo;
    }

    public static String getDetail(CommonInfoVo commonInfo, String infoHtmlPath) {
        return getDetail(commonInfo.getInfoID(), commonInfo.getDetail(), infoHtmlPath, ObjectUtils.isEmpty(commonInfo.getPathPre()) ? GlobalConfig.default_disk_path_pre : commonInfo.getPathPre());
    }
    public static String getDetail(Long infoID, String detailStr, String infoHtmlPath, String pathPre) {
        // String detailFilePath = designBasePath  + infoHtmlPath + "/pc/" + seo.getTypeStr() + "/__detail_" + seo.getId() + "__.txt";
        String detailFilePath = pathPre + designBasePath + infoHtmlPath + "/pc/information/__detail_"
                + infoID + "__.txt";
        String detail = FileUtil.getFileContent(detailFilePath);
        if (StringUtil.isEmpty(detail)){
            detail = detailStr;
        }
        return detail;
    }
    public static String getImportFiePath(String sourcePath, Long sourceId, boolean validate) {
        String path = sourcePath
                + "." + sourceId
                + ".importDesign";
        if (validate) {
            File file = new File(path);
            if (!file.exists()){
                path = sourcePath;
            }
        }
        return path;
    }
    /**
     * @Description: 从装扮的字段提取seo
     * @params:  [commonDesign]
     * @Return:  java.lang.String[]
     * @Modified:
     */
    public static String[] getSEOInfoByDesign(CommonDesign commonDesign) {
        String[] seoInfo = null;
        if (commonDesign != null && !StringUtil.isEmpty(commonDesign.getSeo())){
            try {
                Map<String, Object> map = JsonUtils.jsonToMap(commonDesign.getSeo());
                if (map.get("keyword") != null && !map.get("keyword").toString().trim().equals("")){
                    seoInfo = new String[2];
                    seoInfo[0] = map.get("keyword").toString();
                    seoInfo[1] = map.get("description").toString();
                }
            } catch (Exception e){
                LogUtil.error(e, "保存装扮, SEO信息解析错误, " + commonDesign.getSeo() + ", " + commonDesign.getCommonDesignId());
            }
        }
        return seoInfo;
    }
}
