package com.svnlan.webdav;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 文件相关配置
 *
 * @author lingxu 2023/04/14 11:14
 */
@Data
@Component
@PropertySource("classpath:upconfig.properties")
public class FileProperties {

    @Value("${cloud.savePath}")
    private String filePathRoot;

    @Value("${webdav.prefix}")
    private String webdavPrefix;

    @Value("${webdav.static.host:http://127.0.0.1:5000}")
    private String webdavStaticHost;

    public String webdavPrefixPath() {
        return "/" + webdavPrefix;
    }
}

