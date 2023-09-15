package com.svnlan.webdav.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ithit.webdav.server.util.StringUtil;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.enums.CloudOperateEnum;
import com.svnlan.home.service.UploadService;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.user.service.IoSourceService;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.ChinesUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.svnlan.webdav.impl.WebDavEngine.userVoThreadLocal;

@Slf4j
@Component
public class DiskSourceUtil {

    @Resource
    IoSourceDao ioSourceDao;

    @Resource
    IoSourceService ioSourceService;

    @Resource
    private UploadService uploadService;

    @Resource
    private IoFileDao ioFileDao;

    @Resource
    private UserFavDao userFavDao;

    public static final List<String> excludePath = new ArrayList<>();

    static {
        excludePath.add("/WEBDAV-INF");
        excludePath.add("/META-INF");
        excludePath.add("/WEB-INF");
    }

    public IOSourceVo getIoSource(Path path) {
        return getIoSource(path, null, null);
    }

    public IOSourceVo getIoSource(Path path, Long userId, Long rootId) {
        return getIoSource(path, userId, rootId, null);
    }

    /**
     * 根据url获取对应的 io_source
     * rootId 不为空，parentLevel 为空 说明是 个人空间，为个人空间根资源 sourceId
     * parentLevel 不为空，rootId 不为空 说明是查询的 收藏夹 此时 rootId 为空
     *
     * @param path        url
     * @param userId      用户id
     * @param rootId      用户根目录id
     * @param parentLevel 父目录id
     */
    public IOSourceVo getIoSource(Path path, Long userId, Long rootId, String parentLevel) {
        if (Objects.isNull(path)) {
            return null;
        }
        if (Objects.isNull(userId)) {
            UserVo userVo = userVoThreadLocal.get();
            userId = userVo.getUserID();
        }

        StringBuilder sbParentLevel;
        if (Objects.isNull(parentLevel)) {
            if (Objects.isNull(rootId)) {
                IOSourceVo rootDirectory = getUserRootDirectory(userId);
                rootId = rootDirectory.getSourceID();
            }

            log.info("getIoSource path => {}  userId => {}  rootId => {}", path, userId, rootId);
            if ("/".equals(path.toString()) || "\\".equals(path.toString())) {
                // 获取用户根 io_source
                return ioSourceService.getUserRootDirectory(userId);
            }
            sbParentLevel = new StringBuilder(",0,");
        } else {
            sbParentLevel = new StringBuilder(parentLevel);
        }


        int nameCount = path.getNameCount();
        // parentLevel 不为空的话，说明是收藏夹类型，因为根下面的一级资源已经在前面查询了，所有需要跳过
        // 否则为个人空间类型，需要从根下面的资源开始查询
        int firstIndex = Objects.nonNull(parentLevel) ? 1 : 0;

        // parentLevel
        // 用于缓存的 key stringBuilder
        StringBuilder sbCacheKey = new StringBuilder().append("/").append(userId);
//                .append("/").append(rootId);

        //  /57/1000/1681827040303/新建文件夹(1)/

        int index = firstIndex;
        Long parentId = rootId;
        IOSourceVo ioSourceVo = null;

        while (index < nameCount) {
            String name = path.subpath(index, ++index).toString();
            // 解码，因为中文会编码，导致在数据库中查询不到数据
//            log.info("getIoSource name => {}", name);
//            name = URLDecoder.decode(name);
            // 用于缓存的key
            String key = sbCacheKey.append("/").append(name).toString();
//            log.info("cache getIoSource key => {}", key);

            ioSourceVo = CaffeineUtil.PATH_IO_SOURCE_MAP_CACHE.getIfPresent(key);
            if (Objects.isNull(ioSourceVo)) {
//                log.info("getSourceIo Objects.isNull(ioSourceVo) YES  key => {}, name => {}", key, name);
                synchronized (this) {
                    ioSourceVo = CaffeineUtil.PATH_IO_SOURCE_MAP_CACHE.getIfPresent(key);
                    if (Objects.isNull(ioSourceVo)) {
//                        log.info("getSourceIo Objects.isNull(ioSourceVo) YES  parentId => {}, name => {}", parentId, name);
                        sbParentLevel.append(parentId).append(",");
//                        log.info("getSourceIo parentLevel => {}", sbParentLevel.toString());
                        ioSourceVo = ioSourceDao.querySourceVoByParentLevelAndUserIdAndName(sbParentLevel.toString(), userId, name);
                        if (Objects.isNull(ioSourceVo)) {
                            log.warn("未查询到io_source数据 参数为：parentLevel => {} userId => {} name=> {}", sbParentLevel, userId, name);
                            break;
                        }
                        parentId = ioSourceVo.getSourceID();
                        CaffeineUtil.PATH_IO_SOURCE_MAP_CACHE.put(key, ioSourceVo);
                    }
                }
            } else {
//                log.info("getSourceIo Objects.isNull(ioSourceVo)  NOT key => {}, name => {}", key, name);
                sbParentLevel.append(parentId).append(",");
//                log.info("getSourceIo Objects.isNull(ioSourceVo)  NOT ioSourceVo => {}", ioSourceVo);
//                log.info("getSourceIo Objects.isNull(ioSourceVo)  NOT sbParentLevel => {}", sbParentLevel);
                parentId = ioSourceVo.getSourceID();
            }
        }
//        log.info("getIoSource index => {}  nameCount => {}", index, nameCount);
        Assert.isTrue(Objects.equals(index, nameCount), "未查询到最后path,数据可能存在问题");
        return ioSourceVo;
    }

