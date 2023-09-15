package com.svnlan.webdav;

import com.ithit.webdav.server.exceptions.DavException;
import com.ithit.webdav.server.exceptions.WebDavStatus;
import com.ithit.webdav.server.util.StringUtil;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.webdav.auth.Authenticator;
import com.svnlan.webdav.common.ResourceType;
import com.svnlan.webdav.config.DiskSourceUtil;
import com.svnlan.webdav.config.WebDavConfigurationProperties;
import com.svnlan.webdav.impl.AbstractResourceProcessor;
import com.svnlan.webdav.impl.WebDavEngine;
import com.svnlan.webdav.servlet.HttpServletDavRequest;
import com.svnlan.webdav.servlet.HttpServletDavResponse;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@CrossOrigin("*")
@Slf4j
public class WebdavEndpoint {

    final WebDavConfigurationProperties properties;

    private Authenticator authenticator;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    protected DiskSourceUtil diskSourceUtil;

    @Resource
    private Map<ResourceType, AbstractResourceProcessor> resourceProcessorMap;

    /**
     * 获取 windows 系统的 cmd 脚本
     */
    @GetMapping("webdav/windowsCmdFile")
    public void getWindowCmdFile(HttpServletResponse resp) {
        String cmdFile = "dav.cmd";

        try (InputStream is = new ClassPathResource("WEBDAV-INF/" + cmdFile).getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            IOUtils.copy(is, baos);
            String cmdContent = baos.toString(StandardCharsets.UTF_8.name());
            LoginUser loginUser = loginUserUtil.getLoginUser();
//            String webdavUrl = generateWebDavUrl(loginUser, ResourceType.PRIVATE);
            String webdavUrl = Paths.get(properties.getHost(), properties.getRootContext().get(0)).toString();
            String name = loginUser.getName();
            log.info("getWindowCmdFile => {}", cmdContent);
            cmdContent = cmdContent.replaceFirst("\\{webDavUrl\\}", webdavUrl)
                    .replaceFirst("\\{user\\}", name);
            log.info("getWindowCmdFile => {}", cmdContent);

            // 然后写入到输出流中
            resp.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(cmdFile, "utf-8"));
            try (
                    ServletOutputStream outputStream = resp.getOutputStream();
                    ByteArrayInputStream bais = new ByteArrayInputStream(cmdContent.getBytes(StandardCharsets.UTF_8))) {
                IOUtils.copy(bais, outputStream);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @RequestMapping(path = "${webdav.rootContext[0]}/**", produces = MediaType.ALL_VALUE, headers = "Connection!=Upgrade")
    public void privateWebDav(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("webdav path => {}", httpServletRequest.getRequestURI());
        performDavRequest(httpServletRequest, httpServletResponse);
    }

    @RequestMapping(path = "${webdav.rootContext[0]}/**", produces = MediaType.ALL_VALUE, method = {RequestMethod.OPTIONS}, headers = "Connection!=Upgrade")
    public void privateOption(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("options path => {}", httpServletRequest.getRequestURI());
        performDavRequest(httpServletRequest, httpServletResponse);
    }

    @RequestMapping(path = "${webdav.rootContext[1]}/**", produces = MediaType.ALL_VALUE, headers = "Connection!=Upgrade")
    public void favorWebDav(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("webdav path => {} method => {}", httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        // 收藏夹里仅支持查看 所以对应一些修改操作是不支持的
        if (Arrays.stream(notAllowedMethodInFavor).anyMatch(it -> httpServletRequest.getMethod().equals(it))) {
            httpServletResponse.setStatus(HttpStatus.SC_METHOD_NOT_ALLOWED);
            return;
        }
        performDavRequest(httpServletRequest, httpServletResponse);
    }
    // 在收藏夹中，不能被允许的操作
    // PUT 对应的是上传
    // MKCOL 对应创建文件夹
    // MOVE 移动 或者 重命名
    // DELETE 对应删除  本来删除在这里可以看作是对该资源取消收藏，但是上传 PUT, 如果遇到了同名资源，并且选择覆盖的话，客户端会先发起删出，
    // 因这两者删除区分不出来，故 DELETE 也不支持
    static final String[] notAllowedMethodInFavor = {"PUT", "MKCOL", "MOVE", "DELETE"};

    @RequestMapping(path = "${webdav.rootContext[1]}/**", produces = MediaType.ALL_VALUE, method = {RequestMethod.OPTIONS}, headers = "Connection!=Upgrade")
    public void favorOption(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        log.info("options path => {}", httpServletRequest.getRequestURI());
        performDavRequest(httpServletRequest, httpServletResponse);
    }

    private void performDavRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        HttpServletDavRequest davRequest = new HttpServletDavRequest(httpServletRequest) {
            @Override
            public String getServerPath() {
                return "/";
            }
        };
        HttpServletDavResponse davResponse = new HttpServletDavResponse(httpServletResponse);
        // 认证
        if (!authenticator.doAuthenticate(davRequest, davResponse)) {
            return;
        }

        // 根据 url 计算出用哪个 engine
        String requestUrl = httpServletRequest.getRequestURI();
        log.info("performDavRequest requestUrl => {}", requestUrl);
        if (requestUrl.endsWith(".DS_Store") || requestUrl.endsWith("._.")) {
            return;
        }
        if (requestUrl.startsWith("//")) {
            requestUrl = StringUtil.trimStart(requestUrl, "/");
        }
        String contextRequestUrl = getContextAware(requestUrl);
        log.info("performDavRequest contextRequestUrl => {}", contextRequestUrl);
        // 资源类型
        ResourceType resourceType = WebDavEngine.resourceTypeThreadLocal.get();
        WebDavEngine engine = null;
        if (resourceType == ResourceType.PRIVATE) {
            IOSourceVo ioSource = diskSourceUtil.getIoSource(Paths.get(decodeAndConvertToPath(contextRequestUrl)));
            log.info("performDavRequest ioSource => {}", ioSource);

            if (Objects.nonNull(ioSource)) {
                Integer storageId = ioSource.getStorageID();
                engine = engineMap.get(storageId);
            } else {
                // 返回默认的
                engine = resolveDefaultEngine();
            }

        } else if (resourceType == ResourceType.FAVOR) {
            engine = resolveDefaultEngine();
        }

        try {
            engine.service(davRequest, davResponse);
        } catch (DavException e) {
            if (e.getStatus() == WebDavStatus.INTERNAL_ERROR) {
                engine.getLogger().logError("Exception during request processing", e);
                if (engine.isShowExceptions())
                    e.printStackTrace(new PrintStream(davResponse.getOutputStream()));
            }
        }

    }

    @Resource
    protected Map<Integer, WebDavEngine> engineMap;

    @Resource
    protected StorageService storageService;

    @Resource
    protected Environment environment;

    /**
     * 返回默认的 webDavEngine
     */
    public WebDavEngine resolveDefaultEngine() {
        WebDavEngine engine = null;
        // 返回默认的
        Integer storageId = storageService.getDefaultStorageDeviceId();
        for (Integer key : engineMap.keySet()) {
            if (Objects.equals(key, storageId)) {
                engine = engineMap.get(key);
            }
        }
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            engine = engineMap.values().stream().findFirst().get();
        } else {
            Assert.notNull(engine, "未查询到 engine 实例");
        }
        return engine;
    }

    public String getContextAware(String path) {
        String context = getRootContext(path);
//        log.info("getContextAware path => {}", context);
        if (StringUtils.hasText(context)) {
            // /webdav/private/test/xxx.jpg
            String contextAware = path.replaceFirst(context, "/");
            return contextAware.replace("//", "/");
        }
        return null;
    }

    String getRootContext(String path) {
        for (String item : properties.getRootContext()) {
//            log.info("getRootContext item => {}", item);
//            log.info("getRootContext path => {}", path);
            item = StringUtil.trimEnd(item, "/");
            // /webdav/private
            if (path.startsWith(item)) {
                ResourceType resourceType = ResourceType.getResourceType(item);
                WebDavEngine.resourceTypeThreadLocal.set(resourceType);
                return item;
            }
        }
        return null;
    }

    protected static String decodeAndConvertToPath(String url) {
        String path = decode(url);
        return path.replace("/", File.separator);
    }

    static String decode(String url) {
        try {
            return URLDecoder.decode(url.replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLDecoder.decode(url.replace("+", "%2B"));
        }
    }

}
