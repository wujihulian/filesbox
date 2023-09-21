package com.svnlan.home.utils;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.GetM3u8NewDTO;
import com.svnlan.home.dto.GetMyM3u8DTO;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2020/9/7 10:17
 */
@Component
public class CommonConvertUtil {

    public GetM3u8NewDTO convertGetM3u8NewDTO(GetMyM3u8DTO getMyM3u8DTO){
        GetM3u8NewDTO dto = new GetM3u8NewDTO();
        String sourceType = getMyM3u8DTO.getSourceType();
        if (!ObjectUtils.isEmpty(sourceType)){
            dto.setSourceType(sourceType);
        }
        String sourceIdStr = getMyM3u8DTO.getSourceID();
        if (!ObjectUtils.isEmpty(sourceIdStr)){
            if (!StringUtil.isNumeric(sourceIdStr)){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            dto.setSourceID(Long.valueOf(sourceIdStr));
        }
        String isCamera = getMyM3u8DTO.getIsCamera();
        if (!ObjectUtils.isEmpty(isCamera)){
            if (!StringUtil.isNumeric(isCamera)){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            dto.setIsCamera(Integer.valueOf(isCamera));
        }
        String isReview = getMyM3u8DTO.getIsReview();
        if (!ObjectUtils.isEmpty(isReview)){
            if (!StringUtil.isNumeric(isReview)){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            dto.setIsReview(Integer.valueOf(isReview));
        }
        String key = getMyM3u8DTO.getKey();
        if (!ObjectUtils.isEmpty(key)){
            dto.setKey(key);
        }
        return dto;
    }

    public LoginUser getApiLoginUser(HttpServletRequest request, String token, LoginUserUtil loginUserUtil){

        return loginUserUtil.getLoginUserByToken(request, token);
    }
}
