package com.svnlan.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.GetM3u8NewDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.service.M3u8Service;
import com.svnlan.home.utils.CommonConvertUtil;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.home.dto.*;
import com.svnlan.user.dao.Impl.SystemOptionDaoImpl;
import com.svnlan.utils.*;
import com.svnlan.webdav.MimeTypeEnum;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:
 * @Description:
 * @Date:
 */
@Service
public class M3u8ServiceImpl implements M3u8Service {
    @Resource
    FileOptionTool fileOptionTool;

    @Resource
    private LoginUserUtil loginUserUtil;
    @Resource
    private CommonConvertUtil commonConvertUtil;
    @Resource
    SystemOptionDaoImpl systemOptionDaoImpl;

    @Value("${ppt.replace.need}")
    private String needReplace;
    @Value("${cdn.domain}")
    private String cdnDomain;

    private static Pattern tsPattern = Pattern.compile("(\\.ts$)", Pattern.MULTILINE);
    private static Pattern oldServerPattern = Pattern.compile(GlobalConfig.oldInnerServer);
    private static Pattern urlPattern = Pattern.compile(GlobalConfig.m3u8ConvertUrlPlaceholder);
    private static String tsIdentifyKey = "wjsourcedomain";
    /**
       * @Description: 获取m3u8加密的key
       * @params:  [key]
       * @Return:  void
       * @Modified:
       */
    @Override
    public void getKey(String key,HttpServletResponse response) {
        if (StringUtil.isEmpty(key)){
            return;
        }
        String decodeStr = AESUtil.decrypt(key, GlobalConfig.M3U8_AES_PASSWORD);
        String[] decodeArr;
        try {
            decodeStr = URLDecoder.decode(decodeStr, "UTF-8");
            decodeArr = decodeStr.split(GlobalConfig.M3U8_KEY_INFO_SEPARATOR);
        } catch (Exception e){
            LogUtil.error("解码失败,key:" + key);
            return;
        }
        if (StringUtil.isEmpty(decodeArr[0]) || StringUtil.isEmpty(decodeArr[1])){
            LogUtil.error("解码数据不正确" + JsonUtils.beanToJson(decodeArr));
            return;
        }
        String filePath = decodeArr[1];
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file); ServletOutputStream sos = response.getOutputStream()) {
            String mineType = MimeTypeEnum.getContentType(ext);
            response.setContentType(mineType);

            IOUtils.copy(fis, sos);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("getVideoImgAttachmentWithName 拷贝文件流出错 filePath=" + filePath);
        }

        /*try {
//            LogUtil.info("KEY解码后内容：" + decodeStr);
            //HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.responseFile(response, decodeArr[1], System.currentTimeMillis()+".m3u8", false);
        } catch (Exception e){
            LogUtil.error(e, "获取key失败，" + JsonUtils.beanToJson(decodeArr));
        }*/
    }

    private static boolean isApp(String ua) {
        boolean check = false;
        if (ua.toLowerCase().contains("ijkplayer")){
            check = true;
        }else if (ua.toLowerCase().contains("Lavf")){
            check = true;
        }
        return check;
    }
    /**
       * @Description: 获取m3u8文件
       * @params:  [getM3u8DTO]
       * @Return:  void
       * @Modified:
       */
    @Override
    public void getM3u8(GetM3u8DTO getM3u8DTO, HttpServletResponse response, HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        LogUtil.info("使用了老的m3u8接口, " + ua + ", " + request.getHeader("Referer")
                + ", " + JsonUtils.beanToJson(getM3u8DTO) + ", " + IpUtil.getIp(request));
        // 20200927 老接口废弃，app暂时使用 VUL-9040 播放老接口废弃
        if (!isApp(ua)){
            return;
        }
        String filePath = null;
        CommonSource commonSource = fileOptionTool.getFileAttachment(getM3u8DTO.getSourceID());

        if (commonSource == null){
            // 记录不存在
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        filePath = commonSource.getPreviewUrl();
        if (ObjectUtils.isEmpty(filePath)){
            LogUtil.error("m3u8查询失败" + JsonUtils.beanToJson(getM3u8DTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        //todo 等各端改造
//        this.checkM3u8Permission(m3u8, loginUser);
        try {
            //HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            LogUtil.info("m3u8文件路径:" + filePath);
            if (!"m3u8".equals(FileUtil.getFileExtension(filePath))){
                // "类型不正确"
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
            }
            FileUtil.responseFile(response, filePath, System.currentTimeMillis() + ".m3u8", false);
        } catch (Exception e){
            LogUtil.error(e, "m3u8获取失败");
        }
    }

    /**
       * @Description: 获取m3u8
       * @params:  [getM3u8NewDTO, response, request]
       * @Return:  void
       * @Modified:
       */
    @Override
    public String getM3u8WithAuth(GetM3u8NewDTO getM3u8NewDTO, HttpServletResponse response, HttpServletRequest request, int isApi) {
        //免费的, 直接拥有权限
        boolean hasPermission = true;
        LoginUser loginUser = null;
        if (!hasPermission) {
            if (StringUtil.isEmpty(getM3u8NewDTO.getKey())) {
                // , "验证失败"
                throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
            }

            //从key解密
            String keyString = AESUtil.decrypt(getM3u8NewDTO.getKey(), GlobalConfig.M3U8_PLAY_AES_KEY, true);
            if (keyString == null) {
                // "验证错误"
                throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
            }
            //[token, timestamp]
            String[] keyArr = keyString.split(GlobalConfig.M3U8_PLAY_KEY_SEPARATOR);
            String token = keyArr[0];
            loginUser = commonConvertUtil.getApiLoginUser(request, token, loginUserUtil);
            // loginUserUtil.getLoginUserByToken(request, token);
            if (loginUser == null || loginUser.getUserID() == null) {
                // 用户验证失败
                throw new SvnlanRuntimeException(CodeMessageEnum.loginFirst.getCode());
            }
            Long time = Long.parseLong(keyArr[1]);
            //时间超过一天, 先记录, 暂不限制
            if (time + 86400000 < System.currentTimeMillis()) {
                LogUtil.info("获取m3u8, key时间超限" + time + "+86400000 < " + System.currentTimeMillis()
                        + JsonUtils.beanToJson(getM3u8NewDTO));
            }
        }
        CommonSource commonSource = fileOptionTool.getFileAttachment(getM3u8NewDTO.getSourceID(), getM3u8NewDTO.getF());
        if (commonSource == null){
            // 记录不存在
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        String filePath = commonSource.getPreviewUrl();
        if (ObjectUtils.isEmpty(filePath)){
            LogUtil.error("my_m3u8查询失败" + JsonUtils.beanToJson(getM3u8NewDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        try {
            //HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            LogUtil.info("m3u8文件路径:" + filePath);
            if (!"m3u8".equals(FileUtil.getFileExtension(filePath))){
                // 类型不正确
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
            }
            File f = new File(filePath);
            if (!f.exists()){
                // 文件不存在
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
            }
            String fileContent = FileUtil.getFileContent(f);
            //String fileContent = FileUtil.getM3u8LineContent(filePath, commonSource.getSourceID() + "_" + commonSource.getFileID());
            if (fileContent != null){
                // 转码老域名处理url
                return this.handleOldRootUrl(fileContent);
            }
        } catch (Exception e){
            LogUtil.error(e, "m3u8获取失败");
        }
        return null;
    }

    private String handleOldRootUrl(String fileContent){
        Matcher matcher = null;
        String rootUrl = null;
        if (!ObjectUtils.isEmpty(cdnDomain)){
            rootUrl = "//" + cdnDomain;
        }else {
            rootUrl = HttpUtil.getReqRootUrl(null, getNetworkConfig());
        }
        //String rootUrl = ObjectUtils.isEmpty(this.cdnDomain) ? HttpUtil.getReqRootUrl(null) : "https://" + this.cdnDomain;
        if ("1".equals(needReplace)){
            matcher = oldServerPattern.matcher(fileContent);
            if (matcher.find()){
                return matcher.replaceAll(rootUrl);
            }
        }
        matcher =  urlPattern.matcher(fileContent);
        if (matcher.find()){
            return matcher.replaceAll(rootUrl);
        }
        return fileContent;
    }

    private List<String> getNetworkConfig(){
        List<String> list = null;
        String networkConfig = systemOptionDaoImpl.getSystemConfigByKey("networkConfig",null);
        if (!ObjectUtils.isEmpty(networkConfig) && !"{}".equals(networkConfig)) {
            try {
                JSONObject obj = JSONObject.parseObject(networkConfig);
                String status = obj.getString("status");
                if (!ObjectUtils.isEmpty(status) && "1".equals(status)){
                    list = new ArrayList<>();
                    list.add(obj.getString("intranet"));
                    list.add(obj.getString("external"));
                }
            }catch (Exception e){
            }
        }
        return list;
    }

    private String getTsWithDomainIdentify(String fileContent) {
        Matcher matcher = tsPattern.matcher(fileContent);
        if (matcher.find()){
            return matcher.replaceAll("$1\\?" + tsIdentifyKey +"=" + loginUserUtil.getServerName());
        }
        return fileContent;
    }
}
