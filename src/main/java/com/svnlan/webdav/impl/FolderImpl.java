package com.svnlan.webdav.impl;

import com.ithit.webdav.server.File;
import com.ithit.webdav.server.Folder;
import com.ithit.webdav.server.HierarchyItem;
import com.ithit.webdav.server.Property;
import com.ithit.webdav.server.exceptions.*;
import com.ithit.webdav.server.paging.OrderProperty;
import com.ithit.webdav.server.paging.PageResults;
import com.ithit.webdav.server.quota.Quota;
import com.ithit.webdav.server.resumableupload.ResumableUploadBase;
import com.ithit.webdav.server.search.Search;
import com.ithit.webdav.server.search.SearchOptions;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.enums.CloudOperateEnum;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.webdav.common.ResourceType;
import com.svnlan.webdav.config.DiskServiceOrDaoWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.util.Pair;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.svnlan.webdav.impl.WebDavEngine.userVoThreadLocal;

/**
 * Represents a folder in the File system repository.
 */
@ToString
@Slf4j
public class FolderImpl extends HierarchyItemImpl implements Folder, Search, Quota, ResumableUploadBase {
    public FolderImpl() {
    }

    public FolderImpl(DiskServiceOrDaoWrapper wrapper, FileImpl fileImpl) {
        setWrapper(wrapper);
        this.fileImpl = fileImpl;
        log.info("construct fileImpl => {}", fileImpl);
        Assert.notNull(fileImpl, "fileImpl 不能为空 ");
    }

    public FolderImpl getNewItemImpl(String name, String path, long created, long modified,
                                     WebDavEngine engine) {
        log.info("getNewItemImpl name => {}  path => {}  created => {}  modified => {}", name, path, created, modified);
        FolderImpl folder = new FolderImpl(name, path, created, modified, engine);
        folder.setWrapper(this.wrapper);
        log.info("getNewItemImpl end");
        return folder;
    }

    @Setter
    private FileImpl fileImpl;

    /**
     * 数据库对应的资源
     */
    @Setter
    @Getter
    private IOSourceVo sourceVo;


    //    @Lazy
    @Setter
    private Map<ResourceType, AbstractResourceProcessor> resourceProcessorMap;

    /**
     * Initializes a new instance of the {@link FolderImpl} class.
     *
     * @param name     Name of hierarchy item.
     * @param path     Relative to WebDAV root folder path.
     * @param created  Creation time of the hierarchy item.
     * @param modified Modification time of the hierarchy item.
     * @param engine   Instance of current {@link WebDavEngine}
     */
    private FolderImpl(String name, String path, long created, long modified,
                       WebDavEngine engine) {
        super(name, path, created, modified, engine);
    }

    public static final FolderImpl NULL = new FolderImpl();

