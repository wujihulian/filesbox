package com.svnlan.webdav.config;


import com.ithit.webdav.server.Engine;
import com.ithit.webdav.server.Folder;
import com.ithit.webdav.server.util.StringUtil;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dto.StorageDTO;
import com.svnlan.user.service.StorageService;
import com.svnlan.webdav.attribute.ExtendedAttributesExtension;
import com.svnlan.webdav.auth.Authenticator;
import com.svnlan.webdav.auth.WebdavAuthenticator;
import com.svnlan.webdav.common.ResourceReader;
import com.svnlan.webdav.common.ResourceType;
import com.svnlan.webdav.impl.*;
import com.svnlan.webdav.websocket.HandshakeHeadersInterceptor;
import com.svnlan.webdav.websocket.SocketHandler;
import com.svnlan.webdav.websocket.WebSocketServer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EnableConfigurationProperties(WebDavConfigurationProperties.class)
@EnableWebSocket
@Configuration
@Slf4j
// extends WebMvcConfigurationSupport
public class WebDavConfiguration implements WebSocketConfigurer {
    final WebDavConfigurationProperties properties;
    final ResourceReader resourceReader;
    @Value("classpath:handler/MyCustomHandlerPage.html")
    Resource customGetHandler;
    @Value("classpath:handler/attributesErrorPage.html")
    Resource errorPage;

    @javax.annotation.Resource
    private StorageService storageService;
    private final SocketHandler socketHandler = new SocketHandler();

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("PROPFIND", "PROPPATCH", "COPY", "MOVE", "DELETE", "MKCOL", "LOCK", "UNLOCK", "PUT", "GETLIB", "VERSION-CONTROL", "CHECKIN", "CHECKOUT", "UNCHECKOUT", "REPORT", "UPDATE", "CANCELUPLOAD", "HEAD", "OPTIONS", "GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // -1 will allow to process static resources if main controller is running on the root.
//        registry.setOrder(-1);
//        registry.addResourceHandler("/wwwroot/**")
//                .addResourceLocations("classpath:/wwwroot/", "/wwwroot/");
//    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, properties.getRootWebSocket())
                .addInterceptors(new HandshakeHeadersInterceptor()).setAllowedOrigins("*");
    }

    @javax.annotation.Resource
    private Environment environment;

    //    @RequestScope
    @Bean
    public Map<Integer, WebDavEngine> engineMap(Authenticator authenticator, Folder folderImpl, com.ithit.webdav.server.File fileImpl) {
        List<StorageDTO> list;
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setLocation("uploads");
            storageDTO.setId(30038);
            StorageDTO storageDTO2 = new StorageDTO();
            storageDTO2.setLocation("aliyun");
            storageDTO2.setId(60008);
            StorageDTO storageDTO3 = new StorageDTO();
            storageDTO3.setLocation("uploads");
            storageDTO3.setId(43);
            list = Arrays.asList(storageDTO, storageDTO2, storageDTO3);
        } else {
            list = storageService.list();
        }

