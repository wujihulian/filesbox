package com.svnlan.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.annotation.VisitHandler;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.UserFav;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.utils.AsyncUtil;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dao.CommonInfoDao;
import com.svnlan.manage.dao.UserCommonInfoDao;
import com.svnlan.manage.domain.*;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.service.CommonInfoService;
import com.svnlan.manage.utils.DesignUtil;
import com.svnlan.manage.vo.CommonInfoVo;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 16:54
 */
@Slf4j
@Service
public class CommonInfoServiceImpl implements CommonInfoService, VisitHandler {

    @Resource
    CommonInfoDao commonInfoDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    AsyncUtil asyncUtil;
    @Resource
    UserFavDao userFavDao;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    UserDao userDao;
    @Resource
    SystemOptionDao systemOptionDao;

    private String defaultPath;

    @PostConstruct
    public void setDesignBasePath(){
        // 已改
        try {
            Properties properties = PropertiesUtil.getConfig("upconfig.properties");
            defaultPath = properties.getProperty("home.path.default");
        } catch (Exception e){
            defaultPath = "/uploads";

        }
    }

    @Value("${info.common.htmlPath.name}")
    private String infoHtmlPath;

    private Map<String, Object> searchListParamMap(CommonInfoDto infoDto, Map<String, Object> paramMap) {
        paramMap.put("infoType", infoDto.getInfoType());
        if (!StringUtils.isEmpty(infoDto.getKeyword())) {
            paramMap.put("keyword", infoDto.getKeyword());
        }
        if (!ObjectUtils.isEmpty(infoDto.getParentLevel())) {
            paramMap.put("infoTypeLevel", infoDto.getParentLevel());
        }
        if (!ObjectUtils.isEmpty(infoDto.getInfoTypeID())) {
            paramMap.put("infoTypeID", infoDto.getInfoTypeID());
        }
        if (!ObjectUtils.isEmpty(infoDto.getStatus())) {
            paramMap.put("state", infoDto.getStatus());
        }
        if (!ObjectUtils.isEmpty(infoDto.getTagID())) {
            paramMap.put("tagID", infoDto.getTagID());
        }
        return paramMap;
    }

    @Override
    public PageResult getInfoList(CommonInfoDto infoDto, LoginUser loginUser) {
        PageResult<CommonInfoVo> pageResult = new PageResult<>();
        Map<String, Object> paramMap = new HashMap<>(0);
        searchListParamMap(infoDto, paramMap);

        PageHelper.startPage(infoDto.getCurrentPage(), infoDto.getPageSize());
        List<CommonInfoVo> list = commonInfoDao.getInfoVoListByParam(paramMap);
        if (CollectionUtils.isEmpty(list)) {
            pageResult.setList(new ArrayList<>());
            pageResult.setTotal(0L);
            return pageResult;
        }

        for (CommonInfoVo vo: list){
            if (!ObjectUtils.isEmpty(vo.getThumb())){
                vo.setThumb(FileUtil.getShowImageUrl(vo.getThumb(), "info"+vo.getInfoID()+".png"));
            }
            if (!ObjectUtils.isEmpty(vo.getAvatar())){
                vo.setAvatar(FileUtil.getShowAvatarUrl(vo.getAvatar(), vo.getUserName()));
            }
        }

        PageInfo<CommonInfoVo> pageInfo = new PageInfo<>(list);
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setList(pageInfo.getList());

        return pageResult;
    }

    @Override
    public Long saveCommonInfo(CommonInfoDto infoDto, LoginUser loginUser) {

        Long infoID = 0L;

        infoDto.setInfoType(ObjectUtils.isEmpty(infoDto.getInfoType()) ? "0" : infoDto.getInfoType());
        String infoType = infoDto.getInfoType();
        checkInfoParam(infoDto);
        CommonInfo commonInfo = changeCommonInfo(infoDto, loginUser);

        Integer sort = commonInfoDao.getMaxSort(infoType);
        sort = !ObjectUtils.isEmpty(sort) ? sort + 1 : 1;
        commonInfo.setSort(sort);
        this.setAdditional(commonInfo, infoDto);

        if (!ObjectUtils.isEmpty(infoDto.getFileIds())) {
            List<Map<String, Object>> list = getMaps(infoDto);
            if (list.size() == 0) {
                commonInfo.setFileDetail("");
            } else {
                String s = JsonUtils.beanToJson(list);
                commonInfo.setFileDetail(s);
            }
        }

        commonInfo.setPathPre(ObjectUtils.isEmpty(defaultPath) ? GlobalConfig.default_disk_path_pre : defaultPath);
        try {
            commonInfoDao.insert(commonInfo);
        } catch (Exception e) {
            LogUtil.error(e, "saveCommonInfo error");
        }
        if (!ObjectUtils.isEmpty(commonInfo.getInfoID())) {
            // 关联资讯标签
            associationTag(infoDto, commonInfo.getInfoID(), false);

            infoDto.setInfoIdList(Arrays.asList(commonInfo.getInfoID()));

            if (1 == commonInfo.getStatus().intValue()) {
                // 生成静态页面
                asyncUtil.genInformationPage(infoDto, loginUser);
            }
        }

        return infoID;
    }

