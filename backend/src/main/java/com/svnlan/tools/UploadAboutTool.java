package com.svnlan.tools;


import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class UploadAboutTool {


    public Map<String, Object> reUploadFileRepeatType3Map(Map<String, Object> resultMap, CommonSource commonSource) {
        resultMap.put("fileExists", true);
        resultMap.put("chunkList", new ArrayList<>());
        resultMap.put("sourceID", commonSource.getSourceID());
        resultMap.put("fileID", commonSource.getFileID());
        resultMap.put("resolution", commonSource.getResolution());
        //resultMap.put("sourcePath", FileUtil.returnPath(commonSource.getPath(), "cloud", commonSource.getThumb()));
        //resultMap.put("thumb", commonSource.getThumb());
        //处理返回文档预览URL
        CommonSourceVO commonSourceVO = new CommonSourceVO();
        //
        // this.doPreviewUrlByBusType(newSource, busTypeEnum, commonSourceVO);
        String previewUrl = commonSourceVO.getPath();
        if (!StringUtil.isEmpty(previewUrl)) {
            resultMap.put("sourcePath", previewUrl);
        }
        //第三方预览地址
        String newPreviewUrl = commonSourceVO.getPreviewUrl();
        if (!StringUtil.isEmpty(newPreviewUrl)) {
            resultMap.put("previewUrl", newPreviewUrl);
        }
        //获取已上传的分片序号
        return resultMap;
    }
}
