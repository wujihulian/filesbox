package com.svnlan.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:  @Description:拦截配置--调用链
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    public SysInterceptor sysInterceptor() {
        return new SysInterceptor();
    }

    @Bean
    public ParentInterceptor parentInterceptor(){return new ParentInterceptor();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns = new String[]{"/api/disk/login", "/api/disk/logout", "/register" , "/service/**", "/error/**",
                "/api/disk/enterUser", "/api/disk/getSystemTime", "/api/disk/appErrLogs", "/api/disk/homeExplorer", "/api/disk/getVerifyImageCode"
                , "/api/disk/attachment/**", "/api/disk/attachment","/api/disk/mu/key","/api/disk/mu/getM3u8.m3u8","/api/disk/mu/getMyM3u8.m3u8"
                ,"/api/disk/service/mu/getMyM3u8.m3u8", "/api/disk/home", "/api/disk/share/get", "/api/disk/verification/image", "/api/disk/verification/result"
                , "/api/disk/share/pathList","/api/disk/zip/fileDownload", "/api/disk/dingCallback", "/api/disk/share/menu", "/api/disk/preview"
                , "/api/disk/share/previewNum", "/api/disk/yzwo/**","/api/ding/login/callback","/api/webdav/file/content", "/api/disk/register"
                , "/api/enwechat/login/callback", "/api/wechat/login/callback","/api/wechat/data","/api/platform/wechat/**","/api/disk/unzipList"
                , "/api/disk/share/report", "/api/disk/checkBanWord","/api/disk/scanLogin","/api/disk/findLoginAuthUrl","/api/disk/getHomepageDetail"
                , "/api/disk/getInfoList", "/api/common/like","/api/disk/getWingSystemInfo","/api/disk/video/img/**","/webdav/**","/api/disk/zip"
                ,"/api/disk/zip/*","/api/ding/even/callback","/api/enWebChat/callback","/api/disk/getAboutEnInfo","/api/disk/cube/callback"
                ,"/api/disk/fileOut/**","/api/disk/firstHomepageDetail","/api/disk/edu/**", "/api/disk/getInfoTypeList", "/api/disk/getUsingDesign"
        };//登录、注册不做token校验
        registry.addInterceptor(sysInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);

    }
}