        Map<Integer, WebDavEngine> engineMap = new HashMap<>();
        for (StorageDTO item : list) {

            final WebDavEngine webDavEngine = new WebDavEngine(properties.getRootFolder(), properties.isShowExceptions(), properties.getRootContext());
//            webDavEngine.setStorageId(item.getId());
            webDavEngine.setLocation(item.getLocation());
            webDavEngine.setAuthenticator(authenticator);
            webDavEngine.setFolderImpl(folderImpl);
            webDavEngine.setFileImpl(fileImpl);

            final boolean extendedAttributesSupported = ExtendedAttributesExtension.isExtendedAttributesSupported(item.getLocation());

            CustomFolderGetHandler handler = new CustomFolderGetHandler(webDavEngine.getResponseCharacterEncoding(), Engine.getVersion(),
                    extendedAttributesSupported, customGetHandler(), errorPage(), properties.getRootContext().get(0));
            CustomFolderGetHandler handlerHead = new CustomFolderGetHandler(webDavEngine.getResponseCharacterEncoding(), Engine.getVersion(),
                    extendedAttributesSupported, customGetHandler(), errorPage(), properties.getRootContext().get(0));

            handler.setPreviousHandler(webDavEngine.registerMethodHandler("GET", handler));
            handlerHead.setPreviousHandler(webDavEngine.registerMethodHandler("HEAD", handlerHead));

            webDavEngine.setWebSocketServer(new WebSocketServer(socketHandler.getSessions()));

            engineMap.put(item.getId(), webDavEngine);
        }
        return engineMap;
    }


    @Bean
    public Authenticator authenticator(UserDao userDao) {
        // getUserByUserName
        WebdavAuthenticator authenticator = new WebdavAuthenticator();
        authenticator.setUserDao(userDao);
        return authenticator;
    }

    @javax.annotation.Resource
    private ApplicationContext context;

    @Bean
    public Map<ResourceType, AbstractResourceProcessor> resourceProcessorMap() {
        List<String> rootContext = properties.getRootContext();
        Map<ResourceType, AbstractResourceProcessor> processorMap = new HashMap<>();
        for (String str : rootContext) {
            String type = Paths.get(str).getName(1).toString();
            AbstractResourceProcessor processor = context.getBean(type + "ResourceProcessor", AbstractResourceProcessor.class);
            ResourceType resourceType = ResourceType.getResourceType(str);
            processorMap.put(resourceType, processor);
        }
        return processorMap;
    }

//    @Bean
//    public String rootLocalPath() {
//        return checkRootPath(properties.getRootFolder(), Paths.get(properties.getRootFolder()).normalize().toString());
//    }

    @Bean
    public String customGetHandler() {
        return getStreamAsString(customGetHandler);
    }

    @Bean
    public String errorPage() {
        return getStreamAsString(errorPage);
    }

    @Bean
    public FolderImpl folderImpl(DiskServiceOrDaoWrapper wrapper, FileImpl fileImpl, Map<ResourceType, AbstractResourceProcessor> resourceProcessorMap) {
        log.info("FolderImpl => {}", fileImpl);
        FolderImpl folder = new FolderImpl(wrapper, fileImpl);
        folder.setResourceProcessorMap(resourceProcessorMap);
        return folder;
    }

    @Bean
    public FileImpl fileImpl(DiskServiceOrDaoWrapper wrapper) {
        return new FileImpl(wrapper);
    }

    @SneakyThrows
    private String getStreamAsString(Resource customGetHandler) {
        try (InputStream is = customGetHandler.getInputStream()) {
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }

    @Bean
    public ExtendedAttributesExtension extAttrExtension(RedisTemplate<String,Object> redisTemplate) {
        return new ExtendedAttributesExtension(redisTemplate);
    }

    private String checkRootPath(String rootPath, String path) {
        String realPath = resourceReader.getRootFolder();
        if (StringUtil.isNullOrEmpty(rootPath)) {
            path = createDefaultPath();
        } else {
            if (Files.exists(Paths.get(rootPath))) {
                return path;
            }
            try {
                Path relative = Paths.get(realPath, rootPath);
                if (Files.exists(relative)) {
                    path = relative.toString();
                } else {
                    path = createDefaultPath();
                }
            } catch (Exception ignored) {
                path = createDefaultPath();
            }
        }
        return path;
    }

    private String createDefaultPath() {
        return resourceReader.getDefaultPath();
    }

    /**
     * Creates index folder if not exists.
     *
     * @return Absolute location of index folder.
     */
    private String createIndexPath() {
        Path indexLocalPath = Paths.get(resourceReader.getDefaultIndexFolder());
        if (Files.notExists(indexLocalPath)) {
            try {
                Files.createDirectory(indexLocalPath);
            } catch (IOException e) {
                return null;
            }
        }
        return indexLocalPath.toString();
    }
}
