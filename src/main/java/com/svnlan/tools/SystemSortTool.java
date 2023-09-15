package com.svnlan.tools;

import com.svnlan.common.I18nUtils;
import com.svnlan.enums.ShareMenuEnum;
import com.svnlan.enums.SourceFieldEnum;
import com.svnlan.enums.SourceSortEnum;
import com.svnlan.enums.UserOptionEnum;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.SystemSortVo;
import com.svnlan.user.dao.UserOptionDao;
import com.svnlan.user.vo.UserOptionVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/15 14:47
 */
@Component
public class SystemSortTool {

    @Resource
    UserOptionDao userOptionDao;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    LoginUserUtil loginUserUtil;

    @Value("${cdn.domain}")
    private String cdnDomain;

    public SystemSortVo getUserSort(Long userID){
        SystemSortVo sortVo = new SystemSortVo();
        List<UserOptionVo> sortOptionList =  userOptionDao.getUserSortConfig(userID, UserOptionEnum.sortKeyList);
        for (UserOptionVo vo : sortOptionList){
            switch (vo.getKey()){
                case "listSortField" :
                    sortVo.setListSortField(vo.getValue());
                    break;
                case "listSortOrder":
                    sortVo.setListSortOrder(vo.getValue());
                break;
                case "fileIconSize":
                    sortVo.setFileIconSize(vo.getValue());
                break;
                case "listSortKeep":
                    sortVo.setListSortKeep(vo.getValue());
                break;
                case "listType":
                    sortVo.setListTypeList(new ArrayList<>());
                    // 判断是否是json
                    if (JsonUtils.isJson(vo.getValue())){
                        try {
                            Map<String, Object> map = JsonUtils.jsonToMap(vo.getValue());
                            if (!ObjectUtils.isEmpty(map)){
                                String[] arr = null;
                                List<SystemSortVo> listIconList = new ArrayList<>();
                                for(String key : map.keySet()){
                                    arr = map.get(key).toString().split(":");
                                    listIconList.add(new SystemSortVo(key,arr[0], arr.length > 1 ? arr[1] : ""));
                                }
                                sortVo.setListTypeList(listIconList);
                            }
                        }catch (Exception e){
                            LogUtil.error(e, "getUserSort listType json 解析错误 value" + vo.getValue());
                        }
                    }else {
                        sortVo.setListType(vo.getValue());
                    }
                    break;
                case "listSort":
                    sortVo.setListSortList(new ArrayList<>());
                    if (JsonUtils.isJson(vo.getValue())){
                        try {
                            Map<String, Object> map = JsonUtils.jsonToMap(vo.getValue());
                            if (!ObjectUtils.isEmpty(map)){
                                String[] arr = null;
                                List<SystemSortVo> listSortList = new ArrayList<>();
                                for(String key : map.keySet()){
                                    arr = map.get(key).toString().split(":");
                                    listSortList.add(new SystemSortVo(key,arr[0],arr[1]));
                                }
                                sortVo.setListSortList(listSortList);
                            }
                        }catch (Exception e){
                            LogUtil.error(e, "getUserSort listSort json 解析错误 value" + vo.getValue());
                        }
                    }
                    break;
                default:
            }
        }

        return sortVo;
    }

    public List<HomeExplorerVO> getUserGroupSourceList(Long userID){
        return homeExplorerDao.getUserGroupSourceList(userID);
    }

    /** 系统管理员可以看到所有 */
    public List<HomeExplorerVO> getSystemGroupSourceList(){
        return homeExplorerDao.getSystemGroupSourceList();
    }

    public String getUserSpaceKey (Long userID){
        return "getUserSpaceKey_"+ userID;
    }

    public HomeExplorerVO getUserSpaceSize(Long userID, Long sourceID){

        long groupID = 0;
        HomeExplorerVO vo = homeExplorerDao.getOneSourceInfo(sourceID);
        if (2 == vo.getTargetType()){
            String groupSource = ioSourceMetaDao.getValueMetaByKey(sourceID, "groupSource");
            if (!ObjectUtils.isEmpty(groupSource) && StringUtil.isNumeric(groupSource)){
                groupID = Long.valueOf(groupSource);
            }
            if (groupID <= 0){
                List<Long> sourcePIDs = Arrays.asList(vo.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                        .map(Long::parseLong).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sourcePIDs)){
                    List<String> gpList = homeExplorerDao.getGroupParentLevelList(sourcePIDs);
                    if (!CollectionUtils.isEmpty(gpList)){
                        String pLevel = StringUtil.getMaxString(gpList);
                        List<Long> plList = Arrays.asList(pLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                                .map(Long::parseLong).collect(Collectors.toList());
                        groupID = plList.get(plList.size() - 1);
                    }
                }
            }
        }
        vo.setGroupName("");
        vo.setGroupID(0L);
        vo.setSizeMax(0.0);
        vo.setSizeUse(0L);
        vo.setUserID(userID);
        List<HomeExplorerVO> diskList = homeExplorerDao.getUserSpace(userID, groupID);


        for (HomeExplorerVO homeVo : diskList){
            if ("user".equals(homeVo.getSourceType())){
                vo.setUserSizeMax(homeVo.getUserSizeMax());
                vo.setUserSizeUse(homeVo.getUserSizeUse());
                vo.setIgnoreFileSize(homeVo.getIgnoreFileSize());
            }else if ("group".equals(homeVo.getSourceType())){
                vo.setGroupID(homeVo.getGroupID());
                vo.setGroupName(homeVo.getGroupName());
                vo.setSizeMax(homeVo.getSizeMax());
                vo.setSizeUse(homeVo.getSizeUse());
            }
        }
        return vo;
    }

