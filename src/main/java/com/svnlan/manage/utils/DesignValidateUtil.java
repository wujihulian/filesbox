package com.svnlan.manage.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.ClientTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.manage.dao.CommonDesignDao;
import com.svnlan.manage.domain.CommonDesign;
import com.svnlan.manage.enums.DesignClientTypeEnum;
import com.svnlan.manage.enums.DesignTypeEnum;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description:
 */
@Component
public class DesignValidateUtil {
    @Resource
    private CommonDesignDao commonDesignDao;
    @Resource
    private IoSourceDao ioSourceDao;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static final String os = System.getProperty("os.name");

    @Value("${design.subPage.limitCount}")
    private Integer subPageLimit;



    /**
     * @Description: 验证装扮数量
     * @params:  [commonDesign]
     * @Return:  void
     * @Modified:
     */
    public void checkDesignCount(CommonDesign commonDesign) {

        //是否二级页
        Boolean isSubPage = commonDesign.getDesignType().equals(DesignTypeEnum.SUB.getCode());
        Boolean isPic = commonDesign.getDesignType().equals(DesignTypeEnum.PIC.getCode());
        Integer count = commonDesignDao.getCountByClientType(commonDesign);
        //非二级页的, 小程序200个，其他100个
        if ((commonDesign.getClientType().equals(DesignClientTypeEnum.MP.getCode()) && count >= 200 ||
                !commonDesign.getClientType().equals(DesignClientTypeEnum.MP.getCode()) && count >= GlobalConfig.MaxDesignCount.intValue())
                && !isSubPage && !isPic){
            throw new SvnlanRuntimeException(CodeMessageEnum.EXCEEDS_LIMIT.getCode());
        }
        //二级页, 限制个数验证
        if (isSubPage && count >= subPageLimit){
            throw new SvnlanRuntimeException(CodeMessageEnum.EXCEEDS_LIMIT.getCode());
        }
    }


    /**
     * @Description: 验证二级页url格式和是否重复
     * @params:  [commonDesign]
     * @Return:  boolean
     * @Modified:
     */
    private boolean checkUrl(CommonDesign commonDesign) {
        if (StringUtil.isEmpty(commonDesign.getUrl())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        if (commonDesign.getUrl().length() > 128){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        if (!RegUtil.onlyNumAndWord(commonDesign.getUrl()) || commonDesign.getUrl().equals("index")){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        CommonDesign existUrlDesign = commonDesignDao.geForExistsUrl(commonDesign);
        if (existUrlDesign != null){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        return true;
    }
    /**
     * @Description: 验证url
     * @params:  [url, commonDesign]
     * @Return:  java.lang.String
     * @Modified:
     */
    public String checkUrlEntry(String url, CommonDesign commonDesign) {
        //url 锁
        String urlLockKey = this.getUrlLockKey(commonDesign.getClientType(), url);
        if (!stringRedisTemplate.opsForValue().setIfAbsent(urlLockKey, "1", GlobalConfig.DESIGN_URL_LOCK_TTL, TimeUnit.MILLISECONDS)){
            throw new SvnlanRuntimeException(CodeMessageEnum.designUrlLocked.getCode());
        }
        commonDesign.setUrl(url);
        //验证url
        checkUrl(commonDesign);
        return urlLockKey;
    }
    public String getUrlLockKey(String clientType, String url){
        return GlobalConfig.DESIGN_URL_LOCK_PRE + "_" +  clientType + "_" + url;
    }

    /**
     * @Description: 从网校detail获取seo信息
     * @params:  [schoolDetail]
     * @Return:  java.lang.String[]
     * @Modified:
     */
    public String[] getSEOInfo(Map<String, Object> detailMap, CommonDesign commonDesign, CommonDesign mainPage) {
        String seoKeyword = null;
        String seoDescription = null;
        //二级页先取自身seo信息, 没有的话取主页
        String[] seoInfo = DesignUtil.getSEOInfoByDesign(commonDesign);
        if (seoInfo == null){
            seoInfo = DesignUtil.getSEOInfoByDesign(mainPage);
        }
        if (seoInfo != null){
            return seoInfo;
        }
        //主页没有取网校, 网校还没有, 就用固定的
        seoKeyword = detailMap.get("seoKeyword") == null ? "" : detailMap.get("seoKeyword").toString();
        seoDescription = detailMap.get("seoDescription") == null ? "" : detailMap.get("seoDescription").toString();
        return new String[]{seoKeyword, seoDescription};
    }

}