    /**
     * Returns folder that corresponds to path.
     *
     * @param path   Encoded path relative to WebDAV root.
     * @param engine Instance of {@link WebDavEngine}
     * @return Folder instance or null if physical folder not found in file system.
     * @throws ServerException in case of exception
     */
    FolderImpl getFolder(String path, WebDavEngine engine) throws ServerException {
//        log.info(">>- getFolder path => {}", path);
        ResourceType resourceType = WebDavEngine.resourceTypeThreadLocal.get();
        if (resourceType == ResourceType.PRIVATE) {
            if (!checkIfNecessary(path, "._")) {
                return null;
            }

            String realPath = engine.getContextAware(path);
//            log.info(">> getFolder realPath => {}", realPath);
            String pathFragment = decodeAndConvertToPath(realPath);
            boolean root = pathFragment.equals("/");
//            log.info(">> getFolder pathFragment => {}", pathFragment);

            // 个人空间
            IOSourceVo ioSource = getSourceIoByPath(pathFragment);
//            log.info(">> getFolder ioSource => {}", ioSource);
            if (Objects.isNull(ioSource)) {
                // 这种属于 ioSource 不存在，那对应的 ioFile 肯定也是不存在，也就没有必要在后续的步骤里查询  createFileImpl
                return FolderImpl.NULL;
            }
            // 判断 ioSource 是否为文件夹
            if (Objects.equals(ioSource.getIsFolder(), 0)) {
                return null;
            }

            String name = root ? "ROOT" : ioSource.getName();
            long created = ioSource.getCreateTime() * 1000;
            long modified = ioSource.getModifyTime() * 1000;
            FolderImpl newItemImpl = this.getNewItemImpl(name, fixPath(path), created, modified, engine);
            newItemImpl.setSourceVo(ioSource);
            newItemImpl.setWrapper(wrapper);
            newItemImpl.setPathFragment(pathFragment);
            newItemImpl.setFileImpl(fileImpl);
            newItemImpl.setResourceProcessorMap(resourceProcessorMap);
//            log.info(">>- getFolder end  pathFragment => {}", pathFragment);
            return newItemImpl;
        } else if (resourceType == ResourceType.FAVOR) {
            if (!checkIfNecessary(path)) {
                return null;
            }
            String realPath = engine.getContextAware(path);
//            log.info(">> getFolder realPath => {}", realPath);
            String pathFragment = decodeAndConvertToPath(realPath);
            boolean root = pathFragment.equals("/") || pathFragment.equals("\\");
            if (root) {
                FolderImpl newItemImpl = this.getNewItemImpl("ROOT", fixPath(path), 0L, 0L, engine);
                newItemImpl.setSourceVo(null);
                newItemImpl.setWrapper(wrapper);
                newItemImpl.setPathFragment(pathFragment);
                newItemImpl.setFileImpl(fileImpl);
                newItemImpl.setResourceProcessorMap(resourceProcessorMap);
                return newItemImpl;
            } else {
                // 获取非根路径下的资源
                Pair<IOSourceVo, IOFile> pair = wrapper.diskSourceUtil.getFileImplFromFavor(pathFragment);
                IOSourceVo ioSourceVo = pair.getFirst();
                IOFile ioFile = pair.getSecond();
                if (Objects.equals(ioFile, IOFile.NULL)) {
                    // 表示是文件夹类型
                    FolderImpl newItemImpl = this.getNewItemImpl(ioSourceVo.getName(), fixPath(path), ioSourceVo.getCreateTime(),
                            ioSourceVo.getModifyTime(), engine);
                    newItemImpl.setSourceVo(ioSourceVo);
                    newItemImpl.setWrapper(wrapper);
                    newItemImpl.setPathFragment(pathFragment);
                    newItemImpl.setFileImpl(fileImpl);
                    newItemImpl.setResourceProcessorMap(resourceProcessorMap);
                    return newItemImpl;
                } else {
                    // 表示是文件类型
                    return null;
                }
            }
        } else {
            throw new SvnlanRuntimeException("目前只支持 private favor 资源类型");
        }


    }

    private static String fixPath(String path) {
        if (!Objects.equals(path.substring(path.length() - 1), "/")) {
            path += "/";
        }
        return path;
    }

    @Override
    public boolean beforeCreateFile(String name) {
        log.info("beforeCreateFile 1 >> 检验文件后缀");
        if (StringUtils.hasText(name) && name.startsWith(".")) {
            return true;
        }
        wrapper.uploadService.validateUploadFileName(name);
        log.info("beforeCreateFile 1 >> end");
        return true;
    }

