package com.svnlan.manage.utils;

import com.alibaba.fastjson.JSONException;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.manage.dao.CommonDesignDao;
import com.svnlan.manage.domain.CommonDesign;
import com.svnlan.manage.enums.DesignClientTypeEnum;
import com.svnlan.manage.enums.DesignTypeEnum;
import com.svnlan.manage.enums.DesignUsedEnum;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PropertiesUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/5 13:14
 */
@Component
public class AsyncDesignUtil {

    @Resource
    CommonDesignDao commonDesignDao;
    @Resource
    DesignValidateUtil designValidateUtil;

    @Value("${info.common.htmlPath.name}")
    private String infoHtmlPath;

    /**
     * @Description: 初始化装扮保存路径
     * @params:
     */
    private String designBasePath;
    private String templatePath;
    @PostConstruct
    public void setDesignBasePath(){
        // 已改
        try {
            Properties properties = PropertiesUtil.getConfig(GlobalConfig.UPCONFIG);
            designBasePath = properties.getProperty("home.savePath");
            templatePath = properties.getProperty("template.savePath");
        } catch (Exception e){
            designBasePath = "/uploads/design/home/";
            templatePath = "/uploads/design/template/";

        }
    }

    public Boolean  saveDesignFile(Long designId, Map<String, Object> result) {
        //获取当前装扮
        CommonDesign commonDesign = commonDesignDao.selectById(designId);

        //小程序装扮
        if (commonDesign.getClientType().equals(DesignClientTypeEnum.MP.getCode())){
            result.put("message", "小程序装扮");
            return true;
        }

        //非启用的，直接返回
        if (!commonDesign.getIsUsed().equals(DesignUsedEnum.USED.getCode()) && commonDesign.getDesignType().equals(DesignTypeEnum.MAIN.getCode())){
            result.put("message", "非启用");
            return true;
        }
        CommonDesign mainPage = commonDesign;
        //二级页高度
        int subHeight = 0;
        //如果是修改二级页，获取主页信息
        if (commonDesign.getDesignType().equals(DesignTypeEnum.SUB.getCode())){
            subHeight = Integer.valueOf(commonDesign.getSize().split("\\|")[1]);
            mainPage = commonDesignDao.selectForMainPage(commonDesign.getClientType());
            if (mainPage == null){//没有已启用的首页时
                return true;
            }
        }
        //读取配置
        Properties properties = PropertiesUtil.getConfig(GlobalConfig.UPCONFIG);
        if (properties == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }

        //按域名保存
        String fileName = "";
        String clientTypeDirectory;
        String theTemplatePath;
        //手机
        if (commonDesign.getClientType().equals(DesignClientTypeEnum.MOBILE.getCode())){
            clientTypeDirectory = "mb/";
            theTemplatePath =  templatePath + properties.getProperty("template_mobile.fileName");
        } /*else if (commonDesign.getClientType().equals(DesignClientTypeEnum.MP.getCode())){
            clientTypeDirectory = "mp/";
            theTemplatePath =  templatePath + properties.getProperty("template_mp.fileName");
        } */else if (commonDesign.getClientType().equals(DesignClientTypeEnum.PC.getCode())){//都为pc
            clientTypeDirectory = "pc/";
            theTemplatePath =  templatePath + properties.getProperty("template.fileName");
        } else if (commonDesign.getClientType().equals(DesignClientTypeEnum.APP.getCode())){
            clientTypeDirectory = "app/";
            theTemplatePath =  templatePath + properties.getProperty("template_app.fileName");
        } else {
            LogUtil.error("保存文件，客户端类型不正确，"+JsonUtils.beanToJson(commonDesign));
            return false;
        }
        //主页 index.shtml 二级页 {url}.shtml
        if (!commonDesign.getDesignType().equals(DesignTypeEnum.MAIN.getCode())){
            fileName += commonDesign.getUrl().replaceAll("/", "_") + GlobalConfig.GEN_PAGE_SUFFIX;
        } else {
            fileName += "index" + GlobalConfig.GEN_PAGE_SUFFIX;
        }
        //保存目录
        String savePath =  designBasePath +  infoHtmlPath + "/" + clientTypeDirectory;
        File saveDirectory = new File(savePath);
        if (!saveDirectory.exists()){
            if (!saveDirectory.mkdirs()){
                LogUtil.error("创建目录失败," + savePath);
                return false;
            }
        }

        //获取模板文件内容
        String html = FileUtil.getFileContent(theTemplatePath);
        //替换html内容
        html = this.replaceHtml(html, subHeight, mainPage, commonDesign);
        //写入文件
        FileUtil.putFileContent(savePath + fileName, html);

        //如果当前是主页，需要把二级页的文件修改
        if (commonDesign.getDesignType().equals(DesignTypeEnum.MAIN.getCode())) {
            //保存头部尾部, 供公共页使用
            this.saveHeadFoot(savePath, commonDesign);
            this.saveForNotIndex(saveDirectory, mainPage);
        }
        return true;
    }

