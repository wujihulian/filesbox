package com.svnlan.home.utils;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFileMeta;
import com.svnlan.home.utils.office.LibreOfficeDUtil;
import com.svnlan.manage.vo.ConvertToPDFMsgDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/30 14:20
 */
@Component
public class ConvertPicUtil {

    @Resource
    IoFileDao ioFileDao;

    @Value("${office.libreOfficeVersion}")
    private String libreOfficeVersion;


    public Result convertToPDF(String prefix, CommonSource commonSource, ConvertToPDFMsgDTO convertToPDFMsgDTO) {
        Long sourceID = convertToPDFMsgDTO.getSourceID();
        Result result = null;
        String code = "200";
        String message = "";
        Boolean success = Boolean.FALSE;
        if(null == sourceID || sourceID <= 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if(null == commonSource) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String regionFilePath = commonSource.getPath();
        File file = new File(regionFilePath);
        if(!file.exists()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String fileName = file.getName();
        String fileExtension = FileUtil.getFileExtension(fileName);
        if(!Arrays.asList("pdf", "ppt", "pptx", "xls", "xlsx", "doc", "docx").contains(fileExtension)) {
            // "只支持pdf、ppt、pptx、xls、xlsx、doc、docx转换"
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode() );
        }

        String targetDir = file.getParent() + "/" + "pdf";
        String pdfPath = targetDir + "/" + fileName.substring(0, fileName.length() - fileExtension.length()) + "pdf";
        //转的最后1张图片路径

        String firstPath = FileUtil.getFirstStorageDevicePath(regionFilePath);

        String lastImagePath = file.getParent() + "/image/";
        if (lastImagePath.indexOf(firstPath + "/private/cloud/") >= 0){
            lastImagePath = lastImagePath.replace(firstPath + "/private/cloud/", firstPath + "/common/brochure_attachment/");
        }
        File lastFile = new File(lastImagePath);
        if(!lastFile.exists()) {
            lastFile.mkdirs();
        }
        lastImagePath += fileName.substring(0, fileName.length() - fileExtension.length() - 1) + "@";

        Integer isH264Preview = commonSource.getIsH264Preview();
        String regionH264Path = commonSource.getH264Path();
        //若已转过 并且 H264不为空，则判断下
        if(!ObjectUtils.isEmpty(isH264Preview) && 1 == isH264Preview.intValue() && !StringUtil.isEmpty(regionH264Path)
                && !Boolean.TRUE.equals(convertToPDFMsgDTO.getNoImage())) {
            String regionH264Extension = FileUtil.getFileExtension(regionH264Path);
            //若非jpg
            if(!"jpg".equals(regionH264Extension)) {
                isH264Preview = 0;
                commonSource.setIsH264Preview(isH264Preview);
                commonSource.setH264Path("");
                //
                this.updateH264InfoOperate(commonSource);
            }
        }
        String h264Path = "";
        if (1 == isH264Preview.intValue()){
            // 获取h264Path
            IOFileMeta meta = ioFileDao.getFileValue(commonSource.getSourceID());
            Map<String, Object> map = null;
            if (!ObjectUtils.isEmpty(meta) && !ObjectUtils.isEmpty(meta.getValue())){
                map = JsonUtils.jsonToMap(meta.getValue());
            }
            if (!ObjectUtils.isEmpty(map) && map.containsKey("h264Path") && !ObjectUtils.isEmpty(map.get("h264Path"))){
                h264Path = map.get("h264Path").toString();
            }
        }
        //若是已有转换过的，则跳过
        if(1 == isH264Preview.intValue() && !ObjectUtils.isEmpty(h264Path)) {
            code = "AlreadySuccess";
            message = "";
            lastImagePath = h264Path;
        } else {
            //若原文件非pdf，则先转成pdf
            if(!"pdf".equals(fileExtension)) {
                //转换成PDF
                Boolean isSuccess = LibreOfficeDUtil.libreOfficeCommand(prefix, this.libreOfficeVersion, "pdf",
                        regionFilePath, targetDir);
                if (!isSuccess) {
                    isH264Preview = 2;
                } else {
                    isH264Preview = 1;
                }
            } else {
                pdfPath = regionFilePath;
            }

            //未转失败时，则再转图片下
            if(2 != isH264Preview.intValue() && !Boolean.TRUE.equals(convertToPDFMsgDTO.getNoImage())) {
                //将PDF转PNG或JPG（有压缩）
                lastImagePath = PdfDUtil.pdfToPngOrJpg(prefix, pdfPath, lastImagePath, 0.2, 6600, 1900);
                //为NULL则失败
                if(null == lastImagePath) {
                    isH264Preview = 2;
                } else {
                    isH264Preview = 1;
                }
            }

            commonSource.setIsH264Preview(isH264Preview);
            //转成功才将
            if(1 == isH264Preview.intValue()) {
                commonSource.setH264Path(lastImagePath);
            } else {
                commonSource.setH264Path("");
            }

            //执行更新状态等
            this.updateH264InfoOperate(commonSource);

            //若是失败，则
            if(2 == isH264Preview.intValue()) {
                code = "ConvertFail";
                message = "转换失败";
            }
        }
        commonSource.setPdfPath(pdfPath);
        commonSource.setH264Path(lastImagePath);

        if(!"200".equals(code)) {
            success = Boolean.FALSE;
        }
        result = new Result(success, code, message, commonSource);
        return result;
    }


    /** 保存h264Path */
    public void updateH264InfoOperate(CommonSource commonSource) {
        this.ioFileDao.updateH264Info(commonSource);
        if (!ObjectUtils.isEmpty(commonSource.getH264Path())){
            if (ObjectUtils.isEmpty(commonSource.getSourceID())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
            }
            IOFileMeta meta = ioFileDao.getFileValue(commonSource.getSourceID());
            Map<String, Object> map = null;
            if (!ObjectUtils.isEmpty(meta) && !ObjectUtils.isEmpty(meta.getValue())){
                map = JsonUtils.jsonToMap(meta.getValue());
            }
            if (ObjectUtils.isEmpty(map)){
                map = new HashMap<>(1);
            }
            map.put("h264Path", commonSource.getH264Path());

            try {
                if (!ObjectUtils.isEmpty(meta)){
                    ioFileDao.updateOneFileUrlValue(meta.getFileID(), JsonUtils.beanToJson(map));
                }else {
                    IOFileMeta fileMeta = new IOFileMeta();
                    fileMeta.setFileID(meta.getFileID());
                    fileMeta.setKey("fileInfoMore");
                    fileMeta.setValue(JsonUtils.beanToJson(map));
                    ioFileDao.insertMeta(fileMeta);
                }
            }catch (Exception e){
                LogUtil.error(e, " 保存h264Path失败！commonSource=" + JsonUtils.beanToJson(commonSource));
            }
        }
    }
}