    /**
     * Creates new {@link FileImpl} file with the specified name in this folder.
     *
     * @param name Name of the file to create.
     * @return Reference to created {@link File}.
     * @throws LockedException This folder was locked. Client did not provide the lock token.
     * @throws ServerException In case of an error.
     */
    @SneakyThrows
    @Override
    public FileImpl createFile(String name) throws LockedException, ServerException {
        String contextAwarePath = getContextAwarePath();
        log.info("folderImpl fullPath =>{} contextPath => {},  createFile name => {}", this.getFullPath().toString(), contextAwarePath, name);
        if (!checkIfNecessary(name, "._")) {
            log.info("createFile checkIfNecessary name => {}", name);
            return null;
        }
        ensureHasToken();
        UserVo userVo = userVoThreadLocal.get();

        String realFilePath = wrapper.diskSourceUtil.getRealFilePath(HierarchyItemImpl.decodeAndConvertToPath(contextAwarePath) + "/" + name, userVo.getUserID());
        log.info("realFilePath =>> {}", realFilePath);
        // 当前的存储路径
//        String defaultStoragePath = wrapper.storageService.getDefaultStorageDevicePath();
//        Path absolutePath = Paths.get(getRootFolder(), defaultStoragePath, "private/cloud", realFilePath);
        Path absolutePath = Paths.get(getRootFolder(), getLocation(), "private/cloud", realFilePath);
        log.info("absolute path => {}", absolutePath.toString());
        if (Files.exists(absolutePath)) {
            // 说明数据有问题, 为了数据一致性，需要把对应的数据删除
            log.warn("createFile absolutePath is Exist!");
            throw new ServerException("createFile absolutePath is Exist!");
            // 将这个数据删除
//            Files.delete(absolutePath);
            // 对应的表记录也得删除
//            String realFileName = absolutePath.getFileName().toString();
//            IOFile ioFile = wrapper.ioFileDao.selectOne(new LambdaQueryWrapper<IOFile>()
//                    .select(IOFile::getFileID)
//                    .eq(IOFile::getFileName, realFileName));
//            wrapper.ioFileDao.deleteById(ioFile.getFileID());
//            wrapper.ioSourceDao.delete(new LambdaQueryWrapper<IOSource>().eq(IOSource::getFileID, ioFile.getFileID()));
        } else {
            try {
                // 判断其父目录是否存在， 不存在，需要创建
                java.io.File parentFile = absolutePath.toFile().getParentFile();
                if (!parentFile.exists()) {
                    Assert.isTrue(parentFile.mkdirs(), "createFile 创建文件夹失败");
                }
                Files.createFile(absolutePath);
            } catch (IOException e) {
                Files.delete(absolutePath);
                throw new ServerException(e);
            }
        }

        getEngine().getWebSocketServer().notifyCreated(getPath() + encode(name), getWebSocketID());
//            log.info("ioSourceVO => {}", sourceVo);
//            log.info("fileImpl => {}", fileImpl);
        ResourceType resourceType = WebDavEngine.resourceTypeThreadLocal.get();
//            log.info("resourceType => {}", resourceType.name);


        // 这里需要写入一些数据到数据库 确保 fileImpl.getFile() 能获取到数据
        String url = resourceType.urlPrefix + getContextAwarePath() + encode(name);
        Pair<IOSource, IOFile> pair = wrapper.diskSourceUtil.setSourceMetadataToDb(sourceVo, absolutePath.toString(), userVo.getUserID(), name);
        FileImpl file = fileImpl.createFileImpl(url, getEngine(), pair.getSecond(), sourceVo);
        file.setAbsolutePath(absolutePath.toString());

        FileCacheInfo fileCacheInfo = new FileCacheInfo(pair.getFirst().getSourceID(), pair.getSecond().getFileID(), absolutePath.toString());
        String key = TEMP_FILE_PREFIX + userVo.getUserID() + contextAwarePath.replace("/", java.io.File.separator) + name;
        if (log.isDebugEnabled()) {
            log.info("createFile redis key => {}", decode(key));
        }
        wrapper.redisTemplate.opsForValue().set(decode(key), fileCacheInfo, 1, TimeUnit.DAYS);
        return file;
    }

