package com.svnlan.webdav.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "webdav")
public class WebDavConfigurationProperties {
    String license;
    boolean showExceptions;
    String rootFolder;
    List<String> rootContext;
    String rootWebSocket;
    String host;
}