    public Map<String, Object> setSortAboutMap(SystemSortVo systemSortVo, Map<String, Object> hashMap, long userID, String sortFieldParam
            , String sortTypeParam, String sourceID){
        return setSortAboutMap(systemSortVo,hashMap, userID, sortFieldParam  , sortTypeParam, sourceID, "");
    }
    public Map<String, Object> setSortAboutMap(SystemSortVo systemSortVo, Map<String, Object> hashMap, long userID, String sortFieldParam
            , String sortTypeParam, String sourceID, String prefix){
        if (ObjectUtils.isEmpty(systemSortVo)) {
            systemSortVo = this.getUserSort(userID);
        }
        if (!ObjectUtils.isEmpty(sortFieldParam) && !ObjectUtils.isEmpty(sortTypeParam)){
            hashMap.put("sortType", SourceSortEnum.getSortType(sortTypeParam));
            hashMap.put("sortField", prefix + SourceFieldEnum.getSortField(sortFieldParam));
        }else {
            String sortType = systemSortVo.getListSortOrder();
            String sortField = systemSortVo.getListSortField();
            hashMap.put("sortType", SourceSortEnum.getSortType(sortType));
            hashMap.put("sortField", prefix + SourceFieldEnum.getSortField(sortField));
            if ("1".equals(systemSortVo.getListSortKeep()) && !CollectionUtils.isEmpty(systemSortVo.getListSortList())){
                for (SystemSortVo vo : systemSortVo.getListSortList()){
                    if (vo.getSourceID().equals(sourceID)){
                        if (!ObjectUtils.isEmpty(vo.getListSortOrder())) {
                            hashMap.put("sortType", SourceSortEnum.getSortType(vo.getListSortOrder()));
                        }
                        if (!ObjectUtils.isEmpty(vo.getListSortField())) {
                            hashMap.put("sortField", prefix + SourceFieldEnum.getSortField(vo.getListSortField()));
                        }
                        break;
                    }
                }
            }
        }

        return hashMap;
    }

    /**  填充路径 */
    public String getSourcePathDisplay(String parentLevel, Map<Long, String> sourceNameMap, List<String> levelList, List<Long> parentList){
        levelList = null;
        if (ObjectUtils.isEmpty(parentLevel)){
            return "";
        }
        parentList = Arrays.asList(parentLevel.split(",")).stream().filter(n->!ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(parentList)){
            levelList = new ArrayList<>();
            for (Long parentID : parentList){
                if (!ObjectUtils.isEmpty(sourceNameMap) && sourceNameMap.containsKey(parentID)){
                    levelList.add(sourceNameMap.get(parentID) + "/");
                }
            }
        }
        return CollectionUtils.isEmpty(levelList) ? "" : levelList.stream().collect(Collectors.joining("", "", ""));
    }


    public String getCdnPath(String hardLinkPath, String fileName){
        String cdnPath = "";
        if (ObjectUtils.isEmpty(cdnDomain)){
            cdnPath = HttpUtil.getRequestRootUrl(null) + FileUtil.getShowImageUrlDown(hardLinkPath, fileName) ;
        }else {
            cdnPath = "https://" + cdnDomain + FileUtil.getShowImageUrlDown(hardLinkPath, fileName);
        }
        return cdnPath;
    }


    public HomeExplorerVO shareToMeMenu() {
        HomeExplorerVO vo = new HomeExplorerVO();
        vo.setIsChildren(true);
        vo.setCanDownload(false);
        vo.setIsTruePath(false);
        vo.setName(I18nUtils.tryI18n(ShareMenuEnum.shareToMe.getCode()));
        vo.setPathDesc("");
        if (!ObjectUtils.isEmpty(ShareMenuEnum.shareToMe.getDesc())) {
            vo.setPathDesc(I18nUtils.tryI18n(ShareMenuEnum.shareToMe.getDesc()));
        }
        vo.setPathDisplay("/" + vo.getName());
        vo.setType("folder");
        vo.setIcon(ShareMenuEnum.shareToMe.getIcon());
        return vo;
    }
    // 资讯
    public HomeExplorerVO infoToMeMenu() {
        HomeExplorerVO vo = new HomeExplorerVO();
        vo.setIsChildren(true);
        vo.setCanDownload(false);
        vo.setIsTruePath(false);
        vo.setName(I18nUtils.tryI18n(ShareMenuEnum.info.getCode()));
        vo.setPathDesc("");
        if (!ObjectUtils.isEmpty(ShareMenuEnum.info.getDesc())) {
            vo.setPathDesc(I18nUtils.tryI18n(ShareMenuEnum.info.getDesc()));
        }
        vo.setPathDisplay("/" + vo.getName());
        vo.setType("folder");
        vo.setIcon(ShareMenuEnum.info.getIcon());
        return vo;
    }
}