    /**
     * Creates new {@link FolderImpl} folder with the specified name in this folder.
     *
     * @param name Name of the folder to create.
     * @return Instance of newly created Folder.
     * @throws LockedException This folder was locked. Client did not provide the lock token.
     * @throws ServerException In case of an error.
     */
    @Override
    public Folder createFolder(String name) throws LockedException,
            ServerException {
        createFolderInternal(name);
        getEngine().getWebSocketServer().notifyCreated(getPath() + encode(name), getWebSocketID());
        return new FolderImpl(wrapper, fileImpl).getFolder(getPath() + encode(name), getEngine());
    }

    @SneakyThrows
    private boolean isCurrentLevel(String parentLevel) {
        if (!StringUtils.hasText(parentLevel)) {
            return false;
        }
        // 去掉左右两边逗号
        parentLevel = parentLevel.substring(1, parentLevel.length() - 1);
        String[] splits = parentLevel.split(",");
        return Objects.equals(Paths.get(getPath()).getNameCount(), splits.length);
    }

    private void createFolderInternal(String name) throws LockedException,
            ServerException {
        ensureHasToken();
//        log.info("createFolderInternal name => {}  path => {} pathFragment => {}", name, getPath(), pathFragment);
        String newPath = pathFragment + name;
//        log.info("mkdir path => {}", newPath);
        UserVo userVo = userVoThreadLocal.get();
        IOSourceVo rootSourceVo = wrapper.getIoSourceService().getUserRootDirectory(userVo.getUserID());
        Long rootId = rootSourceVo.getSourceID();
        IOSourceVo ioSourceVo = wrapper.diskSourceUtil.getIOSourceVoByPath(newPath, userVo.getUserID(), rootId, true);
//        log.info("ioSourceVo => {}", ioSourceVo);

        String parentLevel;
        Long parentId;
        if (isCurrentLevel(ioSourceVo.getParentLevel())) {
            // 表示同一层级下的节点
            Assert.isTrue(name.equals(ioSourceVo.getName()), "mkdir 数据有误");
            // 表示该目录下有同名的资源
            parentId = ioSourceVo.getParentID();
            parentLevel = ioSourceVo.getParentLevel();
            // 根据 parentLevel 查询所有的资源
            name = wrapper.ioSourceService.deriveProperName(parentLevel, name);

        } else {
            // ioSourceVo 为父级资源
            // 表示该目录下没有同名的资源，其 ioSourceVo 为父目录资源
            parentId = ioSourceVo.getSourceID();
            parentLevel = ioSourceVo.getParentLevel() + parentId + ",";
        }
        // 判断是否为要创建的资源

        Assert.isTrue(wrapper.ioSourceService.createDirectory(userVo.getUserID(), name, parentLevel, parentId), "新建文件夹失败");

    }