    /**
     * @Description: 更新index以外的文件
     * @params:  [files, mainPage]
     * @Return:  void
     * @Modified:
     */
    private void saveForNotIndex(File saveDirectory, CommonDesign mainPage) {
        //遍历目录下所有非index的文件
        File[] files = saveDirectory.listFiles(new FileNotSymbolAndDirectoryFilter());
        if (files == null) {
            return;
        }
        Map<String, Object> detailMap =  new HashMap<>();
        //查询二级页seo信息
        Map<String, CommonDesign> subPageMap = commonDesignDao.getSubPageByMain(mainPage);
        String[] SEOInfo;
        for (File i : files) {
            if (i.getName().equals("index"+ GlobalConfig.GEN_PAGE_SUFFIX)) {
                continue;
            }
            //文件名
            String fileName = i.getAbsoluteFile().getName();
            String url = fileName.substring(0, fileName.lastIndexOf("."));
            //从map取二级页, 没有默认主页
            CommonDesign subPage = subPageMap.get(url);
            SEOInfo = designValidateUtil.getSEOInfo(detailMap, subPage, mainPage);
            //装扮文件内容
            String html = FileUtil.getFileContent(i.getAbsolutePath());
            //二级页head, foot 优先使用本装扮, 再使用主页的
            String head = (subPage != null && !StringUtil.isEmpty(subPage.getHead()) ? subPage.getHead() : mainPage.getHead())
                    .replace("\\", "\\\\");
            String foot = (subPage != null && !StringUtil.isEmpty(subPage.getFoot()) ? subPage.getFoot() : mainPage.getFoot())
                    .replace("\\", "\\\\");
            String sCssText = this.getCssText(mainPage.getSetting());
            if (sCssText != null) {
                sCssText = sCssText.replace("\\", "\\\\");
                html = html.replaceFirst("(?<=<!-- /\\*setting.start\\*/ -->)([\\s\\S]*?)(?=<!-- /\\*setting.end\\*/ -->)", this.formatHtml(sCssText));
            }
            html = html.replaceFirst("(?<=<!-- /\\*module.top.start\\*/ -->)([\\s\\S]*?)(?=<!-- /\\*module.top.end\\*/ -->)", this.formatHtml(head));
            html = html.replaceFirst("(?<=<!-- /\\*module.foot.start\\*/ -->)([\\s\\S]*?)(?=<!-- /\\*module.foot.end\\*/ -->)",this.formatHtml(foot));
            html = html.replaceFirst("(?<=<meta name=\"keywords\" content=\")([\\s\\S]*?)(?=\"/>)", SEOInfo[0]);
            html = html.replaceFirst("(?<=<meta name=\"description\" content=\")([\\s\\S]*?)(?=\"/>)", SEOInfo[1]);
            FileUtil.putFileContent(i.getAbsolutePath(), html);
        }
    }

    private String getTitleString(String name, Map<String, Object> detailMap) {
        String titleString = null;
        if (detailMap.get("subhead") != null) {
            String subhead = "";
            //副标题处理
            subhead = detailMap.get("subhead").toString();
            //名称 + 副标题
            titleString = name + (subhead.equals("") ? "" : ("-" + subhead));
        }
        return titleString;
    }