    /**
     * 根据用户id 获取用户的根文件 id
     *
     * @param userId 用户id
     */
    public IOSourceVo getUserRootDirectory(Long userId) {
        return ioSourceService.getUserRootDirectory(userId);
    }


    /**
     * 查询资源 io_source  如果 isNew 为 true, 则会返回 父资源
     * 仅用于创建操作
     *
     * @param path   url path
     * @param userId 用户id
     * @param rootId 用户资源顶层id
     * @param isNew  是否为新创建的资源
     */
    public IOSourceVo getIOSourceVoByPath(String path, Long userId, Long rootId, boolean isNew) {
        Path checkedPath = Paths.get(path);
        IOSourceVo ioSourceVo = getIoSource(checkedPath, userId, rootId);
        if (Objects.isNull(ioSourceVo)) {
            // 创建文件夹时，ioSourceVoList为空，表示该目录层级里没有重名的资源
            if (isNew) {
                // 则需要查询父级资源
                Path parent = checkedPath.getParent();
                return getIOSourceVoByPath(parent.toString(), userId, rootId, false);
            }
            return null;
        }
        return ioSourceVo;
    }

    @Resource
    private UploadService fileExecuteService;

    /**
     * 构造入参 并执行后续操作
     *
     * @param ioSourceVo       数据库中对应的资源
     * @param cloudOperateEnum 操作类型
     * @param isDest           是否未目标资源
     * @param consumer         源数据
     * @return
     */
    public CheckFileDTO derivePopulateAndSuccessor(IOSourceVo ioSourceVo, CloudOperateEnum cloudOperateEnum, Boolean isDest,
                                                   Consumer<SourceOpDto> consumer) {
        CheckFileDTO checkFileDTO = new CheckFileDTO();
        populateCheckFileDto(checkFileDTO, ioSourceVo, cloudOperateEnum, isDest, consumer);
        doResourceOperate(checkFileDTO, cloudOperateEnum);
        return checkFileDTO;
    }

    @Resource
    private JWTService jwtService;


    private void doResourceOperate(CheckFileDTO checkFileDTO, CloudOperateEnum cloudOperateEnum) {
        log.info("checkFileDTO => {}  cloudOperateEnum => {}", JSON.toJSONString(checkFileDTO), cloudOperateEnum.getOperate());
        UserVo userVo = userVoThreadLocal.get();
        Map<String, Object> resultMap = new HashMap<>(1);
        // zhy-note loginUser数据来源 后面再看还需要什么字段
        LoginUser loginUser = new LoginUser();
        loginUser.setUserID(userVo.getUserID());
        loginUser.setSex(userVo.getSex());
        // userType
        jwtService.setUserType(loginUser);
        checkFileDTO.setOperation(cloudOperateEnum.getCode());
        Assert.isTrue(fileExecuteService.updateFile(checkFileDTO, resultMap, loginUser), cloudOperateEnum.getOperate() + "报错");
    }

