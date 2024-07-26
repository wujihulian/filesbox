package com.svnlan.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.manage.dao.CommonDesignDao;
import com.svnlan.manage.domain.CommonDesign;
import com.svnlan.manage.domain.CommonDesignList;
import com.svnlan.manage.dto.*;
import com.svnlan.manage.enums.DesignClientTypeEnum;
import com.svnlan.manage.enums.DesignTypeEnum;
import com.svnlan.manage.enums.DesignUsedEnum;
import com.svnlan.manage.service.CommonDesignService;
import com.svnlan.manage.utils.AsyncDesignUtil;
import com.svnlan.manage.utils.DesignUtil;
import com.svnlan.manage.utils.DesignValidateUtil;
import com.svnlan.manage.vo.DesignDetailVO;
import com.svnlan.manage.vo.DesignListVO;
import com.svnlan.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 功能描述:
 *
 * @author:
 * @param:
 * @return:
 */
@Service
public class CommonDesignServiceImpl implements CommonDesignService {

    @Resource
    private CommonDesignDao commonDesignDao;

    @Resource
    private AsyncDesignUtil asyncDesignUtil;
    @Resource
    private DesignValidateUtil designValidateUtil;
    @Autowired
    private LoginUserUtil loginUserUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    private String clientTypeDirectory[] = new String[]{"pc", "mb", "app", "mp"};

    private String designBasePath;
    private String defaultPath;
    @PostConstruct
    public void setDesignBasePath(){
        // 已改
        try {
            Properties properties = PropertiesUtil.getConfig("upconfig.properties");
            designBasePath = properties.getProperty("home.savePath");
            defaultPath = properties.getProperty("home.path.default");
        } catch (Exception e){
            designBasePath = "/uploads/design/home/";
            defaultPath = "/uploads";
        }
    }

    @Value("${cdn.domain}")
    private String cdnDomain;

