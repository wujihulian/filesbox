package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.utils.zip.ZipUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Set;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/15 11:39
 */
@Component
public class SourceDownUrlUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * @Description: 删除普通下载
     * @params:  []
     * @Return:  int
     * @Modified:
     */
    public int deleteCommonDownload() {
        Set<String> members = stringRedisTemplate.opsForZSet()
                .rangeByScore(GlobalConfig.COMMON_DOWNLOAD_KEY_SET, 0, System.currentTimeMillis());
        if (CollectionUtils.isEmpty(members)){
            LogUtil.info("删除普通下载, 集合空");
            return 0;
        }
        String pre = "deleteCommonDownload 删除临时文件";
        int count = 0;
        for (String path : members){
            try {
                count ++;
                stringRedisTemplate.opsForZSet().remove(GlobalConfig.COMMON_DOWNLOAD_KEY_SET, path);
                if (StringUtil.isEmpty(path.trim())){
                    continue;
                }
                LogUtil.info(pre + " path=" + path);
                File file = new File(path);
                if (file.exists()){
                    if (!file.isDirectory() && path.indexOf("/common/down_temp") == 0){
                        LogUtil.info("普通下载删除", path);
                        file.delete();
                    }else {
                        try {
                            ZipUtils.deleteFile(file);
                        }catch (Exception e){
                            LogUtil.error(e,pre + "夹失败 ");
                        }
                    }
                }
            } catch (Exception e){
                LogUtil.error(e, pre + " 普通下载出错");
            }
        }
        return count;
    }
}
