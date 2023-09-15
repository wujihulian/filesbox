package com.svnlan.webdav.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ithit.webdav.server.*;
import com.ithit.webdav.server.exceptions.ConflictException;
import com.ithit.webdav.server.exceptions.LockedException;
import com.ithit.webdav.server.exceptions.MultiStatusException;
import com.ithit.webdav.server.exceptions.ServerException;
import com.ithit.webdav.server.resumableupload.ResumableUpload;
import com.ithit.webdav.server.resumableupload.UploadProgress;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.enums.CloudOperateEnum;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.HttpUtil;
import com.svnlan.webdav.attribute.ExtendedAttributesExtension;
import com.svnlan.webdav.common.ResourceType;
import com.svnlan.webdav.config.DiskServiceOrDaoWrapper;
import com.svnlan.webdav.config.DiskSourceUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.svnlan.webdav.impl.WebDavEngine.userVoThreadLocal;

/**
 * Represents file in the File System repository.
 */
@ToString
@Slf4j
public final class FileImpl extends HierarchyItemImpl implements File, Lock,
        ResumableUpload, UploadProgress {

    private static final int BUFFER_SIZE = 1048576; // 1 Mb

    private String snippet;

    private OpenOption[] allowedOpenFileOptions = null;
    // 只能注入配置的 ioFile
    private IOFile ioFile;
    // 只能注入配置的 sourceVo
    @Getter
    private IOSourceVo sourceVo;

    public FileImpl(DiskServiceOrDaoWrapper wrapper) {
        setWrapper(wrapper);
    }

    /**
     * Initializes a new instance of the {@link FileImpl} class.
     *
     * @param name     Name of hierarchy item.
     * @param path     Relative to WebDAV root folder path.
     * @param created  Creation time of the hierarchy item.
     * @param modified Modification time of the hierarchy item.
     * @param engine   Instance of current {@link WebDavEngine}.
     */
    public FileImpl(String name, String path, long created, long modified, WebDavEngine engine) {
        super(name, path, created, modified, engine);

        /* Mac OS X and Ubuntu doesn't work with ExtendedOpenOption.NOSHARE_DELETE */
        String systemName = System.getProperty("os.name").toLowerCase();
        allowedOpenFileOptions = (systemName.contains("mac") || systemName.contains("linux")) ?
                (new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.READ}) :
                (new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.READ,
                        noShareDeleteOption()});
    }

    /**
     * Load ExtendedOpenOption with reflection without direct reference - because most of Linux/MacOS jdks don't have it and not required.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private OpenOption noShareDeleteOption() {
        try {
            Class enumClass = Class.forName("com.sun.nio.file.ExtendedOpenOption");
            return (OpenOption) Enum.valueOf(enumClass, "NOSHARE_DELETE");
        } catch (ClassNotFoundException e) {
            return StandardOpenOption.READ;
        }
    }

    // 文件在磁盘上的绝对路径
    @Setter
    private String absolutePath;

    /**
     * Returns file that corresponds to path.
     *
     * @param path   Encoded path relative to WebDAV root.
     * @param engine Instance of {@link WebDavEngine}
     * @return File instance or null if physical file not found in file system.
     * @throws ServerException in case of exception
     */
    FileImpl createFileImpl(String path, WebDavEngine engine) throws ServerException {
//        log.info("111>>- getFile this => {}, path => {}", this, path);

//        log.info("111getFile path => {} ", path);
        ResourceType resourceType = WebDavEngine.resourceTypeThreadLocal.get();
        if (resourceType == ResourceType.PRIVATE) {
            if (!checkIfNecessary(path, "._")) {
                return null;
            }
            String realPath = engine.getContextAware(path);
            String pathFragment = decodeAndConvertToPath(realPath);
            IOSourceVo ioSource = getSourceIoByPath(pathFragment);
            if (Objects.isNull(ioSource)) {
                return null;
            }

            long created = ioSource.getCreateTime() * 1000;
            long modified = ioSource.getModifyTime() * 1000;
            // 查询该文件资源
            IOFile ioFile = wrapper.getIoFileDao().selectById(ioSource.getFileID());
            if (Objects.isNull(ioFile)) {
                return null;
            }
            return createFileImpl(path, engine, pathFragment, ioSource, created, modified, ioFile);
        } else {
            if (!checkIfNecessary(path)) {
                return null;
            }
            String realPath = engine.getContextAware(path);
            log.info(">> getFile favor realPath => {}", realPath);
            String pathFragment = decodeAndConvertToPath(realPath);
            Map<String, IOSourceVo> stringIOSourceVoMap = WebDavEngine.favorRootSourceMap.get();
            log.info("stringIOSourceVoMap => {}", stringIOSourceVoMap);
            log.info("&&&&&&&&&&&&&&&&&& pathFragment => {}", pathFragment);
            Pair<IOSourceVo, IOFile> pair = wrapper.diskSourceUtil.getFileImplFromFavor(pathFragment);
            return createFileImpl(path, engine, pair.getSecond(), pair.getFirst());
        }
    }

    FileImpl createFileImpl(String path, WebDavEngine engine, IOFile ioFile, IOSourceVo ioSource) throws ServerException {
        log.info("222>>- getFile path => {}", path);
        if (!checkIfNecessary(path, "._")) {
            return null;
        }

        String realPath = engine.getContextAware(path);
        String pathFragment = decodeAndConvertToPath(realPath);

        long created = ioSource.getCreateTime() * 1000;
        long modified = ioSource.getModifyTime() * 1000;

        return createFileImpl(path, engine, pathFragment, ioSource, created, modified, ioFile);
    }


    private FileImpl createFileImpl(String path, WebDavEngine engine, String pathFragment, IOSourceVo ioSource, long created, long modified, IOFile ioFile) {
        FileImpl file = new FileImpl(ioSource.getName(), path, created, modified, engine);
        file.sourceVo = ioSource;
        file.wrapper = wrapper;
        file.pathFragment = pathFragment;
        if (!Objects.equals(ioFile, IOFile.NULL)) {
            file.ioFile = ioFile;
            file.absolutePath = Paths.get(ioFile.getPath()).toString();
        }
        return file;
    }

    /**
     * Array of items that are being uploaded to this item subtree.
     *
     * @return Return array with a single item if implemented on file items. Return all items that are being uploaded to this subtree if implemented on folder items.
     * @throws ServerException - in case of an error.
     */
    @Override
    public List<? extends ResumableUpload> getUploadProgress()
            throws ServerException {
        return Collections.singletonList(this);
    }

    /**
     * In this method implementation you can delete partially uploaded file.
     * <p>
     * Client do not plan to restore upload. Remove any temporary files / cleanup resources here.
     *
     * @throws LockedException - this item or its parent was locked and client did not provide lock token.
     * @throws ServerException - in case of an error.
     */
    @SneakyThrows
    @Override
    public void cancelUpload() throws LockedException, ServerException {
        ensureHasToken();
        // 将创建的文件删除，删除表记录， 如果存在的话
        if (StringUtils.hasText(absolutePath)) {
            Path path = Paths.get(absolutePath);
            Files.deleteIfExists(path);
        }

        if (Objects.nonNull(sourceVo) && Objects.nonNull(sourceVo.getSourceID())) {
            wrapper.ioSourceDao.deleteById(sourceVo.getSourceID());
        }

        if (Objects.nonNull(ioFile) && Objects.nonNull(ioFile.getFileID())) {
            wrapper.ioFileDao.deleteById(ioFile.getFileID());
        }
    }

    /**
     * Amount of bytes successfully saved to your storage.
     *
     * @return Amount of bytes successfully saved.
     * @throws ServerException in case of an error.
     */
    @Override
    public long getBytesUploaded() throws ServerException {
        return getContentLength();
    }

    /**
     * Indicates if item will be checked-in by the engine when last chunk of a file is uploaded
     * if item was checked in when upload started.
     *
     * @return True if item will be checked in when upload finishes.
     * @throws ServerException in case of an error.
     */
    @Override
    public boolean getCheckInOnFileComplete() throws ServerException {
        return false;
    }

    /**
     * Shall store value which indicates whether file will be checked in when upload finishes.
     *
     * @param value True if item will be checked in when upload finishes.
     * @throws ServerException in case of an error.
     */
    @Override
    public void setCheckInOnFileComplete(boolean value) throws ServerException {
        throw new ServerException("Not implemented");
    }

    /**
     * The date and time when the last chunk of file was saved in your storage.
     *
     * @return Time when last chunk of file was saved.
     * @throws ServerException in case of an error.
     */
    @Override
    public long getLastChunkSaved() throws ServerException {
        return getModified();
    }

    /**
     * Total file size that is being uploaded.
     *
     * @return Total file size in bytes.
     * @throws ServerException in case of an error.
     */
    @Override
    public long getTotalContentLength() throws ServerException {
        return getContentLength();
    }

    /**
     * Gets the size of the file content in bytes.
     *
     * @return Length of the file content in bytes.
     */
    @Override
    public long getContentLength() {
        return Objects.nonNull(ioFile) ? ioFile.getSize() : 0L;
    }

    /**
     * Gets the media type of the {@link FileImpl}.
     *
     * @return MIME type of the file.
     * @throws ServerException In case of an error.
     */
    @Override
    public String getContentType() throws ServerException {
        String name = this.getName();
        int periodIndex = name.lastIndexOf('.');
        String ext = name.substring(periodIndex + 1);
        String contentType = MimeType.getInstance().getMimeType(ext);
        if (contentType == null)
            contentType = "application/octet-stream";
        return contentType;
    }

    @Override
    public String getEtag() throws ServerException {
        return String.format("%s-%s", Long.hashCode(getModified()), getSerialNumber());
    }

    /**
     * Writes the content of the file to the specified stream.
     *
     * @param out        Output stream.
     * @param startIndex Zero-based byte offset in file content at which to begin copying bytes to the output stream.
     * @param count      Number of bytes to be written to the output stream.
     * @throws ServerException In case of an error.
     */
    @Override
    public void read(OutputStream out, long startIndex, long count) throws ServerException {
        Path absolute = Paths.get(ioFile.getPath());
        // 判断文件是否存在
        if (!Files.exists(absolute)) {
            return;
        }
        absolutePath = absolute.toString();
        byte[] buf = new byte[BUFFER_SIZE];
        int retVal;
        try (InputStream in = Files.newInputStream(absolute)) {
            in.skip(startIndex);
            while ((retVal = in.read(buf)) > 0) {
                // Strict servlet API doesn't allow to write more bytes then content length. So we do this trick.
                if (retVal > count) {
                    retVal = (int) count;
                }
                out.write(buf, 0, retVal);
                startIndex += retVal;
                count -= retVal;
            }
        } catch (IOException x) {
            throw new ServerException(x);
        }
    }

    private CommonSource commonSource;

    private UploadDTO uploadDTO;
    public static final String BEFORE_WRITE_PREFIX = "before_write_prefix:";

    @Override
    public boolean beforeWrite(String contentType, long expectEntityLength) {
        // 判断是否已经通过了该步骤
        if (expectEntityLength <= 0) {
            Object obj = wrapper.redisTemplate.opsForValue().get(BEFORE_WRITE_PREFIX + ioFile.getFileID());
            log.info("beforeWrite obj => {} key => {}", obj, BEFORE_WRITE_PREFIX + ioFile.getFileID());
            synchronized (this) {
                if (Boolean.TRUE.equals(wrapper.redisTemplate.hasKey(BEFORE_WRITE_PREFIX + ioFile.getFileID()))) {
                    log.info("beforeCreateFile 已判断");
                    return true;
                }
                log.info("-------------------------------->>>>");
                wrapper.redisTemplate.opsForValue().set(BEFORE_WRITE_PREFIX + ioFile.getFileID(), "", 2, TimeUnit.HOURS);
            }
        }

        log.info("beforeCreateFile 2 >> 前置检查 fileId => {}", this.ioFile.getFileID());
        CommonSource commonSource = new CommonSource();
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setSourceID(sourceVo.getSourceID());
        // userId
        LoginUser loginUser = new LoginUser();
        UserVo userVo = userVoThreadLocal.get();
        loginUser.setUserID(userVo.getUserID());
        HomeExplorerVO homeExplorerVO = wrapper.uploadService.beforeUpload(commonSource, uploadDTO, loginUser, BusTypeEnum.CLOUD);

        if (expectEntityLength > 0) {
            // 校验用户是否还需足够的空间上传该资源
            log.info("beforeCreateFile 3 >> 判断容量是否足够");
            wrapper.fileOptionTool.checkMemory(homeExplorerVO, expectEntityLength);

            String fileName = DiskSourceUtil.getFileName(pathFragment);
            String fileExt = FileUtil.getFileExtension(fileName);
            log.info("beforeWrite fileName => {}  fileExt => {}  pathFragment => {}", fileName, fileExt, pathFragment);
            // 文件后缀
            commonSource.setFileType(fileExt);
            commonSource.setName(fileName);
            // 真实的文件路径及真实的文件名
            commonSource.setPath(absolutePath);

            // 文件大小
            commonSource.setSize(expectEntityLength);
            // 文件归属类型 目前是 1 用户类型
            commonSource.setTargetType(1);
            this.commonSource = commonSource;
            log.info("uploadDTO =>{}  commonSource => {}", JSONObject.toJSONString(uploadDTO), JSONObject.toJSONString(commonSource));
            log.info("beforeCreateFile 4 >> 根据业务类型做处理");
            // 根据业务类型做处理
            wrapper.uploadService.doByBusType(uploadDTO, false, commonSource, BusTypeEnum.CLOUD, homeExplorerVO);
            this.uploadDTO = uploadDTO;
        }

        return true;
    }

    /**
     * Saves the content of the file from the specified stream to the File System repository.
     *
     * @param content         {@link InputStream} to read the content of the file from.
     * @param contentType     Indicates media type of the file.
     * @param startIndex      Index in file to which corresponds first byte in {@code content}.
     * @param totalFileLength Total size of the file being uploaded. -1 if size is unknown.
     * @return Number of bytes written.
     * @throws LockedException File was locked and client did not provide lock token.
     * @throws ServerException In case of an error.
     * @throws IOException     I/O error.
     */
    @Override
    public long write(InputStream content, String contentType, long startIndex, long totalFileLength, long expectEntityLength)
            throws LockedException, ServerException, IOException {
        log.info("fileImpl write serverPath => {}  startIndex => {} totalFileLength => {} fileId => {} expectEntityLength => {}",
                pathFragment, startIndex, totalFileLength, ioFile.getFileID(), expectEntityLength);
//        log.info("fileImpl ioFile => {} sourceVo => {}", JSONObject.toJSONString(ioFile), JSONObject.toJSONString(sourceVo));
        ensureHasToken();

        SeekableByteChannel writer = Files.newByteChannel(Paths.get(absolutePath), this.allowedOpenFileOptions);
        if (startIndex == 0) {
            // If we override the file we must set position to 0 because writer could be at not 0 position.
            writer = writer.truncate(0);
        } else {
            // We must set to start position in case of resume upload.
            writer.position(startIndex);
        }
        incrementSerialNumber();
        byte[] inputBuffer = new byte[BUFFER_SIZE];
        long totalWrittenBytes = startIndex;
        int readBytes;
        try {
            while ((readBytes = content.read(inputBuffer)) > -1) {
                ByteBuffer byteBuffer;
                byteBuffer = ByteBuffer.wrap(inputBuffer, 0, readBytes);
                writer.write(byteBuffer);
                totalWrittenBytes += readBytes;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writer.close();
        }
        getEngine().getWebSocketServer().notifyUpdated(getContextAwarePath(), getWebSocketID());
        log.info("====================== expectEntityLength => {}", expectEntityLength);
        if (expectEntityLength > 0) {
            log.info("expectEntityLength > 0  => {} ioFile => {}", expectEntityLength, ioFile);
            // 表示已经完毕，将文件大小更新到数据库中
//            wrapper.ioFileDao.updateFileSize(ioFile.getFileID(), expectEntityLength);
//            log.info("expectEntityLength > 0  => {}", expectEntityLength);
            uploadDTO.setBusType(BusTypeEnum.CLOUD.getBusType());
            commonSource.setIsEdit("1");
            commonSource.setFileID(ioFile.getFileID());
            commonSource.setSourceID(sourceVo.getSourceID());
            commonSource.setSize(expectEntityLength);
            wrapper.uploadService.recordSourceDataToDb(commonSource, uploadDTO, BusTypeEnum.CLOUD);

            // 文件转码
            HttpServletRequest request = HttpUtil.getRequest();
            String serverUrl = HttpUtil.getRequestRootUrl(request);
            log.info("write serverUrl => {}", serverUrl);
            commonSource.setDomain(serverUrl);
            wrapper.asyncTaskExecutor.execute(this::fileTranscodingAsync);
            // 后续清理工作 包含清理缓存数据，数据库中以._开头的数据
            Pair<String, String> pair = getCacheKeyPair();
            log.info("fileImpl pair => {}", pair);
            FileCacheInfo fileCacheInfo = (FileCacheInfo) wrapper.redisTemplate.opsForValue().get(pair.getFirst());
            log.info("fileImpl fileCacheInfo => {}", fileCacheInfo);
            if (Objects.nonNull(fileCacheInfo)) {
                wrapper.redisTemplate.delete(pair.getFirst());
            }
            FileCacheInfo fileCacheTempInfo = (FileCacheInfo) wrapper.redisTemplate.opsForValue().get(pair.getSecond());
            log.info("fileImpl fileCacheTempInfo => {}", fileCacheTempInfo);
            if (Objects.nonNull(fileCacheTempInfo)) {
                wrapper.redisTemplate.delete(pair.getSecond());
                // 删除数据库中对应的数据
                Long fileId = fileCacheTempInfo.getFileId();
                Long sourceId = fileCacheTempInfo.getSourceId();
                log.info("write fileId => {} sourceId => {}", fileId, sourceId);
                wrapper.ioFileDao.deleteById(fileId);
                wrapper.ioSourceDao.deleteById(sourceId);
            }
            wrapper.redisTemplate.delete(BEFORE_WRITE_PREFIX + ioFile.getFileID());
        }
        return totalWrittenBytes;
    }

    private Pair<String, String> getCacheKeyPair() {
        UserVo userVo = userVoThreadLocal.get();
        String separator = java.io.File.separator;
        String tempPathFragment = pathFragment.replace("/", separator);
        String key = TEMP_FILE_PREFIX + userVo.getUserID() + tempPathFragment;
        log.info("getCacheKeyPair pathFragment => {}", tempPathFragment);
        int lastSlashIndex = tempPathFragment.lastIndexOf(separator);
        String parent = tempPathFragment.substring(0, lastSlashIndex);
        String fileName = tempPathFragment.substring(lastSlashIndex + 1);
        String tempKey = TEMP_FILE_PREFIX + userVo.getUserID() + parent + separator + "._" + fileName;

        return Pair.of(key, tempKey);
    }
    // getCacheKeyPair pathFragment => \vmware.log
    // getCacheKeyPair pathFragment => \yyy\vmware-0.log

    /**
     * 文件转码
     */
    public void fileTranscodingAsync() {
        // 数据转码
        CommonSourceVO commonSourceVo = new CommonSourceVO();
        commonSourceVo.setFileID(ioFile.getFileID());
        commonSourceVo.setSourceID(sourceVo.getSourceID());
        commonSourceVo.setFileType(sourceVo.getFileType());
        log.info(">>- 转码操作");
        wrapper.uploadService.tryToConvertFile(commonSourceVo, commonSource, BusTypeEnum.CLOUD.getBusType());
    }

    private void incrementSerialNumber() {
        try {
            Property serialNumber = Property.create("", "SerialNumber", "1");
            String sn = getSerialNumber();
            if (!Objects.equals(sn, "0")) {
                serialNumber.setValue(String.valueOf((Integer.parseInt(sn) + 1)));
            }
//            ExtendedAttributesExtension.setExtendedAttribute(getFullPath().toString(), "SerialNumber",
//                    SerializationUtils.serialize(Collections.singletonList(serialNumber)));
            log.info("incrementSerialNumber getAbsolutePath => {}", getAbsolutePath());
            wrapper.extAttrExtension.setExtendedAttribute(getAbsolutePath(), "SerialNumber",
                    SerializationUtils.serialize(Collections.singletonList(serialNumber)));
        } catch (Exception ex) {
            getEngine().getLogger().logError("Cannot update serial number.", ex);
        }
    }

    private String getSerialNumber() throws ServerException {
        String serialJson = wrapper.extAttrExtension.getExtendedAttribute(getAbsolutePath(), "SerialNumber");
        List<ExtendAttribute> extendAttributes = JSON.parseArray(serialJson, ExtendAttribute.class);
        if (!CollectionUtils.isEmpty(extendAttributes)) {
            List<Property> properties = extendAttributes.stream()
                    .map(it -> new Property(it.getNamespace(), it.getName(), it.getXmlValueRaw()))
                    .collect(Collectors.toList());
            if (properties.size() == 1) {
                return properties.get(0).getXmlValueRaw();
            }
        }
        return "0";
    }

    @Data
    static class ExtendAttribute {
        private String name;
        private String namespace;
        private String xmlValueRaw;
    }


    @Override
    public void delete() throws LockedException, MultiStatusException, ServerException {
        ensureHasToken();
        try {
            wrapper.getDiskSourceUtil().derivePopulateAndSuccessor(sourceVo, CloudOperateEnum.DELETE_FILE, false, null);
            // 清理缓存
            Long userId = userVoThreadLocal.get().getUserID();
            log.info("cache delete userId => {} pathFragment => {}", userId, pathFragment);
            String key = "/" + userId + pathFragment.replace("\\", "/");
            // zhy-do ioSourcePath 缓存失效
            CaffeineUtil.PATH_IO_SOURCE_MAP_CACHE.invalidate(key);
        } catch (Exception e) {
            getEngine().getLogger().logError("Tried to delete file in use.", e);
            throw new ServerException(e);
        }
        getEngine().getWebSocketServer().notifyDeleted(getPath(), getWebSocketID());
    }

    @Override
    String getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public void copyTo(Folder folder, String destName, boolean deep)
            throws LockedException, MultiStatusException, ServerException, ConflictException {
        ((FolderImpl) folder).ensureHasToken();
        String destinationFolder = Paths.get(getRootFolder(), decodeAndConvertToPath(((FolderImpl) folder).getContextAwarePath())).toString();
        if (!Files.exists(Paths.get(destinationFolder))) {
            throw new ConflictException();
        }
        Path newPath = Paths.get(destinationFolder, destName);
        try {
            Files.copy(getFullPath(), newPath);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        // Locks should not be copied, delete them
        if (wrapper.extAttrExtension.hasExtendedAttribute(newPath.toString(), activeLocksAttribute)) {
            wrapper.extAttrExtension.deleteExtendedAttribute(newPath.toString(), activeLocksAttribute);
        }
        try {
            String currentPath = ((FolderImpl) folder).getContextAwarePath() + encode(destName);
            getEngine().getWebSocketServer().notifyCreated(folder.getPath() + encode(destName), getWebSocketID());
            getEngine().getSearchFacade().getIndexer().indexFile(decode(destName), decode(currentPath), null, this);
        } catch (Exception ex) {
            getEngine().getLogger().logError("Errors during indexing.", ex);
        }
    }

    @Override
    public void moveTo(Folder folder, String destName) throws LockedException,
            ConflictException, MultiStatusException, ServerException {
        ensureHasToken();
        FolderImpl folderImpl = (FolderImpl) folder;
        folderImpl.ensureHasToken();
        // /webdav/private/webdav_test/45433/
        // 34522.doc
//        log.info("moveTo folder => {}  destName => {}", ((FolderImpl) folder).getSourceVo(), destName);
        wrapper.diskSourceUtil.moveTo(sourceVo, folderImpl.getSourceVo());
        String destinationFolder = Paths.get(getRootFolder(), decodeAndConvertToPath(((FolderImpl) folder).getContextAwarePath())).toString();
        Path newPath = Paths.get(destinationFolder, destName);

        setName(destName);
        // Locks should not be copied, delete them
        if (wrapper.extAttrExtension.hasExtendedAttribute(newPath.toString(), activeLocksAttribute)) {
            wrapper.extAttrExtension.deleteExtendedAttribute(newPath.toString(), activeLocksAttribute);
        }
        try {
            getEngine().getWebSocketServer().notifyMoved(getPath(), folder.getPath() + encode(destName), getWebSocketID());
        } catch (Exception ex) {
            getEngine().getLogger().logError("Errors during indexing.", ex);
        }
    }

    @Override
    public boolean checkIfRename(Folder folder) {
        FolderImpl folderImpl = (FolderImpl) folder;
        String parentLevel = sourceVo.getParentLevel();
        IOSourceVo folderImplSourceVo = folderImpl.getSourceVo();
        return (folderImplSourceVo.getParentLevel() + folderImplSourceVo.getSourceID() + ",").equals(parentLevel);
    }

    @Override
    public void renameTo(Folder folder, String destName) {
        destName = decode(getEngine().getContextAware(destName));
        wrapper.diskSourceUtil.renameResource(sourceVo, destName);
    }

    /**
     * Returns snippet of file content that matches search conditions.
     *
     * @return Snippet of file content that matches search conditions.
     */
    String getSnippet() {
        return snippet;
    }

    /**
     * Sets snippet of file content that matches search conditions.
     *
     * @param snippet Snippet of file content that matches search conditions.
     */
    void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