    /**
     * Gets the array of this folder's children.
     *
     * @param propNames  List of properties to retrieve with the children. They will be queried by the engine later.
     * @param offset     The number of items to skip before returning the remaining items.
     * @param nResults   The number of items to return.
     * @param orderProps List of order properties requested by the client.
     * @return Instance of {@link PageResults} class that contains items on a requested page and total number of items in a folder.
     * @throws ServerException In case of an error.
     */
    @Override
    public PageResults getChildren(List<Property> propNames, Long offset, Long nResults, List<OrderProperty> orderProps) throws ServerException {
//        log.info("getChildren propNames => {} offset => {} nResults => {} orderProps => {}", propNames, offset, nResults, orderProps);
        String decodedPath = HierarchyItemImpl.decodeAndConvertToPath(getContextAwarePath());
        List<HierarchyItemImpl> children = new ArrayList<>();

        UserVo userVo = userVoThreadLocal.get();
        IOSourceVo rootDirectory = wrapper.getDiskSourceUtil().getUserRootDirectory(userVo.getUserID());
//        log.info("IOSourceVo rootDirectory rootId => {}", rootDirectory.getSourceID());
        if ("/".equals(decodedPath) || "\\".equals(decodedPath)) {
            // 这里需要判断资源所属 即是个人空间 还是收藏夹
            ResourceType resourceType = WebDavEngine.resourceTypeThreadLocal.get();
            Pair<List<HierarchyItemImpl>, Long> pair = Optional.ofNullable(resourceProcessorMap.get(resourceType))
                    .orElseThrow(() -> new SvnlanRuntimeException("没有 " + resourceType + " 对应的处理器"))
                    .getRootChildrenResource(rootDirectory.getSourceID(), jsonObj -> {
                        try {
                            String childPath = getPath() + encode(jsonObj.getString("name"));
                            return (HierarchyItemImpl) getEngine().getHierarchyItem(childPath);
                        } catch (ServerException e) {
                            log.warn("getChildren resourceType => {} childPath => {} 未查询到数据", resourceType, children);
                            e.printStackTrace();
                        }
                        return null;
                    });
            return new PageResults(pair.getFirst(), pair.getSecond());
        } else {
            List<IOSourceVo> childrenSourceVoList = getSubIoSource(sourceVo.getSourceID(), userVo.getUserID(), rootDirectory.getSourceID());
            if (!CollectionUtils.isEmpty(childrenSourceVoList)) {
                for (IOSourceVo sourceVo : childrenSourceVoList) {
                    String childPath = getPath() + encode(sourceVo.getName());
                    HierarchyItemImpl item = (HierarchyItemImpl) getEngine().getHierarchyItem(childPath);
                    if (Objects.nonNull(item)) {
                        children.add(item);
                    }
                }

            }
            long total = children.size();
            return new PageResults(children, total);
        }
    }

    /**
     * 根据parentId 获取旗下的资源
     */
    public List<IOSourceVo> getSubIoSource(Long parentId, Long userId, Long rootId) {
        // zhy-note 这里是否添加缓存，后面再考虑下
        log.info("getSubIoSource this => {}, parentId => {},  userId => {}, rootId => {}", this, parentId, userId, rootId);
        List<IOSourceVo> tempList = wrapper.getIoSourceDao().getSourceInfoByParentIdAndUser(parentId, userId);
//        log.info("getSubIoSource tempList => {}", tempList);
        if (CollectionUtils.isEmpty(tempList)) {
            return Collections.emptyList();
        }
        if (Objects.nonNull(rootId)) {
            return tempList.stream().filter(it -> it.getParentLevel().startsWith(",0," + rootId)).collect(Collectors.toList());
        }
        return tempList;
    }

    @Override
    String getAbsolutePath() {
        return getFullPath().toString();
    }

    @Override
    public void delete() throws LockedException,
            ServerException {
        ensureHasToken();
        try {
            wrapper.getDiskSourceUtil().derivePopulateAndSuccessor(sourceVo, CloudOperateEnum.DELETE_FILE, false, null);
        } catch (Exception e) {
            throw new ServerException(e);
        }
        getEngine().getWebSocketServer().notifyDeleted(getPath(), getWebSocketID());
    }

    @Override
    public boolean checkIfRename(Folder folder) {
        FolderImpl folderImpl = (FolderImpl) folder;
        return folderImpl.sourceVo.getSourceID().equals(this.sourceVo.getParentID());
    }

    @Override
    public void renameTo(Folder folder, String destName) {
        destName = decode(getEngine().getContextAware(destName));
        wrapper.diskSourceUtil.renameResource(sourceVo, destName);
    }

