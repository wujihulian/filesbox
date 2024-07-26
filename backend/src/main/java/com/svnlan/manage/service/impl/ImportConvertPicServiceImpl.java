package com.svnlan.manage.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.utils.AsyncConvertPicUtil;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.service.ImportConvertPicService;
import com.svnlan.manage.utils.BanWordsUtil;
import com.svnlan.manage.vo.ConvertToJPGDTO;
import com.svnlan.manage.vo.ConvertToPDFMsgDTO;
import com.svnlan.manage.vo.ConvertToPicResultDTO;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/30 13:38
 */
@Service
public class ImportConvertPicServiceImpl implements ImportConvertPicService {

    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    AsyncConvertPicUtil asyncConvertPicUtil;

    @Override
    public void convertToPic(String prefix, Long sourceID, LoginUser loginUser) {
        //
        ValidateDUtil.validateObjectNotEmpty(sourceID);
        //
        CommonSource commonSource = this.fileOptionTool.getSourceInfo(sourceID);
        if(null == commonSource) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        //设置缓存
        String cacheKey = String.format(GlobalConfig.infoConvertToJPGKey, sourceID);
        ConvertToJPGDTO convertToJPGDTO = new ConvertToJPGDTO("0", null);
        //进行中
        this.stringRedisTemplate.opsForValue().set(cacheKey, JsonUtils.beanToJson(convertToJPGDTO), 20, TimeUnit.MINUTES);

        ConvertToPDFMsgDTO msgDTO = new ConvertToPDFMsgDTO(RandomUtil.getuuid(), DateUtil.getCurrentTime(), sourceID);
        //
        asyncConvertPicUtil.asyncConvertToJPG(msgDTO, commonSource);
    }

    @Override
    public ConvertToPicResultDTO getConvertPicResult(String prefix, Long sourceID, LoginUser loginUser) {
        //
        ValidateDUtil.validateObjectNotEmpty(sourceID);
        String cacheKey = String.format(GlobalConfig.infoConvertToJPGKey, sourceID);
        String resultJson = this.stringRedisTemplate.opsForValue().get(cacheKey);
        if(StringUtil.isEmpty(resultJson)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        ConvertToJPGDTO convertToJPGDTO = JsonUtils.jsonToBean(resultJson, ConvertToJPGDTO.class);
        String status = convertToJPGDTO.getStatus();
        String lastImagePath = convertToJPGDTO.getLastImagePath();
        List<String> list = new ArrayList<>();
        if("1".equals(status)) {
            int index = lastImagePath.lastIndexOf("@");
            int suffixIndex = lastImagePath.lastIndexOf(".");
            int imageCount = Integer.parseInt(lastImagePath.substring(index + 1, suffixIndex));
            String preStr = lastImagePath.substring(0, index + 1);
            for (int i = 1; i <= imageCount; i++) {
                list.add(preStr + i + ".jpg");
            }
        }
        ConvertToPicResultDTO resultDTO = new ConvertToPicResultDTO();
        resultDTO.setStatus(status);
        resultDTO.setList(list);
        return resultDTO;
    }

    @Override
    public List<String> checkBanWord(CommonInfoDto infoDto){
        if (ObjectUtils.isEmpty(infoDto.getDetail())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> banWordList = null;
        try {
            banWordList = BanWordsUtil.searchBanWords(infoDto.getDetail());
        }catch (Exception e){
            LogUtil.error(e, " checkBanWord Exception ");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        return CollectionUtils.isEmpty(banWordList) ? new ArrayList<>() : banWordList;
    }
}