    /**
     * @Description: 替换html内容
     * @params:  [html, subHeight, mainPage, commonDesign]
     * @Return:  java.lang.String
     */
    private String replaceHtml(String html, int subHeight, CommonDesign mainPage, CommonDesign commonDesign) {
        //主体内容替换
        Map<String, Object> detailMap = new HashMap<>();
        String titleString =  null ;

        //非二级页替换网校推广的title
        if (!commonDesign.getDesignType().equals(DesignTypeEnum.SUB.getCode()) && titleString != null) {
            html = html.replaceFirst("(?<=<title>)([\\s\\S]*?)(?=</title>)", titleString);
        }
        //二级页替换装扮title
        else if (commonDesign.getDesignType().equals(DesignTypeEnum.SUB.getCode())){
            html = html.replaceFirst("(?<=<title>)([\\s\\S]*?)(?=</title>)", commonDesign.getTitle());
        }
        //替换SEO信息
        String[] SEOInfo = designValidateUtil.getSEOInfo(detailMap, commonDesign, mainPage);
        html = html.replaceFirst("(?<=<meta name=\"keywords\" content=\")([\\s\\S]*?)(?=\"/>)", SEOInfo[0]);
        html = html.replaceFirst("(?<=<meta name=\"description\" content=\")([\\s\\S]*?)(?=\"/>)", SEOInfo[1]);
        String favIconFilePath = GlobalConfig.FAV_ICON_PATH + GlobalConfig.FAV_ICON_FILENAME;
        //浏览器图标加参数
        html = html.replaceFirst("(?<=<link rel=\"icon\" href=\")([\\s\\S]*?)(?=\" type=\"image/x-icon\">)", favIconFilePath + "?t=" + System.currentTimeMillis());
        //标题额外标签, 微信pc取不到title用
        String title = !StringUtil.isEmpty(commonDesign.getTitle()) ? commonDesign.getTitle() : titleString;
        String titleExtra = "\n<div style=\"display:none\" id=\"hiddenPageTitleForReserve\">" + title + "</div>";
        html = html.replace("<!-- /*module.body.content*/ -->", commonDesign.getDetail() + titleExtra);
        //二级页head,foot优先取本页, 没有再取首页
        html = html.replace("<!-- /*module.top.content*/ -->", StringUtil.isEmpty(commonDesign.getHead()) ? mainPage.getHead() : commonDesign.getHead());
        html = html.replace("<!-- /*module.foot.content*/ -->", StringUtil.isEmpty(commonDesign.getFoot()) ? mainPage.getFoot() : commonDesign.getFoot());
        //css替换
        String cssText = this.getCssText(mainPage.getSetting());
        if (cssText != null) {
            html = html.replace("<!-- /*cssText*/ -->", cssText);
        }
        //二级页高度设置
        if (subHeight != 0){
            html = html.replace("/*cssMiddle*/", "height:" + subHeight + "px;");
        }
        return html;
    }
    /**
     * @Description: 保存头部和尾部
     * @params:  [savePath, commonDesign]
     * @Return:  void
     * @Modified:
     */
    private void saveHeadFoot(String savePath, CommonDesign commonDesign) {
        try {
            String path = savePath + "__index_head__.shtml";
            FileUtil.putFileContent(path, commonDesign.getHead());
        } catch (Exception e){
            LogUtil.error(e, "保存头部文件失败");
        }
        try {
            String path = savePath + "__index_foot__.shtml";
            FileUtil.putFileContent(path, commonDesign.getFoot());
        } catch (Exception e){
            LogUtil.error(e, "保存尾部文件失败");
        }
        try {
            String sCssText = this.getCssText(commonDesign.getSetting());
            if (sCssText != null) {
                sCssText = sCssText.replace("\\", "\\\\");
            } else {
                sCssText = "";
            }
            String path = savePath + "__index_setting__.shtml";
            FileUtil.putFileContent(path, sCssText);
        } catch (Exception e){
            LogUtil.error(e, "保存setting文件失败");
        }
    }



    //格式
    private String formatHtml(String str) {
        return "\n\t\t" + str + "\n\t\t";
    }
    //获取通用样式
    private String getCssText(String setting) {
        Map<String, Object> settingMap;
        try {
            settingMap = JsonUtils.jsonToMap(setting);
        } catch (JSONException e){
            settingMap = null;
        }
        if (settingMap != null && settingMap.get("cssText") != null){
            return settingMap.get("cssText").toString();
        }
        return null;
    }

}