    /**
     * 构造 checkFileDto 用于后面流程
     */
    public void populateCheckFileDto(CheckFileDTO checkFileDto,
                                     IOSourceVo ioSourceVo,
                                     CloudOperateEnum operateEnum,
                                     Boolean isDest, Consumer<SourceOpDto> consumer) {
        if (isDest) {
            // 目标资源
            Assert.notNull(checkFileDto, "checkFileDto 不能为空");
            checkFileDto.setSourceID(ioSourceVo.getSourceID());
            checkFileDto.setSourceName(ioSourceVo.getName());
            checkFileDto.setSourceLevel(ioSourceVo.getParentLevel());
        } else {
            // 源资源
            SourceOpDto sourceOpDto = new SourceOpDto();
            sourceOpDto.setName(ioSourceVo.getName());
            sourceOpDto.setParentID(ioSourceVo.getParentID());
            sourceOpDto.setParentLevel(ioSourceVo.getParentLevel());
            sourceOpDto.setSourceID(ioSourceVo.getSourceID());
            sourceOpDto.setType(Objects.equals(ioSourceVo.getIsFolder(), 1) ? "folder" : "file");
            // 可用于调用方补充属性
            if (Objects.nonNull(consumer)) {
                consumer.accept(sourceOpDto);
            }
            List<SourceOpDto> dataArr = checkFileDto.getDataArr();
            if (Objects.isNull(dataArr)) {
                dataArr = new ArrayList<>();
                checkFileDto.setDataArr(dataArr);
            }
            dataArr.add(sourceOpDto);
        }
        checkFileDto.setOperation(operateEnum.getCode());
    }

    /**
     * 资源重命名
     *
     * @param ioSourceVo 源资源
     * @param desPath    目标资源路径
     */
    public boolean renameResource(IOSourceVo ioSourceVo, String desPath) {
        log.info("renameResource ioSourceVo => {} desPath => {}", ioSourceVo, desPath);
        // 目标资源名称
        String targetSourceName = desPath.substring(desPath.lastIndexOf("/") + 1);
        log.info("renameResource targetSourceName => {}", targetSourceName);

        derivePopulateAndSuccessor(ioSourceVo, CloudOperateEnum.RENAME_FILE, false, sourceOpDto -> {
            sourceOpDto.setOldName(sourceOpDto.getName());
            sourceOpDto.setName(targetSourceName);
        });
        return true;
    }

    /**
     * 文件移动
     *
     * @param sourceVo       源文件资源
     * @param targetSourceVo 目标文件资源
     */
    public void moveTo(IOSourceVo sourceVo, IOSourceVo targetSourceVo) {
        CheckFileDTO checkFileDTO = new CheckFileDTO();
        populateCheckFileDto(checkFileDTO, targetSourceVo, CloudOperateEnum.MOVE_FILE, true, null);
        populateCheckFileDto(checkFileDTO, sourceVo, CloudOperateEnum.MOVE_FILE, false, null);
        doResourceOperate(checkFileDTO, CloudOperateEnum.MOVE_FILE);

    }

    @Resource
    private FileOptionTool fileOptionTool;

    @Resource
    private StorageService storageService;


    public void uploadFile(int contentLength, String path) {
        UserVo userVo = userVoThreadLocal.get();
        Long userId = userVo.getUserID();

        String fileName = getFileName(path);
        if (contentLength > 0) {

            IOSourceVo rootSourceVo = ioSourceService.getUserRootDirectory(userId);
            Long rootId = rootSourceVo.getSourceID();
            IOSourceVo parentSourceVo = getIOSourceVoByPath(path, userId, rootId, true);


            UploadDTO uploadDTO = new UploadDTO();
            uploadDTO.setSourceID(parentSourceVo.getSourceID());
            // userId
            LoginUser loginUser = new LoginUser();
            loginUser.setUserID(userId);
            loginUser.setSex(userVo.getSex());
            // 前置检查
            CommonSource commonSource = new CommonSource();
            log.info(">>- 前置检查");
            HomeExplorerVO homeExplorerVO = uploadService.beforeUpload(commonSource, uploadDTO, loginUser, BusTypeEnum.CLOUD);
            // 判断容量是否足够
            log.info(">>- 判断容量是否足够");
            fileOptionTool.checkMemory(homeExplorerVO, contentLength);

            commonSource.setName(fileName);
            // 磁盘上真实的目录
            String pathPrefix = storageService.getDefaultStorageDevicePath();
            // 真实的文件名
//                String realFileName = FileUtil.getRealFileName(userSimpleInfo.userId, userSimpleInfo.getFileExt());
            // 真实的文件路径及真实的文件名
            commonSource.setPath(pathPrefix + getRealFilePath(path, userId));
            // 文件后缀
            commonSource.setFileType(FileUtil.getFileExtension(fileName));
            // 文件大小
            commonSource.setSize((long) contentLength);
            // 文件类型 目前是 1 用户类型
            commonSource.setTargetType(1);

            log.info("uploadDTO =>{}  commonSource => {}", JSONObject.toJSONString(uploadDTO), JSONObject.toJSONString(commonSource));
            log.info(">>- 根据业务类型做处理");
            // 根据业务类型做处理
            uploadService.doByBusType(uploadDTO, false, commonSource, BusTypeEnum.CLOUD, homeExplorerVO);

            doUploadAndConvertFile(commonSource, uploadDTO);
        } else {
            // 校验文件后缀
            uploadService.validateUploadFileName(fileName);
        }


    }

