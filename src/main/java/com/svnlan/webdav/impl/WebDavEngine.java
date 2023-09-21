package com.svnlan.webdav.impl;

import com.ithit.webdav.server.*;
import com.ithit.webdav.server.exceptions.DavException;
import com.ithit.webdav.server.exceptions.ServerException;
import com.ithit.webdav.server.util.StringUtil;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.webdav.auth.Authenticator;
import com.svnlan.webdav.common.ResourceType;
import com.svnlan.webdav.websocket.WebSocketServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Implementation if {@link Engine}.
 * Resolves hierarchy items by paths.
 */
@Slf4j
public class WebDavEngine extends Engine {

    private final Logger logger;
    private final String dataFolder;
    private final boolean showExceptions;
    private final List<String> rootContext = new ArrayList<>();
    private SearchFacade searchFacade;
    private WebSocketServer webSocketServer;

    public static ThreadLocal<ResourceType> resourceTypeThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<UserVo> userVoThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<Map<String, IOSourceVo>> favorRootSourceMap = new ThreadLocal<>();

    /**
     * Initializes a new instance of the WebDavEngine class.
     *
     * @param dataFolder     Path to the root folder to map to DAV.
     * @param showExceptions True if you want to print exceptions in the response.
     * @param rootContext    Context path where webdav service is working.
     */
    public WebDavEngine(String dataFolder, boolean showExceptions, List<String> rootContext) {
        this.dataFolder = dataFolder;
        this.showExceptions = showExceptions;
        this.logger = new SpringBootLogger(log);
        for (String item : rootContext) {
            this.rootContext.add(StringUtil.trimEnd(item, "/"));
        }
    }

    @NotNull
    @Setter
    private Authenticator authenticator;

    @Setter
    protected Folder folderImpl;

    @Setter
    protected File fileImpl;


//    @Setter
//    // 存储id
//    private Integer storageId;

    @Getter
    @Setter
    private String location;

    @Override
    public void service(DavRequest davRequest, DavResponse davResponse) throws DavException, IOException {
        try {
            super.service(davRequest, davResponse);
        } finally {
            resourceTypeThreadLocal.remove();
            userVoThreadLocal.remove();
            favorRootSourceMap.remove();
        }

    }

    /**
     * 认证
     */
    @Override
    protected boolean authenticate(DavRequest davRequest, DavResponse davResponse, String method) {
        return authenticator.doAuthenticate(davRequest, davResponse);
    }

    /**
     * Creates {@link HierarchyItem} instance by path.
     *
     * @param contextPath Item relative path including query string.
     * @return Instance of corresponding {@link HierarchyItem} or null if item is not found.
     * @throws ServerException in case if engine cannot read file attributes.
     */
    @Override
    public HierarchyItem getHierarchyItem(String contextPath) throws ServerException {
        int i = contextPath.indexOf('?');
        if (i >= 0) {
            contextPath = contextPath.substring(0, i);
        }
        log.info("getHierarchyItem contextPath => {}", contextPath);
        if (!StringUtils.hasText(contextPath)) {
            return null;
        }
        FolderImpl folderItem = ((FolderImpl) folderImpl).getFolder(contextPath, this);
        if (Objects.nonNull(folderItem)) {
            if (folderItem == FolderImpl.NULL) {
                // 这次属于 io_source 都不存在的
                return null;
            } else {
                // 这种属于不需要查询的隐藏资源或者是资源属于文件夹类型的
                return folderItem;
            }
        }
        FileImpl fileItem = ((FileImpl) fileImpl).createFileImpl(contextPath, this);
        if (fileItem != null) {
            return fileItem;
        }
        getLogger().logDebug("Could not find item that corresponds to path: " + contextPath);
        return null; // no hierarchy item corresponds to path parameter was found in the repository
    }

    /**
     * Returns logger that will be used by engine.
     *
     * @return Instance of {@link Logger}.
     */
    @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * Returns folder where data will be sourced for WebDAV
     *
     * @return data folder.
     */
    public String getDataFolder() {
        return dataFolder;
    }

    /**
     * Returns flag if exception should be printed to response.
     *
     * @return true  if exception should be printed to response.
     */
    public boolean isShowExceptions() {
        return showExceptions;
    }

    /**
     * Returns SearchFacade instance
     *
     * @return SearchFacade instance
     */
    SearchFacade getSearchFacade() {
        return searchFacade;
    }

    /**
     * Sets SearchFacade instance
     *
     * @param searchFacade SearchFacade instance
     */
    public void setSearchFacade(SearchFacade searchFacade) {
        this.searchFacade = searchFacade;
    }

    /**
     * Sets web socket server instance
     *
     * @param webSocketServer web socket server instance
     */
    public void setWebSocketServer(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    /**
     * Returns web socket server instance
     *
     * @return web socket server instance
     */
    WebSocketServer getWebSocketServer() {
        return webSocketServer;
    }

    /**
     * Returns real path considering context which may vary.
     * rootContext = /webdav
     *
     * @param path context aware path.
     * @return real path.
     */
    public String getContextAware(String path) {
//        log.info("getContextAware path => {}", path);
        String context = getRootContext(path);
        if (StringUtils.hasText(context)) {
            // /webdav/private/test/xxx.jpg
            String contextAware = path.replaceFirst(context, "/");
            return contextAware.replace("//", "/");
        }
        return null;
    }

    String getRootContext(String path) {
        for (String item : rootContext) {
            // /webdav/private
            if (path.startsWith(item)) {
                ResourceType resourceType = ResourceType.getResourceType(item);
                resourceTypeThreadLocal.set(resourceType);
                return item;
            }
        }
        return null;
    }
}
