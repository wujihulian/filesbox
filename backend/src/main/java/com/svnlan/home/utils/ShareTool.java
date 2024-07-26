package com.svnlan.home.utils;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.ShareDao;
import com.svnlan.home.dto.GetAttachmentDTO;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/31 17:12
 */
@Component
public class ShareTool {

    @Resource
    ShareDao shareDao;

    public void checkShareLink(GetAttachmentDTO getAttachmentDTO) {
        List<ShareVo> list = shareDao.getShareByCode(getAttachmentDTO.getShareCode());
        if (CollectionUtils.isEmpty(list) || list.size() > 1){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorPathTips.getCode());
        }
        ShareVo vo = list.get(0);
        vo.setPassword(null);

        if (!ObjectUtils.isEmpty(getAttachmentDTO.getD()) && "1".equals(getAttachmentDTO.getD())) {

            boolean checkDown = false;
            Map<String, Object> optionsMap = new HashMap<>(1);
            if (!ObjectUtils.isEmpty(vo.getOptions())){
                optionsMap = JsonUtils.jsonToBean(vo.getOptions(), Map.class);
            }
            // notDownload 是否禁用下载
            if (optionsMap.containsKey("down")) {
                String downStr = optionsMap.get("down").toString();
                if (!ObjectUtils.isEmpty(downStr)) {
                    vo.setDown(Integer.parseInt(downStr));
                }
            }
            if (vo.getDown().intValue() == 1 && optionsMap.containsKey("downNum")){
                String downNumStr = optionsMap.get("downNum").toString();
                if (!ObjectUtils.isEmpty(downNumStr)){
                    int downNum = Integer.parseInt(downNumStr);
                    vo.setDownNum(downNum);
                    if ("0".equals(downNumStr) || downNum > vo.getNumDownload()){
                        checkDown = true;
                    }
                }else {
                    checkDown = true;
                }
            }
            if (!checkDown){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareDownExceedTips.getCode());
            }
            shareDao.updateNumDownload(vo.getShareID(), vo.getNumDownload() + 1);
        }else {
            shareDao.updateNumView(vo.getShareID(), vo.getNumView() + 1);
        }
    }
}