    //获取上传之后的附件地址
    private List<Map<String, Object>> getMaps(CommonInfoDto infoDto) {
        // 附件
        List<Long> sourceIDs = Arrays.asList(infoDto.getFileIds().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        List<CommonSource> sourceList = ioSourceDao.getSourceInfoList(sourceIDs);

        //获取附件的list
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (CommonSource commonSourceVO : sourceList) {
            map = new HashMap<>(10);
            map.put("sourceID", commonSourceVO.getSourceID());
            map.put("name", commonSourceVO.getSourceName());
            map.put("fileType", commonSourceVO.getFileType());
            map.put("type", commonSourceVO.getType());
            map.put("path", commonSourceVO.getPath());
            map.put("size", commonSourceVO.getSize());

            list.add(map);
        }
        return list;
    }

    private void setAdditional(CommonInfo commonInfo, CommonInfoDto infoDto) {
        InfoSource infoSource = new InfoSource();
        infoSource.setName(infoDto.getInfoSourceName());
        infoSource.setAuthor(infoDto.getInfoSourceAuthor());
        infoSource.setUrl(infoDto.getInfoSourceUrl());
        CommonSeo commonSeo = new CommonSeo();
        commonSeo.setKeyword(infoDto.getKeyword() == null ? "" : infoDto.getKeyword());
        commonSeo.setDescription(infoDto.getDescription() == null ? "" : infoDto.getDescription());
        commonInfo.setInfoSource(JsonUtils.beanToJson(infoSource));
        commonInfo.setIsLogin(infoDto.getIsLogin());
        commonInfo.setSeo(JsonUtils.beanToJson(commonSeo));
    }

    private void checkInfoParam(CommonInfoDto infoDto) {
        if (StringUtils.isEmpty(infoDto.getTitle())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (infoDto.getTitle().length() > 256) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(infoDto.getIntroduce()) && infoDto.getIntroduce().length() > 2000) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(infoDto.getSeo()) && infoDto.getSeo().length() > 512) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(infoDto.getInfoSource()) && infoDto.getInfoSource().length() > 1024) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(infoDto.getShowAttachment()) && infoDto.getShowAttachment().length() > 1024) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
    }

    private CommonInfo changeCommonInfo(CommonInfoDto infoDto, LoginUser loginUser) {
        CommonInfo commonInfo = new CommonInfo();
        commonInfo.setTitle(infoDto.getTitle());
        commonInfo.setInfoTypeID(ObjectUtils.isEmpty(infoDto.getInfoTypeID()) ? 0 : infoDto.getInfoTypeID());
        commonInfo.setNamePinyin(ChinesUtil.getPingYin(infoDto.getTitle()));
        commonInfo.setNamePinyinSimple(ChinesUtil.getFirstSpell(infoDto.getTitle()));
        commonInfo.setInfoType(ObjectUtils.isEmpty(infoDto.getInfoType()) ? "0" : infoDto.getInfoType());
        commonInfo.setSort(0);
        commonInfo.setStatus(1);
       /* commonInfo.setStatus(0);
        if(loginUser.getUserType().intValue() == 1){
            commonInfo.setStatus(1);
        }*/
        commonInfo.setIsTop(0);
        commonInfo.setTopTime(0L);
        commonInfo.setGmtPage(0L);
        commonInfo.setCreateUser(loginUser.getUserID());
        commonInfo.setModifyUser(loginUser.getUserID());
        commonInfo.setComputerPicPath(StringUtils.isEmpty(infoDto.getComputerPicPath()) ? "" : infoDto.getComputerPicPath());
        commonInfo.setMobilePicPath(StringUtils.isEmpty(infoDto.getMobilePicPath()) ? "" : infoDto.getMobilePicPath());
        // 内容
        commonInfo.setOriginDetail(StringUtils.isEmpty(infoDto.getDetail()) ? "" : infoDto.getDetail());
        commonInfo.setDetail("");
        //commonInfo.setDetail(StringUtils.isEmpty(infoDto.getDetail()) ? "" : infoDto.getDetail());
        commonInfo.setFileDetail(StringUtils.isEmpty(infoDto.getFileDetail()) ? "" : infoDto.getFileDetail());
        commonInfo.setUserIp(StringUtils.isEmpty(loginUser.getIp()) ? "" : loginUser.getIp());
        commonInfo.setIntroduce(StringUtils.isEmpty(infoDto.getIntroduce()) ? "" : infoDto.getIntroduce());
        commonInfo.setSeo(StringUtils.isEmpty(infoDto.getSeo()) ? "" : infoDto.getSeo());
        commonInfo.setInfoSource(StringUtils.isEmpty(infoDto.getInfoSource()) ? "" : infoDto.getInfoSource());
        commonInfo.setIsApplyOriginal(ObjectUtils.isEmpty(infoDto.getIsApplyOriginal()) ? 0 : infoDto.getIsApplyOriginal());
        commonInfo.setVideoID(ObjectUtils.isEmpty(infoDto.getVideoID()) ? 0L : infoDto.getVideoID());

        if (!ObjectUtils.isEmpty(infoDto.getThumb())){
            if (infoDto.getThumb().indexOf(GlobalConfig.private_replace_key)>=0){
                commonInfo.setThumb(infoDto.getThumb().replace(GlobalConfig.private_replace_key, "/private/"));
            }else {
                commonInfo.setThumb(infoDto.getThumb());
            }
        }
        commonInfo.setPreviewUrl(StringUtils.isEmpty(infoDto.getPreviewUrl()) ? "" : infoDto.getPreviewUrl());
        commonInfo.setIsVertical(ObjectUtils.isEmpty(infoDto.getIsVertical()) ? 0 : infoDto.getIsVertical());
        commonInfo.setIsVideoExists(ObjectUtils.isEmpty(infoDto.getIsVideoExists()) ? 0 : infoDto.getIsVideoExists());
        commonInfo.setThumbVertical(StringUtils.isEmpty(infoDto.getThumbVertical()) ? "" : infoDto.getThumbVertical());
        commonInfo.setComputerPicPathVertical(StringUtils.isEmpty(infoDto.getComputerPicPathVertical()) ? "" : infoDto.getComputerPicPathVertical());
        commonInfo.setUserAgent(UserAgentUtils.getUserAgent());
        commonInfo.setIsUrlInfo(ObjectUtils.isEmpty(infoDto.getIsUrlInfo()) ? 0 : infoDto.getIsUrlInfo());
        commonInfo.setInfoUrl(StringUtils.isEmpty(infoDto.getInfoUrl()) ? "" : infoDto.getInfoUrl());

        commonInfo.setActualViewCount(0);
        commonInfo.setViewCount(0);
        commonInfo.setRemark(StringUtils.isEmpty(infoDto.getRemark()) ? "" : infoDto.getRemark());
        commonInfo.setIsTransport(ObjectUtils.isEmpty(infoDto.getIsTransport()) ? 0 : infoDto.getIsTransport());
        commonInfo.setRightFlag(ObjectUtils.isEmpty(infoDto.getRightFlag()) ? 1 : infoDto.getRightFlag());
        commonInfo.setSourceID(ObjectUtils.isEmpty(infoDto.getSourceID()) ? 0L : infoDto.getSourceID());
        commonInfo.setIsHide(ObjectUtils.isEmpty(infoDto.getIsHide()) ? 0 : infoDto.getIsHide());
        commonInfo.setAttachmentCount(ObjectUtils.isEmpty(infoDto.getAttachmentCount()) ? 0 : infoDto.getAttachmentCount());
        commonInfo.setShowAttachment(ObjectUtils.isEmpty(infoDto.getShowAttachment()) ? "" : infoDto.getShowAttachment());

        return commonInfo;
    }

    @Override
    public void deleteInfo(CommonInfoDto infoDto, LoginUser loginUser) {

        if (ObjectUtils.isEmpty(infoDto.getInfoID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            commonInfoDao.deleteInfo(infoDto.getInfoID(), loginUser.getUserID());
        } catch (Exception e) {
            LogUtil.error(e, "deleteInfo error");
        }
        try {
            this.delInfoFile(infoDto.getInfoID());
        } catch (Exception e) {
            LogUtil.error(e, "删除资讯文件失败, infoDto=" + JsonUtils.beanToJson(infoDto));
        }
    }

    public void delInfoFile(Long infoID) {
        /*
         /uploads/design/pubinfo/pc/pubinfo/30035.shtml
         /uploads/design/home/filesbox/pc/information/__detail_30035__.txt
          * */
        CommonInfo info = commonInfoDao.getInfoById(infoID);

        String firstPath = ObjectUtils.isEmpty(info.getPathPre()) ? GlobalConfig.default_disk_path_pre : info.getPathPre();
        try {
            //客户端类型循环
            for (String clientType : GlobalConfig.DESIGN_WEB_CLIENT_TYPE_ARR) {
                String pubPath = firstPath + "/design/pubinfo/" + clientType + "/pubinfo/" + infoID + ".shtml";
                new File(pubPath).delete();
            }
        } catch (Exception e) {
            LogUtil.error(e, "删除资讯文件失败 infoID=" + infoID);
        }
        try {
            String detailFilePath = firstPath + "/design/home/" + infoHtmlPath + "/pc/information/__detail_" + infoID + "__.txt";
            new File(detailFilePath).delete();
        } catch (Exception e) {
            LogUtil.error(e, "删除资讯文件失败 infoID=" + infoID);
        }
    }

    @Override
    public Long editCommonInfo(CommonInfoDto infoDto, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(infoDto.getInfoID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long infoID = infoDto.getInfoID();
        checkInfoParam(infoDto);
        CommonInfo commonInfo = commonInfoDao.getInfoById(infoID);
        if (ObjectUtils.isEmpty(commonInfo) || 2 == commonInfo.getStatus().intValue()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        this.changeEditCommonInfo(infoDto, commonInfo, loginUser);
        this.setAdditional(commonInfo, infoDto);

        if (!ObjectUtils.isEmpty(infoDto.getFileIds())) {
            List<Map<String, Object>> list = getMaps(infoDto);
            if (list.size() == 0) {
                commonInfo.setFileDetail("");
            } else {
                String s = JsonUtils.beanToJson(list);
                commonInfo.setFileDetail(s);
            }
        }

        commonInfo.setPathPre(ObjectUtils.isEmpty(defaultPath) ? GlobalConfig.default_disk_path_pre : defaultPath);
        try {
            commonInfoDao.updateInfo(commonInfo);
        } catch (Exception e) {
            LogUtil.error(e, "更新资讯失败");
        }

        if (!ObjectUtils.isEmpty(commonInfo.getInfoID())) {

            // 关联资讯标签
            associationTag(infoDto, commonInfo.getInfoID(), true);


            infoDto.setInfoIdList(Arrays.asList(commonInfo.getInfoID()));
            // 生成静态页面
            asyncUtil.genInformationPage(infoDto, loginUser);
        }

        return infoID;
    }

    private CommonInfo changeEditCommonInfo(CommonInfoDto infoDto, CommonInfo commonInfo, LoginUser loginUser) {

        commonInfo.setTitle(infoDto.getTitle());
        commonInfo.setNamePinyin(ChinesUtil.getPingYin(infoDto.getTitle()));
        commonInfo.setNamePinyinSimple(ChinesUtil.getFirstSpell(infoDto.getTitle()));
        commonInfo.setModifyUser(loginUser.getUserID());
        if (!ObjectUtils.isEmpty(infoDto.getInfoTypeID())) {
            commonInfo.setInfoTypeID(infoDto.getInfoTypeID());
        }
        commonInfo.setComputerPicPath(ObjectUtils.isEmpty(infoDto.getComputerPicPath()) ? "" : infoDto.getComputerPicPath());
        commonInfo.setMobilePicPath(ObjectUtils.isEmpty(infoDto.getMobilePicPath()) ? "" : infoDto.getMobilePicPath());
        commonInfo.setFileDetail(ObjectUtils.isEmpty(infoDto.getFileDetail()) ? "" : infoDto.getFileDetail());
        commonInfo.setIntroduce(ObjectUtils.isEmpty(infoDto.getIntroduce()) ? "" : infoDto.getIntroduce());
        commonInfo.setInfoSource(ObjectUtils.isEmpty(infoDto.getInfoSource()) ? "" : infoDto.getInfoSource());
        commonInfo.setIsApplyOriginal(ObjectUtils.isEmpty(infoDto.getIsApplyOriginal()) ? 0 : infoDto.getIsApplyOriginal());
        commonInfo.setVideoID(ObjectUtils.isEmpty(infoDto.getVideoID()) ? 0L : infoDto.getVideoID());
        if (!ObjectUtils.isEmpty(infoDto.getThumb())){
            if (infoDto.getThumb().indexOf(GlobalConfig.private_replace_key)>=0){
                infoDto.setThumb(infoDto.getThumb().replace(GlobalConfig.private_replace_key, "/private/"));
            }else {
                infoDto.setThumb(infoDto.getThumb());
            }
        }
        commonInfo.setThumb(ObjectUtils.isEmpty(infoDto.getThumb()) ? "" : infoDto.getThumb());

        commonInfo.setPreviewUrl(ObjectUtils.isEmpty(infoDto.getPreviewUrl()) ? "" : infoDto.getPreviewUrl());
        commonInfo.setIsVertical(ObjectUtils.isEmpty(infoDto.getIsVertical()) ? 0 : infoDto.getIsVertical());
        commonInfo.setIsVideoExists(ObjectUtils.isEmpty(infoDto.getIsVideoExists()) ? 0 : infoDto.getIsVideoExists());
        commonInfo.setThumbVertical(ObjectUtils.isEmpty(infoDto.getThumbVertical()) ? "" : infoDto.getThumbVertical());
        commonInfo.setComputerPicPathVertical(ObjectUtils.isEmpty(infoDto.getComputerPicPathVertical()) ? "" : infoDto.getComputerPicPathVertical());
        commonInfo.setIsUrlInfo(ObjectUtils.isEmpty(infoDto.getIsUrlInfo()) ? 0 : infoDto.getIsUrlInfo());
        commonInfo.setInfoUrl(ObjectUtils.isEmpty(infoDto.getInfoUrl()) ? "" : infoDto.getInfoUrl());
        commonInfo.setRemark(ObjectUtils.isEmpty(infoDto.getRemark()) ? "" : infoDto.getRemark());
        commonInfo.setIsTransport(ObjectUtils.isEmpty(infoDto.getIsTransport()) ? 0 : infoDto.getIsTransport());
        commonInfo.setRightFlag(ObjectUtils.isEmpty(infoDto.getRightFlag()) ? 1 : infoDto.getRightFlag());
        commonInfo.setSourceID(ObjectUtils.isEmpty(infoDto.getSourceID()) ? 0L : infoDto.getSourceID());
        if (!ObjectUtils.isEmpty(infoDto.getIsHide())) {
            commonInfo.setIsHide(ObjectUtils.isEmpty(infoDto.getIsHide()) ? 0 : infoDto.getIsHide());
        }
        commonInfo.setShowAttachment(ObjectUtils.isEmpty(infoDto.getShowAttachment()) ? "" : infoDto.getShowAttachment());
        return commonInfo;
    }

    /**
     * 关联资讯标签
     */
    private void associationTag(CommonInfoDto infoDto, Long infoID, boolean isDel) {
        if (isDel) {
            userFavDao.removeInfoTagByID(infoID.toString());
        }
        if (!ObjectUtils.isEmpty(infoDto.getTagIds())) {
            Set<Integer> tagIds = Arrays.asList(infoDto.getTagIds().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(Integer::valueOf).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(tagIds)) {
                List<UserFav> list = new ArrayList<>();
                UserFav userFav = null;
                Integer tagSort = 0;
                int i = 1;
                for (Integer tagID : tagIds) {
                    userFav = new UserFav(0L, tagID, "", infoID.toString(), "info", tagSort + i);
                    i++;
                    list.add(userFav);
                }
                try {
                    userFavDao.batchInsert(list);
                } catch (Exception e) {
                    LogUtil.error(e, " 关联资讯标签失败");
                }
            }
        }
    }

    @Override
    public CommonInfoVo getCommonInfo(CommonInfoDto infoDto, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(infoDto.getInfoID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long infoID = infoDto.getInfoID();
        CommonInfoVo info = commonInfoDao.getInfoVoById(infoID);
        if (ObjectUtils.isEmpty(info) || 2 == info.getStatus().intValue()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String detail = DesignUtil.getDetail(info, infoHtmlPath);
        info.setDetail(detail);
        // 附件
        this.getFileDetail(info);
        // seo 、来源
        this.getAdditional(info);

        // 标签
        List<CommonLabelVo> tagList = userFavDao.geTagListByInfoID(String.valueOf(infoID));
        info.setTagList(!CollectionUtils.isEmpty(tagList) ? tagList : new ArrayList<>());


        if (!ObjectUtils.isEmpty(info.getThumb())){
            info.setPreviewPath(FileUtil.getShowImageUrl(info.getThumb(), "info"+info.getInfoID()+""));
        }
        return info;
    }


    private void getAdditional(CommonInfoVo commonInfo) {
        commonInfo.setInfoSourceName("");
        commonInfo.setInfoSourceAuthor("");
        commonInfo.setInfoSourceUrl("");
        commonInfo.setKeyword("");
        commonInfo.setDescription("");
        if (!ObjectUtils.isEmpty(commonInfo.getInfoSource())) {
            InfoSource infoSource = JsonUtils.jsonToBean(commonInfo.getInfoSource(), InfoSource.class);
            if (!ObjectUtils.isEmpty(infoSource)) {
                commonInfo.setInfoSourceName(!ObjectUtils.isEmpty(infoSource.getName()) ? infoSource.getName() : "");
                commonInfo.setInfoSourceAuthor(!ObjectUtils.isEmpty(infoSource.getAuthor()) ? infoSource.getAuthor() : "");
                commonInfo.setInfoSourceUrl(!ObjectUtils.isEmpty(infoSource.getUrl()) ? infoSource.getUrl() : "");
            }
        }
        if (!ObjectUtils.isEmpty(commonInfo.getSeo())) {
            CommonSeo commonSeo = JsonUtils.jsonToBean(commonInfo.getSeo(), CommonSeo.class);
            if (!ObjectUtils.isEmpty(commonSeo)) {
                commonInfo.setDescription(!ObjectUtils.isEmpty(commonSeo.getDescription()) ? commonSeo.getDescription() : "");
                commonInfo.setKeyword(!ObjectUtils.isEmpty(commonSeo.getKeyword()) ? commonSeo.getKeyword() : "");
            }
        }
    }

    public void getFileDetail(CommonInfoVo info) {
        if (!"".equals(info.getFileDetail())) {
            String fileDetail = info.getFileDetail();
            List<Map<String, Object>> list = JsonUtils.jsonToBean(fileDetail, List.class);
            info.setFileDetailList(list);
        } else {
            info.setFileDetailList(null);
        }
    }

    @Override
    public CommonInfoVo getHomepageDetail(CommonInfoDto infoDto, LoginUser loginUser) {
////        //每次点击的时候添加一次浏览次数
//        commonHomepageInfoDao.insertViewCount(homepageInfoId);

        if (!ObjectUtils.isEmpty(loginUser) && 1 != loginUser.getUserType()){
            String treeOpen = systemOptionDao.getSystemConfigByKey("treeOpen");

            // my 个人空间,myFav 收藏夹,rootGroup 企业云盘,recentDoc 最近文档,fileType 文件类型,fileTag 标签,shareLink 外链分享
            // my,myFav,rootGroup,recentDoc,fileType,fileTag,shareLink
            List<String> treeOpenList = ObjectUtils.isEmpty(treeOpen) ? new ArrayList<>() : Arrays.asList(treeOpen.split(",")).stream().map(String::valueOf).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(treeOpenList) || !treeOpenList.contains("information")) {
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionRead.getCode());
            }
            UserVo userVo = userDao.getUserInfo(loginUser.getUserID());
            // 校验权限
            if ((ObjectUtils.isEmpty(userVo.getAuth()) || userVo.getAuth().indexOf("explorer.informationView") < 0)){
                // 资讯
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionRead.getCode());
            }
        }
        Long infoID = infoDto.getInfoID();
        //由ID得对应的信息
        CommonInfoVo homepageDetail = commonInfoDao.getInfoVoById(infoID);

        //判断: 查询无结果
        if (null == homepageDetail) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        // 判断该资讯是否需要登录
        if (Objects.equals(homepageDetail.getIsLogin(), 1) && Objects.isNull(loginUser)) {
            // 表示需要登录,且当前是匿名访问
            throw new SvnlanRuntimeException(CodeMessageEnum.bindSignError.getCode());
        }
        //以防无缓存，则从DB拉置缓存
        //this.commonTool.findHomepageViewCount(homepageInfoId);
        //某资讯的浏览数缓存自增1
        //Integer viewCount = this.commonTool.increaseViewCountForHomePage(homepageInfoId);


        String detail = DesignUtil.getDetail(homepageDetail, infoHtmlPath);
        homepageDetail.setDetail(detail);
        String fileDetail = homepageDetail.getFileDetail();
        if (fileDetail != null && !"".equals(fileDetail) && !"[]".equals(fileDetail)) {
            List<CommonSource> commonHomepageFileDetailVOS = JsonUtils.stringToList(fileDetail, CommonSource.class);
            if (!CollectionUtils.isEmpty(commonHomepageFileDetailVOS)) {
                List<Long> collect = commonHomepageFileDetailVOS.stream().map(CommonSource::getSourceID).collect(Collectors.toList());
                int sourceType = BusTypeEnum.HOMEPAGE_ATTACHMENT.getTypeCode();
                List<CommonSource> commonSources = ioSourceDao.getSourceInfoList(collect);
                for (CommonSource commonSource : commonSources) {
                    String downloadKey = FileUtil.getDownloadKey();
                    String downloadUrl = "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(commonSource.getSourceName())
                            + this.getDownloadParam(homepageDetail.getInfoID(), commonSource.getSourceID(), downloadKey, BusTypeEnum.HOMEPAGE_ATTACHMENT.getBusType());
                    commonSource.setPreviewUrl(downloadUrl);

                    // 预览地址
                    String docDownloadUrl = commonSource.getPath();
                    String checkSum = commonSource.getHashMd5();
                    //
                    String docPreviewUrl = this.fileOptionTool.getPptPreviewUrl(docDownloadUrl, checkSum, Boolean.FALSE);
                    commonSource.setDocPreviewUrl(docPreviewUrl);
                }
                homepageDetail.setFileAttList(commonSources);
            }
        }
        // 附件
        //this.getFileDetail(homepageDetail);
        // seo 、来源
        this.getAdditional(homepageDetail);

        // 标签
        List<CommonLabelVo> tagList = userFavDao.geTagListByInfoID(String.valueOf(infoID));
        homepageDetail.setTagList(!CollectionUtils.isEmpty(tagList) ? tagList : new ArrayList<>());

        homepageDetail.setIsUrlInfo(ObjectUtils.isEmpty(homepageDetail.getIsUrlInfo()) ? 0 : homepageDetail.getIsUrlInfo());
        homepageDetail.setInfoUrl(ObjectUtils.isEmpty(homepageDetail.getInfoUrl()) ? "" : homepageDetail.getInfoUrl());
        //处理新加的短视频的响应参数
        this.handleHomepageVideoDisplay(homepageDetail);

        // 查询当前登录人是否已经点赞了该资讯
        LambdaQueryWrapper<UserCommonInfo> queryWrapper = new LambdaQueryWrapper<UserCommonInfo>()
                .eq(UserCommonInfo::getInfoId, homepageDetail.getInfoID());
        if (Objects.isNull(loginUser)) {
            // 匿名用户
            queryWrapper.eq(UserCommonInfo::getIp, getRequestIp());
        } else {
            queryWrapper.eq(UserCommonInfo::getUserId, loginUser.getUserID());
        }
        UserCommonInfo userCommonInfo = userCommonInfoDao.selectOne(queryWrapper);
        if (Objects.nonNull(userCommonInfo)) {
            homepageDetail.setIsLike(userCommonInfo.getIsLike());
        } else {
            homepageDetail.setIsLike(0);
        }
        return homepageDetail;
    }

    private String getDownloadParam(Long commonHomepageInfoId, Long sourceId, String downloadKey, String busType) {
        return "?busId=" + sourceId + "&busType=" + busType + "&sourceId=" + commonHomepageInfoId + "&key=" + downloadKey;
    }

    /**
     * @param
     * @param
     * @return void
     * @description: 处理新加的短视频的响应参数
     */
    private void handleHomepageVideoDisplay(CommonInfoVo commonHomepageInfoVO) {
        //处理视频的m3u8地址
        String previewUrl = commonHomepageInfoVO.getPreviewUrl();
        if (commonHomepageInfoVO.getVideoID() > 0 && !StringUtil.isEmpty(previewUrl)) {
            String videoUrl = MediaTool.getM3u8Url("homepage",
                    commonHomepageInfoVO.getInfoID(), commonHomepageInfoVO.getVideoID(),
                    BusTypeEnum.HOMEPAGE_VIDEO.getBusType());
            commonHomepageInfoVO.setVideoUrl(videoUrl);
        }
    }

    @Override
    public CommonInfoVo homePageInfoToTop(CommonInfoDto commonInfoDto) {
        if (commonInfoDto.getInfoID() == null || commonInfoDto.getInfoID() <= 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        CommonInfoVo tempHomepageInfo = commonInfoDao.getInfoVoById(commonInfoDto.getInfoID());
        if (null == tempHomepageInfo) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("infoID", commonInfoDto.getInfoID());
        paramMap.put("isTop", Boolean.FALSE.equals(commonInfoDto.getCancelTop()) ? 0 : 1);
        commonInfoDao.setTop(paramMap);
        return tempHomepageInfo;
    }

    @Override
    public void modifyPageinfoSort(LoginUser loginUser, CommonInfoDto modifyPageinfoSortDTO) {
        if (modifyPageinfoSortDTO.getTargetPos() == null) {
            return;
        }
        //媒体号类型：0资讯, 1短视频
        String infoType = modifyPageinfoSortDTO.getInfoType();
        if (StringUtil.isEmpty(infoType)) {
            infoType = "0";
        }
        modifyPageinfoSortDTO.setInfoType(infoType);

        if (modifyPageinfoSortDTO.getTargetPos().equals(0)) {
            // 排序的目标位置不能是0 只能是-1, 或者 >= 1的数
            LogUtil.info("排序参数不正确");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long infoID = modifyPageinfoSortDTO.getInfoID();
        //
        CommonInfoVo regionHomepageInfo = commonInfoDao.getInfoVoById(infoID);
        if (null == regionHomepageInfo) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //  所有的资讯排序
        List<CommonInfoVo> list = commonInfoDao.findHomePageListBySIdPId(infoType);
        if (CollectionUtils.isEmpty(list)) {
            LogUtil.info("资讯为空");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        int sort = regionHomepageInfo.getSort();

        List<CommonInfoVo> updateInfoList = new ArrayList<>(list.size());
        //查询最后一个元素的sort是不是0,如果是0 则初始化
        if (list.get(list.size() - 1).getSort() == 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSort(i + 1);
                if (list.get(i).getInfoID().longValue() == infoID) {
                    sort = i + 1;
                }
            }
            try {
                //批量更新初始化
                commonInfoDao.batchUpdateSort(list);
            } catch (Exception e) {
                LogUtil.error(e);
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
            }

        }
        //当前位置跟传入的位置对比
        if (sort == (modifyPageinfoSortDTO.getTargetPos().intValue())) {
            // origin = target
            return;
        } else if (modifyPageinfoSortDTO.getTargetPos() != -1 && sort > modifyPageinfoSortDTO.getTargetPos()) {
            // origin > target
            for (CommonInfoVo vo : list) {
                if (vo.getSort() < sort && vo.getSort() >= modifyPageinfoSortDTO.getTargetPos()) {
                    vo.setSort(vo.getSort() + 1); // 小于当前课程的sort, 大于等于目标sort的课程都sort + 1
                    updateInfoList.add(vo);
                }
            }
            regionHomepageInfo.setSort(modifyPageinfoSortDTO.getTargetPos());
            updateInfoList.add(regionHomepageInfo);
        } else {
            // origin < target
            if (modifyPageinfoSortDTO.getTargetPos().equals(-1)) { // 直接改为最后一个的话，将targetPos设置为当前最大sort值
                int maxSort = list.get(list.size() - 1).getSort();
                modifyPageinfoSortDTO.setTargetPos(maxSort);
            }
            for (CommonInfoVo vo : list) {
                if (sort < vo.getSort() && vo.getSort() <= modifyPageinfoSortDTO.getTargetPos()) {
                    vo.setSort(vo.getSort() - 1);
                    updateInfoList.add(vo);
                }
            }
            regionHomepageInfo.setSort(modifyPageinfoSortDTO.getTargetPos());
            updateInfoList.add(regionHomepageInfo);
        }
        // 批量更新课程的排序字段，实际上需要跟新的就是sort位于 origin,target之间的课程的sort值
        int u = commonInfoDao.batchUpdateSort(updateInfoList);
        if (u <= 0) {
            LogUtil.error("modifyPageinfoSort failed. affected rows 0. sql data : {}", JSON.toJSONString(updateInfoList));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
    }

    @Override
    public List<Integer> getHomepagePageSort(CommonInfoDto dto, LoginUser loginUser) {

        if (dto.getTotal() == 0 || dto.getCurrentPage() == 0) {
            return new ArrayList<>();
        }

        //媒体号类型：0资讯, 1短视频
        String infoType = dto.getInfoType();
        if (StringUtil.isEmpty(infoType)) {
            infoType = "0";
        }
        if (!Arrays.asList("0", "1").contains(infoType)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        dto.setInfoType(infoType);

        Map<String, Integer> neighbourMap = getNeighbourPageBoundry(dto.getTotal(), dto.getCurrentPage(), dto.getPageSize());
        Map<String, Object> sortMap = new HashMap<>(8);

        sortMap.put("offset", neighbourMap.get("offset"));
        sortMap.put("length", neighbourMap.get("length"));
        searchListParamMap(dto, sortMap);


        List<CommonInfoVo> list = commonInfoDao.getInfoVoSortListByParam(sortMap);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(CommonInfoVo::getSort).collect(Collectors.toList());
    }

    public Map<String, Integer> getNeighbourPageBoundry(long total, int page, int pageSize) {
        int maxPage = (int) (total / (long) pageSize + (long) (total % (long) pageSize == 0L ? 0 : 1));
        Map<String, Integer> map = new HashMap<>(2);
        if (maxPage == 0 || page == 0) { // 最大页是0，证明没有数据，这时候page=0
            map.put("offset", 0);
            map.put("length", pageSize);
            return map;
        }
        LogUtil.info("maxPage {}" + maxPage);
        int offset; // 前后页的数据
        int length;

        if (page == 1 || page == maxPage) {
            offset = page == 1 ? 0 : (page - 2) * pageSize;
            length = pageSize * 2;
        } else {
            offset = (page - 2) * pageSize;
            length = pageSize * 3;
        }
        LogUtil.info(" page " + page + ", pageSize " + pageSize + "  : offset " + offset + ", length  " + length);
        if (offset < 0) {
            offset = 0;
        }

        map.put("offset", offset);
        map.put("length", length);
        return map;
    }

    public void checkVerifyAuthAndState(CommonInfoVo commonHomepageInfo) {
        if (0 != commonHomepageInfo.getStatus()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
    }


    public static final Integer STATE_UNABLE = 0; //未启用
    public static final Integer STATE_NORMAL = 1; //已发布
    public static final Integer STATE_WAIT_VERIFY = 3; //待审核
    public static final Integer STATE_VERIFY_DENY = 4; //审核不通过

    @Override
    public CommonInfoVo verifyHomepageInfo(CommonInfoDto dto, LoginUser loginUser) {
        if (dto.getInfoID() == null || dto.getInfoID() <= 0 || ObjectUtils.isEmpty(dto.getVerifyState())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long infoID = dto.getInfoID();
        CommonInfoVo commonInfoVo = commonInfoDao.getInfoVoById(infoID);
        if (null == commonInfoVo) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        checkVerifyAuthAndState(commonInfoVo);


        CommonInfoVo commonHomepageInfoUpdate = new CommonInfoVo();
        //审核处理
        if (dto.getVerifyState().intValue() == 1) {
            //审核通过
            commonHomepageInfoUpdate.setStatus(STATE_NORMAL);
            //int result = this.commonInfoDao.verifyOnePageInfo(commonHomepageInfoId, commonHomepageInfoUpdate.getState(), commonHomepageInfo.getState(), commonHomepageInfo.getGmtModified(), "");

        } else {
            //审核不通过
            /*commonHomepageInfoUpdate.setStatus(STATE_VERIFY_DENY);
            int result = this.commonHomepageInfoDao.verifyOnePageInfo(commonHomepageInfoId, commonHomepageInfoUpdate.getState(), commonHomepageInfo.getState(),
                    commonHomepageInfo.getGmtModified(), null == verifyHomepageInfoDTO.getRefuseReason()? "": verifyHomepageInfoDTO.getRefuseReason());
            //审核不通过可再次审核
            if (result == 0) {

            }*/
        }

        return commonHomepageInfoUpdate;
    }

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private UserCommonInfoDao userCommonInfoDao;

    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Override
    public void handle(Object value) {
        Long id = (Long) value;
        Long userId = loginUserUtil.getLoginUserId(false);
        String ip = getRequestIp();
//        log.info("handler value => {} userId => {}", id, userId);
        asyncTaskExecutor.execute(() -> updateVisitCount(id, userId, ip));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateVisitCount(Long id, Long userId, String ip) {
        boolean isAnonymous = false;
        UserCommonInfo originUserCommonInfo;
        if (Objects.equals(userId, 0L)) {
            // 说明是匿名用户
            originUserCommonInfo = userCommonInfoDao.selectOne(new LambdaQueryWrapper<UserCommonInfo>()
                    .eq(UserCommonInfo::getInfoId, id)
                    .eq(UserCommonInfo::getIp, ip)
                    .last(" limit 1"));
            isAnonymous = true;
        } else {
            originUserCommonInfo = userCommonInfoDao.selectOne(new LambdaQueryWrapper<UserCommonInfo>()
                    .eq(UserCommonInfo::getInfoId, id)
                    .eq(UserCommonInfo::getUserId, userId)
                    .last(" limit 1"));
        }

        if (Objects.isNull(originUserCommonInfo)) {
            UserCommonInfo userCommonInfoInsert = isAnonymous ? new UserCommonInfo(ip, id) : new UserCommonInfo(userId, id);
            userCommonInfoInsert.setViewCount(1);
            Assert.isTrue(Objects.equals(userCommonInfoDao.insert(userCommonInfoInsert), 1), "新增 viewCount 失败");
        } else {
            Assert.isTrue(Objects.equals(userCommonInfoDao.updateViewCount(id, ip, Objects.equals(userId, 0L) ? null : userId), 1), "更新 viewCount 失败");
        }

        // 更新资源表
        Assert.isTrue(Objects.equals(commonInfoDao.updateViewCountById(id), 1), "更新 common_info viewCount 失败");
    }


    @Override
    public void operateLike(Long infoId, Integer isLike) {
        Long userId = loginUserUtil.getLoginUserId(false);
        if (Objects.equals(userId, 0L)) {
            // 表示是匿名用户
            String ip = getRequestIp();
            // 先查询是否存在记录
            doOperateLike(infoId, UserCommonInfo::getIp, ip, isLike);
        } else {
            // 表示是登录用户
            doOperateLike(infoId, UserCommonInfo::getUserId, userId, isLike);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doOperateLike(Long infoId, SFunction<UserCommonInfo,?> column, Object columnValue, Integer isLike) {
        UserCommonInfo originCommonInfo = userCommonInfoDao.selectOne(new LambdaQueryWrapper<UserCommonInfo>()
                .eq(UserCommonInfo::getInfoId, infoId)
                .eq(column, columnValue));
        if (Objects.nonNull(originCommonInfo)) {
            userCommonInfoDao.updateLikeById(originCommonInfo.getId(), isLike);
        } else {
            UserCommonInfo userCommonInfo;
            if (columnValue instanceof Long) {
                 userCommonInfo = new UserCommonInfo(((Long) columnValue), infoId);
            } else if (columnValue instanceof String) {
                 userCommonInfo = new UserCommonInfo(((String) columnValue), infoId);
            }else {
                throw new IllegalArgumentException("columnValue 类型只能为 Long 或者 String");
            }
            userCommonInfo.setIsLike(isLike);
            userCommonInfoDao.insert(userCommonInfo);
        }
        commonInfoDao.updateLikeCountById(infoId, isLike);
    }

    /**
     * 获取请求的 ip
     */
    private String getRequestIp() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return IpUtil.getIp(requestAttributes.getRequest());
    }
}