    /**
     * 文件的复制，不知道什么情况下会触发这个
     *
     * @param folder   Destination folder.
     * @param destName Name of the destination item.
     * @param deep     Indicates whether to copy entire subtree.
     * @throws LockedException
     * @throws MultiStatusException
     * @throws ServerException
     */
    @Override
    public void copyTo(Folder folder, String destName, boolean deep)
            throws LockedException, MultiStatusException, ServerException {
        ((FolderImpl) folder).ensureHasToken();

        String relUrl = HierarchyItemImpl.decodeAndConvertToPath(((FolderImpl) folder).getContextAwarePath());
        String destinationFolder = Paths.get(getRootFolder(), relUrl).toString();
        if (isRecursive(relUrl)) {
            throw new ServerException("Cannot copy to subfolder", WebDavStatus.FORBIDDEN);
        }
        if (!Files.exists(Paths.get(destinationFolder)))
            throw new ServerException();
        try {
            Path sourcePath = this.getFullPath();
            Path destinationFullPath = Paths.get(destinationFolder, destName);
            FileUtils.copyDirectory(sourcePath.toFile(), destinationFullPath.toFile());
            addIndex(destinationFullPath, ((FolderImpl) folder).getContextAwarePath() + destName, destName);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        setName(destName);
        getEngine().getWebSocketServer().notifyCreated(((FolderImpl) folder).getContextAwarePath() + encode(destName), getWebSocketID());
    }

    /**
     * Check whether current folder is the parent to the destination.
     *
     * @param destFolder Path to the destination folder.
     * @return True if current folder is parent for the destination, false otherwise.
     * @throws ServerException in case of any server exception.
     */
    private boolean isRecursive(String destFolder) throws ServerException {
        return destFolder.startsWith(getContextAwarePath().replace("/", java.io.File.separator));
    }

    /**
     * Sorts array of FileSystemInfo according to the specified order.
     *
     * @param paths      Array of files and folders to sort.
     * @param orderProps Sorting order.
     * @return Sorted list of files and folders.
     */
    private List<Path> sortChildren(List<Path> paths, List<OrderProperty> orderProps) {
        if (orderProps != null && !orderProps.isEmpty()) {
            int index = 0;
            Comparator<Path> comparator = null;
            for (OrderProperty orderProperty :
                    orderProps) {
                Comparator<Path> tempComp = null;
                if ("is-directory".equals(orderProperty.getProperty().getName())) {
                    Function<Path, Boolean> sortFunc = item -> item.toFile().isDirectory();
                    tempComp = Comparator.comparing(sortFunc);
                }
                if ("quota-used-bytes".equals(orderProperty.getProperty().getName())) {
                    Function<Path, Long> sortFunc = item -> item.toFile().length();
                    tempComp = Comparator.comparing(sortFunc);
                }
                if ("getlastmodified".equals(orderProperty.getProperty().getName())) {
                    Function<Path, Long> sortFunc = item -> item.toFile().lastModified();
                    tempComp = Comparator.comparing(sortFunc);
                }
                if ("displayname".equals(orderProperty.getProperty().getName())) {
                    Function<Path, String> sortFunc = item -> item.getFileName().toString();
                    tempComp = Comparator.comparing(sortFunc);
                }
                if ("getcontenttype".equals(orderProperty.getProperty().getName())) {
                    Function<Path, String> sortFunc = item -> getExtension(item.getFileName().toString());
                    tempComp = Comparator.comparing(sortFunc);
                }
                if (tempComp != null) {
                    if (index++ == 0) {
                        if (orderProperty.isAscending()) {
                            comparator = tempComp;
                        } else {
                            comparator = tempComp.reversed();
                        }
                    } else {
                        if (orderProperty.isAscending()) {
                            comparator = comparator != null ? comparator.thenComparing(tempComp) : tempComp;
                        } else {
                            comparator = comparator != null ? comparator.thenComparing(tempComp.reversed()) : tempComp.reversed();
                        }
                    }
                }
            }
            if (comparator != null) {
                paths = paths.stream().sorted(comparator).collect(Collectors.toList());
            }
        }
        return paths;
    }

    private String getExtension(String name) {
        int periodIndex = name.lastIndexOf('.');
        return periodIndex == -1 ? "" : name.substring(periodIndex + 1);

    }

    @Override
    public void moveTo(Folder folder, String destName) throws LockedException,
            ConflictException, ServerException {
        ensureHasToken();
        FolderImpl folderImpl = (FolderImpl) folder;
        folderImpl.ensureHasToken();

        wrapper.diskSourceUtil.moveTo(sourceVo, folderImpl.getSourceVo());
        setName(destName);
        getEngine().getWebSocketServer().notifyMoved(getContextAwarePath(), ((FolderImpl) folder).getContextAwarePath() + encode(destName), getWebSocketID());
    }

    /**
     * Returns list of items that correspond to search request.
     *
     * @param searchString A phrase to search.
     * @param options      Search parameters.
     * @param propNames    List of properties to retrieve with the children. They will be queried by the engine later.
     * @param offset       The number of items to skip before returning the remaining items.
     * @param nResults     The number of items to return.
     * @return Instance of {@link PageResults} class that contains items on a requested page and total number of items in search results.
     */
    @Override
    public PageResults search(String searchString, SearchOptions options, List<Property> propNames, Long offset, Long nResults) {
        log.info("search searchString => {} options => {} propNames => {}", searchString, options, propNames);
        List<HierarchyItem> results = new LinkedList<>();
        SearchFacade.Searcher searcher = getEngine().getSearchFacade().getSearcher();
        if (searcher == null) {
            return new PageResults(results, (long) 0);
        }
        boolean snippet = propNames.stream().anyMatch(x -> SNIPPET.equalsIgnoreCase(x.getName()));
        Map<String, String> searchResult;
        try {
            String decodedPath = decode(getContextAwarePath());
            searchResult = searcher.search(searchString, options, decodedPath, snippet);
            for (Map.Entry<String, String> entry : searchResult.entrySet()) {
                try {
                    HierarchyItem item = getEngine().getHierarchyItem(entry.getKey());
                    if (item != null) {
                        if (snippet && item instanceof FileImpl) {
                            ((FileImpl) item).setSnippet(entry.getValue());
                        }
                        results.add(item);
                    }
                } catch (Exception ex) {
                    getEngine().getLogger().logError("Error during search.", ex);
                }
            }
        } catch (ServerException e) {
            getEngine().getLogger().logError("Error during search.", e);
        }
        return new PageResults((offset != null && nResults != null) ? results.stream().skip(offset).limit(nResults).collect(Collectors.toList()) : results, (long) results.size());
    }

    /**
     * Returns free bytes available to current user.
     *
     * @return Returns free bytes available to current user.
     */
    @Override
    public long getAvailableBytes() {
        // 当前文件夹下可用空间
        return getFullPath().toFile().getFreeSpace();
    }

    /**
     * Returns used bytes by current user.
     *
     * @return Returns used bytes by current user.
     */
    @Override
    public long getUsedBytes() {
        // 当前文件夹下已使用的空间
        long total = getFullPath().toFile().getTotalSpace();
        return total - getAvailableBytes();
    }

    private void removeIndex(Path sourcePath, FolderImpl itSelf) {
        List<HierarchyItem> filesToDelete = new ArrayList<>();
        getEngine().getSearchFacade().getFilesToIndex(sourcePath.toFile().listFiles(), filesToDelete, getRootFolder());
        filesToDelete.add(itSelf);
        for (HierarchyItem hi : filesToDelete) {
            try {
                getEngine().getSearchFacade().getIndexer().deleteIndex(hi);
            } catch (Exception e) {
                getEngine().getLogger().logError("Cannot delete index.", e);
            }
        }
    }

    private void addIndex(Path sourcePath, String path, String name) {
        List<HierarchyItem> filesToIndex = new ArrayList<>();
        getEngine().getSearchFacade().getFilesToIndex(sourcePath.toFile().listFiles(), filesToIndex, getRootFolder());
        getEngine().getSearchFacade().getIndexer().indexFile(name, decode(path), null, null);
        for (HierarchyItem hi : filesToIndex) {
            try {
                getEngine().getSearchFacade().getIndexer().indexFile(hi.getName(), decode(hi.getPath()), null, hi);
            } catch (Exception e) {
                getEngine().getLogger().logError("Cannot index.", e);
            }
        }
    }
}
