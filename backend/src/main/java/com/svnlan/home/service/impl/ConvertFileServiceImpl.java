package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.service.ConvertFileService;
import com.svnlan.home.utils.ConvertUtil;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/27 16:05
 */
@Service
public class ConvertFileServiceImpl implements ConvertFileService {

    @Resource
    ConvertUtil convertUtil;

    /**
     * 试着转码
     */
    @Override
    public void tryToConvertFile(CommonSourceVO commonSourceVO, CommonSource commonSource, String busType) {
        if (commonSourceVO.getFileID() != null && !commonSourceVO.getFileID().equals(0L)) {

            ConvertDTO convertDTO = new ConvertDTO();
            convertDTO.setBusId(commonSourceVO.getSourceID());
            convertDTO.setBusType(busType);
            convertDTO.setDomain(commonSource.getDomain());

            LogUtil.info("doConvert commonSource=" + JsonUtils.beanToJson(commonSource));


            String sourceSuffix = commonSourceVO.getFileType();
            if (Arrays.asList(GlobalConfig.yongzhong_doc_type, GlobalConfig.yongzhong_excel_type
                    , GlobalConfig.yongzhong_ppt_type).contains(sourceSuffix)) {
                // 转码放外面
                convertUtil.doConvert(convertDTO, commonSource);
            } else {
                if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(sourceSuffix)) {
                    // 转码中记录
                    convertUtil.saveVideoConvertRecord(commonSource, "0", "");
                }

                if (!Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(sourceSuffix)) {
                    // 转码放外面
                    Date date = new Date();
                    LogUtil.info("doConvert-- begin---" + JsonUtils.beanToJson(commonSourceVO) + "，time=" + DateUtil.getYearMonthDayHMS(date, DateUtil.YYYY_MM_DD_HH_MM_SS));
                    convertUtil.doConvert(convertDTO, commonSource);
                    LogUtil.info("doConvert--end--" + JsonUtils.beanToJson(commonSourceVO) + "，time=" + DateUtil.getYearMonthDayHMS(date, DateUtil.YYYY_MM_DD_HH_MM_SS));
                }
            }
        }
    }
}
