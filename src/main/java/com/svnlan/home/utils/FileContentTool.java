package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.domain.CommonSource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/19 13:23
 */
@Component
public class FileContentTool {

    /** .txt .js .json .css .sql .xml .java .cs(c#), 返回text */
    public void getFileContent(CommonSource cloudFile, Map<String, Object> reMap){

        // ts,js,json,java,css,html,txt,less,scss,ejs,go,jsx,json5,ls,mysql,nginx,php,py,rb,rst,tsx,xml
        if (Arrays.asList("txt","md","json","css","java","cs","xml","sql","js","ts","less","scss","ejs","go","jsx","json5","ls","mysql"
        ,"nginx","php","py","rb","rst","tsx","srt","ass","smm").contains(cloudFile.getFileType().toLowerCase())){
            //reMap.put("text", FileUtil.getFileContent(cloudFile.getPath(), StandardCharsets.UTF_8));
            //reMap.put("text", HtmlUtils.htmlEscape(FileUtil.textData(cloudFile.getPath())));
            reMap.put("text", (FileUtil.textData(cloudFile.getPath())));
        }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(cloudFile.getFileType().toLowerCase())){
            //reMap.put("text", FileUtil.getImageContent(cloudFile.getPath()));
            reMap.put("imgData", ""); //FileUtil.getImgStr(cloudFile.getPath())
        }else if (Arrays.asList("doc","docx").contains(cloudFile.getFileType().toLowerCase())){
            // reMap.put("text", FileUtil.getDocxContent(cloudFile.getPath()));
        }else if (Arrays.asList("html","htm").contains(cloudFile.getFileType().toLowerCase())){
            reMap.put("text", FileUtil.parseDocumentFromFile(cloudFile.getPath()));
        }
    }
}
