package com.svnlan.home.executor;

import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.svnlan.home.service.DingEventService;
import com.svnlan.home.utils.AsyncDingFileUtil;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.utils.DesignTemplatesInitUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author
 */
@Configuration
public class DingTalkStreamClientConfiguration {

    @Resource
    DingEventService dingEventService;
    @Resource
    OptionTool optionTool;
    @Resource
    AsyncDingFileUtil asyncDingFileUtil;

    /**
     * @return
     * @throws Exception
     */
    @Bean(initMethod = "start")
    public OpenDingTalkClient configureStreamClient() throws Exception {
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig();
        if (ObjectUtils.isEmpty(dingConfigVo) || ObjectUtils.isEmpty(dingConfigVo.getAppKey())){
            return null;
        }

        refreshSubscribe();
        new DesignTemplatesInitUtils();

        LogUtil.info("启动时监听钉盘同步事件");
        String clientId = dingConfigVo.getAppKey();
        String clientSecret = dingConfigVo.getAppSecret();
        LogUtil.info("DingTalk connection clientId is：" + clientId + "clientSecret is：" + clientSecret );
        return dingEventService.configureStreamClient(clientId, clientSecret);
    }

    /** 启动时订阅 */
    public void refreshSubscribe() {
        LogUtil.info("启动时订阅存储事件");
        try {
            asyncDingFileUtil.refreshSubscribe();
        }catch (Exception e){
            LogUtil.error(e, "启动时订阅存储事件失败");
        }

    }
}