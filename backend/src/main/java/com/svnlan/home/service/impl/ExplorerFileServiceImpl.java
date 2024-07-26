package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CompressFileDto;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.service.ExplorerFileService;
import com.svnlan.home.utils.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.PluginListVo;
import com.svnlan.utils.HttpUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/25 14:19
 */
@Service
public class ExplorerFileServiceImpl implements ExplorerFileService {

    @Resource
    CompressFileReader compressFileReader;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    OptionTool optionTool;

    @Override
    public Object unzipList(HomeExplorerDTO homeExp, LoginUser loginUser){

        if (ObjectUtils.isEmpty(homeExp.getSourceID()) || homeExp.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        CommonSource source = ioSourceDao.getSourceInfo(homeExp.getSourceID());
        CompressFileReader.FileNode fileNode = null;
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(source.getPath())){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String compressKey = GlobalConfig.FILE_PREVIEW_COMPRESS_KEY + source.getPath();
        String value = stringRedisTemplate.opsForValue().get(compressKey);
        if (!ObjectUtils.isEmpty(value)){
            try {
                fileNode = JsonUtils.jsonToBean(value, CompressFileReader.FileNode.class);
            }catch (Exception e){
                LogUtil.error(e, "unzipList error value=" + value);
            }
        }
        if (ObjectUtils.isEmpty(fileNode)){
            fileNode = getFileNode(source, compressKey, fileNode);
        }
        if (ObjectUtils.isEmpty(fileNode)){
            File f = new File(source.getPath());
            if (f.exists()){
                throw new SvnlanRuntimeException(CodeMessageEnum.unzipErrorTips.getCode());
            }
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        if (!ObjectUtils.isEmpty(homeExp.getFullName())){
            String suffix = FileUtil.getFileExtension(homeExp.getFullName());

            if (fileNode.getEncrypted() && ObjectUtils.isEmpty(homeExp.getPassword())){
                throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
            }
            if(ObjectUtils.isEmpty(homeExp.getIndex())){
                throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
            }
            String extractPath = source.getPath().substring(0, source.getPath().lastIndexOf(".") ) + GlobalConfig.separatorTO;
            String firstPath = FileUtil.getFirstStorageDevicePath(source.getPath());
            extractPath = extractPath.replace(firstPath + "/private", firstPath + "/common/down_temp");

            String filePath = extractPath + homeExp.getFullName();
            // 单个解压
            CompressFileDto dto =  CompressFileUtil.unzipOneFilePassword(source.getPath(), filePath, homeExp.getPassword(), homeExp.getIndex());
            if (ObjectUtils.isEmpty(dto) || !dto.getSuccess()){
                if (!ObjectUtils.isEmpty(homeExp.getPassword())){
                    throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
                }else {
                    throw new SvnlanRuntimeException(CodeMessageEnum.unzipErrorTips.getCode());
                }
            }

            String path = "";
            if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix) || Arrays.asList("mp3","xmind","smm","mind","km","epub").contains(suffix)) {
                // 预览单个文件则返回链接
                return filePath;
            }else  if (Arrays.asList("txt","md","json","css","java","cs","xml","sql","js","ts","less","scss","ejs","go","jsx","json5","ls","mysql"
                    ,"nginx","php","py","rb","rst","tsx","smm").contains(suffix)){
                return (FileUtil.textData(filePath));
                //return HtmlUtils.htmlEscape(FileUtil.textData(filePath));
                //return FileUtil.getFileContent(filePath, StandardCharsets.UTF_8);
            }else if (Arrays.asList("html","htm").contains(suffix)) {
                return FileUtil.parseDocumentFromFile(filePath);
            }else {
                // 获取预览插件配置
                List<PluginListVo> pluginList = optionTool.pluginList();
                boolean isWeb365 = false;
                boolean isYz = false;
                if (!CollectionUtils.isEmpty(pluginList)){
                    for (PluginListVo vo : pluginList){
                        if ("yzow".equals(vo.getType()) && 1 == vo.getStatus().intValue()){
                            isYz = true;
                        }
                        if ("ow365".equals(vo.getType()) && 1 == vo.getStatus().intValue()){
                            isWeb365 = true;
                        }
                    }
                }
                if (isYz){
                    boolean isYongZhong = Arrays.asList(GlobalConfig.yongzhong_doc_excel_ppt_type).contains(suffix);
                    if (isYongZhong){
                        CommonSource cs = new CommonSource();
                        cs.setPath(filePath);
                        cs.setFileType(suffix);
                        String serverUrl = HttpUtil.getRequestRootUrl(null);
                        cs.setDomain(serverUrl);
                        cs.setName(fileNode.getOriginName());
                        cs.setUserID(loginUser.getUserID());
                        String yzViewData = convertUtil.yongZhongView(cs, false);
                        LogUtil.info("unzipList get yz url yzViewData=" + yzViewData);
                        if (!ObjectUtils.isEmpty(yzViewData)){
                            try {
                                Map<String, Object> m = JsonUtils.jsonToMap(yzViewData);
                                if (!ObjectUtils.isEmpty(m) && m.containsKey("viewUrl")){
                                    path = m.get("viewUrl").toString();
                                }
                            }catch (Exception e){
                                LogUtil.error(e, "永中链接");
                            }
                        }
                    }
                }
                if (!isYz && isWeb365){
                    if (Arrays.asList(GlobalConfig.DOC_SHOW_TYPE_ARR).contains(suffix)){
                        path = fileOptionTool.getPptPreviewUrl(filePath, "", false);
                    }
                }
                return path;
            }
        }

        fileNode.setFileName(source.getName());
        fileNode.setOriginName(source.getName());

        return fileNode;
    }

    @Override
    public CompressFileReader.FileNode getFileNode(CommonSource source, String compressKey, CompressFileReader.FileNode fileNode ){
        String listStr = compressFileReader.unRar(source.getPath(), source.getName(), "");
        LogUtil.info("unzipList listStr=" + listStr);
        if (!ObjectUtils.isEmpty(listStr)){
            try {
                fileNode = JsonUtils.jsonToBean(listStr, CompressFileReader.FileNode.class);
            }catch (Exception e){
                LogUtil.error(e, "unzipList error");
            }
            // 放入缓存
            stringRedisTemplate.opsForValue().set(compressKey, listStr, 3, TimeUnit.HOURS);
            try {
                String firstPath = FileUtil.getFirstStorageDevicePath(source.getPath());
                String extractPath = source.getPath().substring(0, source.getPath().lastIndexOf(".") ) + GlobalConfig.separatorTO;
                extractPath = extractPath.replace(firstPath + "/private", firstPath + "/common/down_temp");
                //路径缓存, 供定时任务删除使用
                stringRedisTemplate.opsForZSet().add(GlobalConfig.COMMON_DOWNLOAD_KEY_SET,
                        extractPath, System.currentTimeMillis() + 86400000);
            } catch (Exception e){
                LogUtil.error(e, "下载文件缓存失败");
            }

        }
        return fileNode;
    }


    @Override
    public Boolean checkZipIsEncrypted(HomeExplorerDTO homeExp){
        if (ObjectUtils.isEmpty(homeExp.getSourceID()) || homeExp.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        CommonSource source = ioSourceDao.getSourceInfo(homeExp.getSourceID());
        if (ObjectUtils.isEmpty(source)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        LogUtil.info("checkZipIsEncrypted source=" + JsonUtils.beanToJson(source));
        return CompressFileUtil.checkZipIsEncrypted(source.getPath());
    }
}