    /**
     * 功能描述: 添加编辑器操作
     *
     * @param:
     * @return:
     */
    @Override
    public Long addCommonDesign(AddDesignDTO addDesignDTO) {
        CommonDesign commonDesign = new CommonDesign();
        //类型不正确
        if (!DesignClientTypeEnum.exists(addDesignDTO.getClientType())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //非二级页 则限定为主页
        if (!addDesignDTO.getDesignType().equals(DesignTypeEnum.SUB.getCode()) ){
            addDesignDTO.setDesignType(DesignTypeEnum.MAIN.getCode());
        }
        commonDesign.setClientType(addDesignDTO.getClientType());
        commonDesign.setDesignType(addDesignDTO.getDesignType());
        //验证某一客户端类型的装扮数量
        designValidateUtil.checkDesignCount(commonDesign);
        commonDesign.setTitle(addDesignDTO.getTitle());
        commonDesign.setSize(addDesignDTO.getSize());
        String urlLockKey = null;
        //只有二级页设置url
        if (commonDesign.getDesignType().equals(DesignTypeEnum.SUB.getCode())) {
            //二级页默认启用
            commonDesign.setIsUsed(DesignUsedEnum.USED.getCode());
            String url = StringUtil.isEmpty(addDesignDTO.getUrl()) ? Long.toHexString(System.currentTimeMillis()) : addDesignDTO.getUrl();
            //验证url
            urlLockKey = designValidateUtil.checkUrlEntry(url, commonDesign);
        }
        //小程序装扮默认启用
        if (commonDesign.getClientType().equals(DesignClientTypeEnum.MP.getCode())){
            commonDesign.setIsUsed(DesignUsedEnum.USED.getCode());
        }
        if (addDesignDTO.getSourceId() != null){
            //验证logo
            /*Long sourceID = this.checkSource(addDesignDTO.getSourceID());
            if (sourceID > 0){
                commonDesign.setPic(sourceID);
            }*/
            commonDesign.setPic(addDesignDTO.getSourceId());
        }
        commonDesign.setState("1");
        commonDesign.setApprovalState("0");
        commonDesign.setMbDesignId(ObjectUtils.isEmpty(addDesignDTO.getMbDesignId()) ? 0L : addDesignDTO.getMbDesignId());
        commonDesign.setDesignClassifyID(ObjectUtils.isEmpty(addDesignDTO.getDesignClassifyID()) ? 0 : addDesignDTO.getDesignClassifyID());
        //seo信息
        if (addDesignDTO.getSeoKeyword() != null && addDesignDTO.getSeoKeyword().length() > 1000){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (addDesignDTO.getSeoDescription() != null && addDesignDTO.getSeoDescription().length() > 1000){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Map<String, String> map = new HashMap<>(2);
        if (addDesignDTO.getSeoKeyword() != null) { //不为空时才设置
            map.put("keyword", addDesignDTO.getSeoKeyword());
            map.put("description", addDesignDTO.getSeoDescription());
            commonDesign.setSeo(JsonUtils.beanToJson(map));
        }

        commonDesign.setPaste(ObjectUtils.isEmpty(addDesignDTO.getIsPaste()) ? false : (addDesignDTO.getIsPaste().intValue() == 1 ? true: false));
        commonDesign.setPathPre(defaultPath);
        //填充空字段
        ObjUtil.initializefield(commonDesign);

        int i = commonDesignDao.insert(commonDesign);
        if (i > 0){
            if (urlLockKey != null) {
                stringRedisTemplate.delete(urlLockKey);
            }
            /*if (Boolean.TRUE.equals(addDesignDTO.getSearchComponent())) {
                this.setSearchPermission(commonDesign.getCommonDesignId(), true);
            }*/
            return commonDesign.getCommonDesignId();
        }
        return null;
    }

    private void setSearchPermission(Long commonDesignId, Boolean isSet) {
        if (isSet == null){
            return;
        }
        try {
            // String key = "" + loginUser.getSchoolId() + "_" + commonDesignId;
            String key = "CommonDesign_Key_" + commonDesignId;
            if (isSet){
                stringRedisTemplate.opsForHash().put(GlobalConfig.SEARCH_PERMISSION_KEY, key, "1");
            } else {
                stringRedisTemplate.opsForHash().delete(GlobalConfig.SEARCH_PERMISSION_KEY, key);
            }
        } catch (Exception e){
            LogUtil.error(e, "设置用户名查询权限失败");
        }
    }

    @Override
    public Map<String, Object> getDesignList(GetDesignListDTO getDesignListDTO) {
        Map<String, Object> paramMap = new HashMap<>();
        //设置文件类型
        paramMap.put("sourceType", BusTypeEnum.DESIGN_LOGO.getTypeCode().toString());
        paramMap.put("clientType", getDesignListDTO.getClientType());
        paramMap.put("designType", getDesignListDTO.getDesignType());
        if (getDesignListDTO.getCommonDesignId() != null) {
            paramMap.put("commonDesignId", getDesignListDTO.getCommonDesignId());
        }
        List<CommonDesignList> designList = null;
        Long total = 0L;
        //取底下网校的使用中的装扮
       //PageHelper.startPage(getDesignListDTO.getCurrentPage(), Math.min(getDesignListDTO.getPageSize(), 1000), Boolean.TRUE.equals(getDesignListDTO.getWithPage()));

        getDesignListDTO.setPageSize(Math.min(getDesignListDTO.getPageSize(), 1000));
        paramMap.put("startIndex", getDesignListDTO.getStartIndex());
        paramMap.put("pageSize", getDesignListDTO.getPageSize());

        boolean search = true;
        if (Boolean.TRUE.equals(getDesignListDTO.getGetChildrenDesign())){
            paramMap.put("keyword", getDesignListDTO.getKeyword());
            if (Boolean.TRUE.equals(getDesignListDTO.getWithPage())){
                total = commonDesignDao.getChildrenDesignListCount(paramMap);
                search = 0 >= total ? false : true;
            }
            if (search) {
                designList = commonDesignDao.getChildrenDesignList(paramMap);
            }
        } else {
            if (Boolean.TRUE.equals(getDesignListDTO.getWithPage())){
                total = commonDesignDao.getDesignListCount(paramMap);
                search = 0 >= total ? false : true;
            }
            if (search) {
                designList = commonDesignDao.getDesignList(paramMap);
            }
        }
        List<DesignListVO> designListVOList = new ArrayList<>();
        List<DesignListVO> addList = null;

        Map<Integer, List<DesignListVO>> childrenMap = new HashMap<>(0);

        if (!CollectionUtils.isEmpty(designList)) {
            for (CommonDesignList c : designList) {
                DesignListVO designListVO = new DesignListVO();
                designListVO.setDesignId(c.getDesignId());
                designListVO.setTitle(c.getTitle());
                designListVO.setIsUsed(c.getIsUsed());
                designListVO.setDesignType(c.getDesignType());
                designListVO.setUrl(c.getUrl());
                designListVO.setSize(c.getSize());
                designListVO.setShare(c.getShare());
                designListVO.setShareDesignId(c.getShareDesignId());
                designListVO.setCreateTime(ObjectUtils.isEmpty(c.getGmtCreate()) ? 0L : c.getGmtCreate().getTime());
                designListVO.setUploaded(c.getUploaded() != null && c.getUploaded().equals(1));
                designListVO.setPaste(c.getPaste());
                String[] seoInfo = DesignUtil.getSEOInfoByString(c.getSeo());
                designListVO.setSeoKeyword(seoInfo[0]);
                designListVO.setSeoDescription(seoInfo[1]);
                if (c.getSourcePath() != null) {
                    designListVO.setThumb(c.getSourcePath());//todo 缩略图查看方式
                }
                designListVO.setMbDesignId(c.getMbDesignId());

                if (c.getDesignClassifyID().intValue() == 0) {
                    designListVOList.add(designListVO);
                } else {
                    if (!ObjectUtils.isEmpty(childrenMap) && childrenMap.containsKey(c.getDesignClassifyID())) {
                        childrenMap.get(c.getDesignClassifyID()).add(designListVO);
                    } else {
                        addList = new ArrayList<>();
                        addList.add(designListVO);
                        childrenMap.put(c.getDesignClassifyID(), addList);
                    }
                }
            }
        }

        // 二级页：添加目录结构
        if (!ObjectUtils.isEmpty(getDesignListDTO.getDesignType()) && "2".equals(getDesignListDTO.getDesignType())) {
            List<DesignListVO> classifyList = commonDesignDao.selectClassifyListByParam();
            if (!CollectionUtils.isEmpty(classifyList)){
                List<DesignListVO> classifyNodesList = setClassifyListToDesign(classifyList, childrenMap);
                if (!CollectionUtils.isEmpty(classifyNodesList)){
                    designListVOList.addAll(classifyNodesList);
                    designListVOList = designListVOList.stream().sorted(Comparator.comparing(DesignListVO::getCreateTime)).collect(Collectors.toList());
                }
            }
        }
        Map<String, Object> map = new HashMap<>(1);
        //返回带分页的
        if (Boolean.TRUE.equals(getDesignListDTO.getWithPage())) {
            //PageInfo<CommonDesignList> pageInfo = new PageInfo<>(designList);
            PageResult<DesignListVO> pageResult = new PageResult<>();
//            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setTotal(total);
            pageResult.setList(designListVOList);
            map.put("list", pageResult);
            return map;
        }
        map.put("list", designListVOList);
        return map;
    }

    private List<DesignListVO> setClassifyListToDesign(List<DesignListVO> list, Map<Integer, List<DesignListVO>> childrenMap){

        Map<String, DesignListVO> appender = new HashMap<>();
        //遍历一级菜单
        for (DesignListVO infoTypeVo : list) {
            if (!ObjectUtils.isEmpty(childrenMap) && childrenMap.containsKey(infoTypeVo.getDesignClassifyID())){
                infoTypeVo.setDesignList(childrenMap.get(infoTypeVo.getDesignClassifyID()));
            }
            if (CollectionUtils.isEmpty(infoTypeVo.getChildren())) {
                infoTypeVo.setChildren(new ArrayList<>());
            }
            addNodes(appender, infoTypeVo.getParentID().toString(), infoTypeVo);
            appender.put(infoTypeVo.getDesignClassifyID().toString(), infoTypeVo);
        }

        DesignListVO node = (appender.get(""));
        if (CollectionUtils.isEmpty(node.getChildren())){
            return new ArrayList();
        }
        return node.getChildren();
    }


    private void addNodes(Map<String, DesignListVO> appender, String parentIDStr, DesignListVO node) {
        if (appender.containsKey(parentIDStr)) {
            appender.get(parentIDStr).getChildren().add(node);
            appender.get(parentIDStr).getChildren().stream().sorted(Comparator.comparing(DesignListVO::getCreateTime)).collect(Collectors.toList());
        } else {
            // 根节点
            DesignListVO nodeRoot = new DesignListVO(node.getParentID(), node.getParentLevel(), new ArrayList<>());
            nodeRoot.getChildren().add(node);
            appender.put("", nodeRoot);
            appender.put(parentIDStr, nodeRoot);
        }
    }

    @Override
    public DesignDetailVO getDesignDetail(Long designId) {
        if (designId == null){
            return null;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("designId", designId);
        DesignDetailVO designDetailVO = commonDesignDao.getDesignDetail(paramMap);

        //seo信息
        String[] seoInfo = DesignUtil.getSEOInfoByString(designDetailVO.getSeo());
        designDetailVO.setSeoKeyword(seoInfo[0]);
        designDetailVO.setSeoDescription(seoInfo[1]);


        return designDetailVO;
    }

    @Override
    public boolean editDesignDetail(EditDesignDTO editDesignDTO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("designId", editDesignDTO.getDesignId());
        //查询装扮
        CommonDesign commonDesign = commonDesignDao.getDesignSimple(paramMap);
        if (commonDesign == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //需要修改logo时
        if (editDesignDTO.getSourceId() != null){
            commonDesign.setPic(editDesignDTO.getSourceId());
        }
        //seo信息
        if (editDesignDTO.getSeoKeyword() != null && editDesignDTO.getSeoKeyword().length() > 1000){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (editDesignDTO.getSeoDescription() != null && editDesignDTO.getSeoDescription().length() > 1000){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Map<String, String> map = new HashMap<>(2);
        if (editDesignDTO.getSeoKeyword() != null) { //不为空时才设置
            map.put("keyword", editDesignDTO.getSeoKeyword());
            map.put("description", editDesignDTO.getSeoDescription());
            commonDesign.setSeo(JsonUtils.beanToJson(map));
        }

        String serverName = loginUserUtil.getServerName();
        //二级页修改url
        String urlLockKey = null;
        String oldUrl = commonDesign.getUrl();
        if (commonDesign.getDesignType().equals(DesignTypeEnum.SUB.getCode())
                && !StringUtil.isEmpty(editDesignDTO.getUrl())
                && !editDesignDTO.getUrl().equals(commonDesign.getUrl())){
            urlLockKey = designValidateUtil.checkUrlEntry(editDesignDTO.getUrl(), commonDesign);
        }
        //head,foot等长度限制
        int textMax = 65535;
        if (editDesignDTO.getHead() != null && editDesignDTO.getHead().length() > textMax ||
                editDesignDTO.getApplet() != null && editDesignDTO.getApplet().length() > textMax ||
                editDesignDTO.getFoot()!= null && editDesignDTO.getFoot().length() > textMax ||
                editDesignDTO.getSetting() != null && editDesignDTO.getSetting().length() > textMax){
            throw new SvnlanRuntimeException(CodeMessageEnum.EXCEEDS_LIMIT.getCode());
        }
        if (editDesignDTO.getBody() != null){
            try {
                if (editDesignDTO.getBody().getBytes("UTF8").length > 9 * 1024 * 1024) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.EXCEEDS_LIMIT.getCode());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        commonDesign.setTitle(editDesignDTO.getTitle());
        commonDesign.setDetail(editDesignDTO.getBody());
        commonDesign.setHead(editDesignDTO.getHead());
        commonDesign.setFoot(editDesignDTO.getFoot());
        commonDesign.setSize(editDesignDTO.getSize());
        commonDesign.setApplet(editDesignDTO.getApplet());
        commonDesign.setSetting(editDesignDTO.getSetting());
        commonDesign.setGmtModified(new Date());
        commonDesign.setDesignClassifyID(ObjectUtils.isEmpty(editDesignDTO.getDesignClassifyID()) ? 0 : editDesignDTO.getDesignClassifyID());

        commonDesign.setPathPre(defaultPath);
        int i = commonDesignDao.editDesign(commonDesign);

        // 清除首页缓存
        //clearIndexRedis(loginUser.getSchoolId());
        List<String> serverNameList = null;
        if (urlLockKey != null) {
            stringRedisTemplate.delete(urlLockKey);
            if (!StringUtil.isEmpty(oldUrl)) {
                serverNameList = asyncDesignUtil.getSaveDesignServerNameList(serverName) ;
                String clientPath = DesignUtil.getClientPath(commonDesign.getClientType());
                //二级页文件重命名
                for (String network : serverNameList) {
                    String domainPath = defaultPath + designBasePath + network;
                    String oldFilePath = domainPath + clientPath + oldUrl + GlobalConfig.GEN_PAGE_SUFFIX;
                    String newFilePath = domainPath + clientPath + editDesignDTO.getUrl() + GlobalConfig.GEN_PAGE_SUFFIX;
                    File oldFile = new File(oldFilePath);
                    File newFile = new File(newFilePath);
                    boolean renameSuccess = oldFile.renameTo(newFile);
                    if (!renameSuccess) {
                        LogUtil.error("装扮二级页重命名" + renameSuccess + ", " + oldFilePath + " -> " + newFilePath);
                    } else {
                        LogUtil.info("装扮二级页重命名" + renameSuccess + ", " + oldFilePath + " -> " + newFilePath);
                    }
                }
            }
        }

        if (i > 0) {
            LogUtil.info("修改装扮,  " + JsonUtils.beanToJson(editDesignDTO));

            //this.setSearchPermission(commonDesign.getCommonDesignId(), editDesignDTO.getSearchComponent());

            Map<String, Object> result = new HashMap<>(1);
            return asyncDesignUtil.saveDesignFile(commonDesign.getCommonDesignId(), result, serverName, serverNameList);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

    }

    /*private void clearIndexRedis(){
        try {
            stringRedisTemplate.expire(GlobalConfig.COURSEINFOLISTBYIDS + "_" + schoolId, 1, TimeUnit.SECONDS);
            stringRedisTemplate.expire(GlobalConfig.COURSEWAREINFOLISTBYIDS + "_" + schoolId, 1, TimeUnit.SECONDS);
            LogUtil.info( "editDesignDetail clearIndexRedis " );
        }catch (Exception e){
            LogUtil.error(e, "editDesignDetail clearIndexRedis error");
        }
    }*/
    @Override
    public boolean deleteCommonDesign(DeleteDesignDTO deleteDesignDTO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("designId", deleteDesignDTO.getDesignId());
        //查询装扮
        CommonDesign commonDesign = commonDesignDao.getDesignSimple(paramMap);
        if (commonDesign == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (commonDesign.getIsUsed().equals(1) && commonDesign.getDesignType().equals(DesignTypeEnum.MAIN.getCode())){
            throw new SvnlanRuntimeException(CodeMessageEnum.Enabling_Not_Deleted.getCode());
        }
        int i = commonDesignDao.deleteDesign(commonDesign);
        //没有删除数据
        if (i == 0){
            return true;
        }
        //this.setSearchPermission(deleteDesignDTO.getDesignId(), false);

        return true;
    }

    @Override
    public boolean setUsing(EditDesignDTO editDesignDTO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("designId", editDesignDTO.getDesignId());
        CommonDesign originDesign = commonDesignDao.getDesignSimple(paramMap);
        //只有主页,和学生页能启用
        if (originDesign == null || !originDesign.getDesignType().equals(DesignTypeEnum.MAIN.getCode()) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //已经是启用状态，直接返回
        if (originDesign.getIsUsed().equals(DesignUsedEnum.USED.getCode())){
            return true;
        }
        originDesign.setGmtModified(new Date());
        try {
            //设置启用
            this.setUsingDesign(originDesign);
            Map<String, Object> result = new HashMap<>(2);
            //保存文件
            asyncDesignUtil.saveDesignFile(originDesign.getCommonDesignId(), result, loginUserUtil.getServerName());
        } catch (Exception e){
            LogUtil.error(e, "设置启用失败" + JsonUtils.beanToJson(originDesign));
        }

        return true;
    }

    public void setUsingDesign(CommonDesign originDesign) {
        //将已启用的设置为未启用
        commonDesignDao.updateToNotUsed(originDesign);
        //将当前的设为启用
        originDesign.setIsUsed(DesignUsedEnum.USED.getCode());
        //
         commonDesignDao.updateToUsed(originDesign.getCommonDesignId());

    }
    /**
     * @Description: 主题库装扮预览
     * @params:  [designId]
     */
    @Override
    public DesignDetailVO getDesignPreview(Long designId) {
        if (designId == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        return commonDesignDao.getDesignPreview(designId);
    }
    @Override
    public DesignDetailVO getUsingDesign(String clientType) {
        if (StringUtil.isEmpty(clientType)) {
            return null;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("clientType", clientType);
        paramMap.put("designType", DesignTypeEnum.MAIN.getCode());
        return commonDesignDao.getUsingDesign(paramMap);
    }

    @Override
    public Long addDesignCopy(AddDesignCopyDTO addDesignCopyDTO) {

        CommonDesign commonDesign = commonDesignDao.getDesignSimpleById(addDesignCopyDTO.getDesignId());
        if (commonDesign == null){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode(), "找不到源装扮");
        }
        //验证数量限制
        designValidateUtil.checkDesignCount(commonDesign);

        Long id = commonDesignDao.addCopyDesign(addDesignCopyDTO.getDesignId());
        if (id.equals(addDesignCopyDTO.getDesignId())){
            LogUtil.error("复制一个装扮插入失败");
            return 0L;
        }
        return id;
    }
}
