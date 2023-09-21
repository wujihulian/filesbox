package com.svnlan.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.domain.LoginUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Author:
 * @Description:
 */
@Component
public class DocUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
       * @Description: pdf 转 图片
       * @params:  [sourceFileName, imagePath]
       * @Return:  int
       * @Modified:
       */
    public int pdf2image(String sourceFileName, String imagePath, String pdfPageRedisKey) {
        File file = new File(imagePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        Integer pageCount = 0;
        try {
            File pdfFile = new File(sourceFileName);
            System.out.println(sourceFileName);
            PDDocument doc = PDDocument.load(pdfFile);
            PDFRenderer renderer = new PDFRenderer(doc);
            pageCount = doc.getNumberOfPages();
            stringRedisTemplate.opsForValue().set(pdfPageRedisKey, imagePath + "/" + pageCount.toString(), 3600, TimeUnit.SECONDS);
            for(int i=0;i<pageCount;i++){
                BufferedImage image = renderer.renderImageWithDPI(i, 96, ImageType.RGB); // Windows native DPI
//                BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, "PNG", new File(imagePath + (i + 1) + ".png"));
            }
        } catch (Exception e) {
            LogUtil.error(e, "pdf转图片失败" + sourceFileName);
        }
        return pageCount;
    }

    /**
       * @Description: 文档转h5
       * @params:  [sourceFileName, commonSource]
       * @Return:  void
       * @Modified:
       */
    public void doc2Image(String sourceFileName, CommonSource commonSource) {
        //转h5
        String apiUrlH5 = getDocApiUrl(sourceFileName, commonSource, true);
        LOGGER.info("apiUrlH5: " + apiUrlH5);
        String res = postFile(sourceFileName, apiUrlH5);
        LOGGER.info("h5 api return : "+ res + "file : " + sourceFileName + ", o:" + commonSource.getName());
        try {
            Map<String, Object> resMap = JsonUtils.jsonToMap(res);
            if (!Integer.valueOf(resMap.get("status").toString()).equals(1)){
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
            }
            //sourceId 和 转码接口id 的map
            stringRedisTemplate.opsForHash().put(GlobalConfig.CONVERT_SOURCE_ID_H5_MAP,
                    commonSource.getFileID().toString(), resMap.get("id").toString());
        } catch (Exception e) {
            LogUtil.error(e, "转h5提交失败,重试" + sourceFileName + ", o:" + commonSource.getName());
            String res1 = postFile(sourceFileName, apiUrlH5);
            LOGGER.info("h5 api return retry : " + res1 + "file : " + sourceFileName + ", o:" + commonSource.getName());
            try {
                Map<String, Object> resMap = JsonUtils.jsonToMap(res);
                if (!Integer.valueOf(resMap.get("status").toString()).equals(1)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
                }
                //sourceId 和 转码接口id 的map
                stringRedisTemplate.opsForHash().put(GlobalConfig.CONVERT_SOURCE_ID_H5_MAP,
                        commonSource.getFileID().toString(), resMap.get("id").toString());
            } catch (Exception e2){
                LogUtil.error(e2, "转h5提交重试也失败" + sourceFileName + ", o:" + commonSource.getName());
            }
        }
    }

    /**
       * @Description: 文档转swf
       * @params:  [sourceFileName, commonSource]
       * @Return:  void
       * @Modified:
       */
    public void pdf2Swf(String sourceFileName, CommonSource commonSource) {
        String apiUlrSwf = getDocApiUrl(sourceFileName, commonSource, false);
        LOGGER.info("apiUrlSwf: " + apiUlrSwf);
        String res2 = postFile(sourceFileName, apiUlrSwf);
        LOGGER.info("swf api return : "+ res2 + ",file : " + sourceFileName + ", o:" + commonSource.getName());
        try {
            Map<String, Object> resMap = JsonUtils.jsonToMap(res2);
            if (!Integer.valueOf(resMap.get("status").toString()).equals(1)){
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode(), "失败");
            }
            //sourceId 和 转码接口id 的map
            stringRedisTemplate.opsForHash().put(GlobalConfig.CONVERT_SOURCE_ID_SWF_MAP,
                    commonSource.getFileID().toString(), resMap.get("id").toString());
        } catch (Exception e) {
            LogUtil.error(e, "转swf提交失败,重试" + sourceFileName + ", o:" + commonSource.getName());
            String res3 = postFile(sourceFileName, apiUlrSwf);
            LOGGER.info("swf api return retry : " + res3  + ", file : " + sourceFileName + ", o:" + commonSource.getName());
            try {
                Map<String, Object> resMap = JsonUtils.jsonToMap(res3);
                if (!Integer.valueOf(resMap.get("status").toString()).equals(1)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode(), "失败");
                }
                //sourceId 和 转码接口id 的map
                stringRedisTemplate.opsForHash().put(GlobalConfig.CONVERT_SOURCE_ID_SWF_MAP,
                        commonSource.getFileID().toString(), resMap.get("id").toString());
            } catch (Exception e2){
                LogUtil.error(e2, "转swf提交重试也失败" + sourceFileName + ", o:" + commonSource.getName());
            }
        }
    }

    @Value("${h5doc.appId}")
    private String appId;
    @Value("${h5doc.appKey}")
    private String appKey;
    @Value("${h5doc.apiUrl}")
    private String apiUrl;
    @Value("${swfdoc.appId}")
    private String appIdSwf;
    @Value("${swfdoc.appKey}")
    private String appKeySwf;
    @Value("${swfdoc.apiUrl}")
    private String apiUrlSwf;

    /**
       * @Description: 获取转码服务接口地址
       * @params:  [sourceFileName, commonSource]
       * @Return:  java.lang.String
       * @Modified:
       */
    private String getDocApiUrl(String sourceFileName, CommonSource commonSource, boolean isH5){
        Long curtime = System.currentTimeMillis() / 1000;
        String theAppid = isH5 ? appId : appIdSwf;
        String theAppKey = isH5 ? appKey : appKeySwf;
        String theApiUrl = isH5 ? apiUrl : apiUrlSwf;
        String src = theAppKey + "appid" + theAppid + "typeupt" + curtime;

        String param = "appid=" + theAppid
                        + "&type=up&t=" + curtime
                        + "&sign="+ DigestUtils.md5Hex(src);
        byte[] bytes;
        try {
            bytes = param.getBytes("UTF-8");
            param = Base64.getEncoder().encodeToString(bytes);
            theApiUrl = theApiUrl + "&param=" + param
                    + "&arg=" + commonSource.getFileID()
                    + "&filename=" + URLEncoder.encode(commonSource.getName(), "UTF-8")
                    + "&type=nfs"
                    + "&nfs=" + sourceFileName.substring(8);//去掉前面的/uploads
//            apiUrl = "http://192.168.0.25:805/i2.aspx?from=ebh&param=YXBwaWQ9ZWJoYXBwMTAwNiZ0eXBlPXVwJnQ9MTU0MDc4MzM5MiZzaWduPTFjNmM3MTdhMDA4ZDE5YjBjNmRkZjhlZTlmMjc0M2Vj&arg=137&filename=%E8%BF%98%E5%A5%BD.docx";
        } catch (Exception e){
            LogUtil.error(e, "获取api地址失败" + JsonUtils.beanToJson(commonSource));
        }
        return theApiUrl;
    }
    /**
       * @Description: 上传文件
       * @params:  [fileName, apiUrl]
       * @Return:  java.lang.String
       * @Modified:
       */
    private static String postFile(String fileName, String apiUrl){
        String result = "";
        try {

            File file = new File(fileName);
            if (file.exists()) {
                HttpClient client = HttpClientBuilder.create().build();

                HttpPost httpPost = new HttpPost(apiUrl);
//                HttpEntity entity = MultipartEntityBuilder.create().addPart("file", new FileBody(file)).build();
//
//                httpPost.setEntity(entity);
                HttpResponse response = client.execute(httpPost);

                HttpEntity responseEntity = response.getEntity();
                result = EntityUtils.toString(responseEntity);
            } else {
                LOGGER.error("h5待转码文件不存在，"+fileName);
            }
        } catch (Exception e) {
            LogUtil.error(e, "h5转码请求失败，" + apiUrl);
        }
        return result;
    }


    public static String zipToFile(String sourceFile, String toFolder, String[] suffix) throws Exception {
        File file = new File(sourceFile);
        String htmlFileName = "";
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))) {

            File fout;
//        String parent = file.getParent();
            ZipEntry entry;
            boolean suffixNamed = false;
            while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
                String fileName = entry.getName();
                fileName = fileName.replaceAll("\\\\", "/");
                if (fileName.indexOf(".html") == fileName.length() - 5) {
                    htmlFileName = fileName;
                } else if (!suffixNamed){
                    String s = FileUtil.getFileExtension(fileName);
                    if (!StringUtil.isEmpty(s) && Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(s)){
                        suffix[0] = s;
                        suffixNamed = true;
                    }
                }
                fout = new File(toFolder, fileName);

                if (!fout.exists()) {
                    fout.getParentFile().mkdirs();
                }
//            FileUtils.copyFile(fout.toPath(), dest.toPath());

                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fout))) {

                    int b = -1;
                    byte[] buffer = new byte[1024];
                    while ((b = zis.read(buffer)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                }
            }
        } catch (Exception e){
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode(), "解压zip失败");
        }
        return htmlFileName;
    }

    public String postYZFile(String postUrl){
        String result = "";
        try {

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(postUrl);
            //RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            //httpPost.setConfig(requestConfig);
            HttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            LogUtil.error(e, "postYZFile 永中转码请求失败，" + postUrl);
        }
        return result;
    }


    @Autowired
    private RestTemplate restTemplate;

    public String postYZFile6(String postUrl, Map<String, Object> param){

        String result = "";
        try {
            //从jwt服务获取用户信息
            result = restTemplate.postForEntity(postUrl + "?jsonParams=" + URLEncoder.encode(JsonUtils.beanToJson(param),"UTF-8"), null, String.class).getBody();
            System.out.println(result);
        } catch (Exception e){
            LOGGER.error("  : 获取当前用户登录信息 >> " + Throwables.getStackTraceAsString(e));

        }
        return result;
    }

    public String postYZFile2(String postUrl, Map<String, Object> param){


        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);//文件类型是multipartFile类型
        //HttpHeaders fileHeader= new HttpHeaders();
        //fileHeader.setContentType(MediaType.parseMediaType(multipartFile.getContentType()));
        //fileHeader.setContentDispositionFormData("file", multipartFile.getOriginalFilename());

        //设置请求体，注意是LinkedMultiValueMap
        //HttpEntity<ByteArrayResource> httpEntity = new HttpEntity<>(new ByteArrayResource(multipartFile.getBytes()), fileHeader);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        //form.add("file", httpEntity);
        form.add("jsonParams", JsonUtils.beanToJson(param));
        //用HttpEntity封装整个请求报文
        org.springframework.http.HttpEntity entity = new org.springframework.http.HttpEntity<>(form, headers);


        String result = "";
        try {
            //从jwt服务获取用户信息
            result = restTemplate.postForEntity(postUrl , entity, String.class).getBody();

            System.out.println(result);
        } catch (Exception e){
            LOGGER.error("  : 获取当前用户登录信息 >> " + Throwables.getStackTraceAsString(e));

        }
        return result;
    }

}