    /**
     * 执行真正的上传操作
     */
    private void doUploadAndConvertFile(CommonSource commonSource, UploadDTO uploadDTO) {
        if (Objects.nonNull(commonSource.getSize()) && commonSource.getSize() > 0) {
            log.info(">>- 资源相关信息写入数据库");
            uploadDTO.setBusType(BusTypeEnum.CLOUD.getBusType());
            Long fileId = uploadService.recordSourceDataToDb(commonSource, uploadDTO, BusTypeEnum.CLOUD);
            CommonSourceVO commonSourceVo = new CommonSourceVO();
            commonSourceVo.setFileID(fileId);
            commonSourceVo.setSourceID(uploadDTO.getSourceID());
            commonSourceVo.setFileType(commonSource.getFileType());
            log.info(">>- 转码操作");
            uploadService.tryToConvertFile(commonSourceVo, commonSource, BusTypeEnum.CLOUD.getBusType());
        }
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String REAL_PATH_PREFIX = "realPath:";

    /**
     * 根据浏览器地址栏上的 url 获取对应的真实磁盘上的路径 仅用于新增
     */
    public String getRealFilePath(String url, Long userId) {
        String realPath = stringRedisTemplate.opsForValue().get(REAL_PATH_PREFIX + url);
        if (!StringUtils.hasText(realPath)) {
            synchronized (this) {
                realPath = stringRedisTemplate.opsForValue().get(REAL_PATH_PREFIX + url);
                if (!StringUtils.hasText(realPath)) {
//                    Path path = Paths.get(url);
                    String fileExt = FileUtil.getFileExtension(getFileName(url));
//                    log.info("getRealFilePath userSimpleInfo => {}", userSimpleInfo);
                    realPath = FileUtil.getDatePath() + FileUtil.getRealFileName(userId, fileExt);
                    log.info("realPath => {}", realPath);
                    stringRedisTemplate.opsForValue().set(REAL_PATH_PREFIX + url, realPath, 10, TimeUnit.SECONDS);
                }
            }
        }
        return realPath;
    }

    @Transactional(rollbackFor = Exception.class)
    public Pair<IOSource, IOFile> setSourceMetadataToDb(IOSourceVo sourceVo, String absolutePath, Long userID, String name) {
        long epochSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();
        IOFile ioFile = new IOFile();
        ioFile.setName(name);
        ioFile.setCreateTime(epochSecond);
        ioFile.setModifyTime(epochSecond);
        ioFile.setPath(absolutePath);
        ioFile.setSize(0L);
        ObjUtil.initializefield(ioFile);
        org.springframework.util.Assert.isTrue(ioFileDao.insert(ioFile) == 1, "写入 ioFile失败");

        log.info("setSourceMetadataToDb ioFile => {}", ioFile);
        log.info("setSourceMetadataToDb sourceVo => {}", sourceVo);

        IOSource ioSource = new IOSource(userID, 0, name, sourceVo.getSourceID(), "");
        ioSource.setCreateTime(epochSecond);
        ioSource.setModifyTime(epochSecond);
        // 表示是用户的
        ioSource.setTargetType(1);
        ioSource.setFileID(ioFile.getFileID());
        ioSource.setParentLevel(sourceVo.getParentLevel() + sourceVo.getSourceID() + ",");
        ioSource.setFileType(FileUtil.getFileExtension(name));
        // 当前的默认存储
        Integer storageId = storageService.getDefaultStorageDeviceId();
        ioSource.setStorageID(storageId);
        ioSource.setNamePinyin(ChinesUtil.getPingYin(ioSource.getName()));
        ioSource.setNamePinyinSimple(ChinesUtil.getFirstSpell(ioSource.getName()));
        ObjUtil.initializefield(ioSource);
        org.springframework.util.Assert.isTrue(ioSourceDao.insert(ioSource) == 1, "写入 ioSource失败");

        return Pair.of(ioSource, ioFile);
    }

    public Pair<IOSourceVo, IOFile> getFileImplFromFavor(String url) {
        Path path = Paths.get(url);
        UserVo userVo = userVoThreadLocal.get();
        if (path.getNameCount() == 1) {
            return getFavorRootSource(userVo.getUserID(), path);
        } else {
            log.info("getFileImplFromFavor multi url => {}", url);
            // 先查出收藏夹下根资源 这个资源肯定是文件夹类型
            Pair<IOSourceVo, IOFile> rootPair = getFavorRootSource(userVo.getUserID(), path);
            log.info("Pair<IOSourceVo, IOFile> rootPair => {}", rootPair);
            IOSourceVo rootSourceVo = rootPair.getFirst();
//            String parentLevel = rootSourceVo.getParentLevel() + rootSourceVo.getSourceID() + ",";
            IOSourceVo ioSourceVo = getIoSource(path, userVo.getUserID(), rootSourceVo.getSourceID(), rootSourceVo.getParentLevel());
            IOFile ioFile = Objects.equals(ioSourceVo.getIsFolder(), 0) ? ioFileDao.selectById(ioSourceVo.getFileID()) : IOFile.NULL;
            return Pair.of(ioSourceVo, ioFile);
        }
    }

    /**
     * 获取收藏夹根路径下的一级资源
     */
    private Pair<IOSourceVo, IOFile> getFavorRootSource(Long userId, Path path) {
        String pathStr = path.toString();
        if (StringUtils.hasText(pathStr) && pathStr.startsWith("\\")) {
            // 為 windows 系統，將反斜綫變爲正斜綫
            pathStr = path.toString().replace("\\", "/");
        }
        Pair<IOSourceVo, IOFile> pair = CaffeineUtil.FAVOR_SOURCE.getIfPresent(userId + ":" + pathStr);
        if (Objects.nonNull(pair)) {
            return pair;
        }

        // 说明是在根下面的
        // 查询了 sourceId 和 对应的 name
        List<JSONObject> jsonObjList = userFavDao.selectFavorSourceId(userId, path.getName(0).toString());
        Assert.notEmpty(jsonObjList, "未查询到 url => " + path + " | userId => " + userId + " 对应的 sourceId");
        // key 为 sourceId  value 为对应的 name
        Map<Long, String> sourceIdNameMap = jsonObjList.stream().collect(Collectors.toMap(it -> it.getLong("sourceId"), it -> it.getString("name")));
        List<IOSource> ioSources = ioSourceDao.selectBatchIds(sourceIdNameMap.keySet());
        log.info("getFileImplFromFavor url => {}  ioSources => {}", path, ioSources);

        Map<Long, IOSourceVo> map = new HashMap<>();
        List<IOSourceVo> ioSourceVoList = new ArrayList<>();
        for (IOSource ioSource : ioSources) {
            IOSourceVo ioSourceVo = new IOSourceVo();
            ioSourceVo.copyFromIoSource(ioSource);
            if (Objects.equals(ioSource.getIsFolder(), 1)) {
                ioSourceVoList.add(ioSourceVo);
                continue;
            }
            map.put(ioSource.getFileID(), ioSourceVo);
        }
        List<Pair<IOSourceVo, IOFile>> result = new ArrayList<>();
        if (MapUtils.isNotEmpty(map)) {
            log.info("getFavorRootSource MapUtils.isNotEmpty(map) map => {}", map);
            List<IOFile> ioFiles = ioFileDao.selectBatchIds(map.keySet());
            for (IOFile ioFile : ioFiles) {
                result.add(Pair.of(map.get(ioFile.getFileID()), ioFile));
            }
        } else {
            result = ioSourceVoList.stream().map(it -> Pair.of(it, IOFile.NULL)).collect(Collectors.toList());
        }
        log.info("getFavorRootSource result => {}", result);
        pair = result.get(0);
        CaffeineUtil.FAVOR_SOURCE.put(userId + ":" + path, pair);
        return pair;
    }

    /**
     * 创建真实的文件，如果其父文件夹不存在，也需要创建其父文件夹
     *
     * @param dest 目标资源
     */
    private void createNewRealFile(@NotNull File dest) {
        if (dest.exists()) {
            return;
        }

        File parentFile = dest.getParentFile();
        if (!parentFile.exists()) {
            Assert.isTrue(parentFile.mkdirs(), "createNewRealFile 创建文件夹失败");
        }
        try {
            Assert.isTrue(dest.createNewFile(), "createNewRealFile 创建文件失败");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件名称
     */
    public static String getFileName(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }
        url = StringUtil.trimEnd(url, File.separator);
        int index = url.lastIndexOf(File.separator);
        if (index > -1) {
            return url.substring(index + 1);
        }
        return "";
    }
}
