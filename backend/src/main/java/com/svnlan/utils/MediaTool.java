package com.svnlan.utils;

import com.svnlan.common.GlobalConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/29 16:36
 */
@Component
public class MediaTool {

    public static String getM3u8Url(String businessType, Long busId, Long sourceId, String busType) {
        String key = AESUtil.encrypt(String.format("%s@%d@%d", businessType, busId, sourceId)
                + "@" + GlobalConfig.M3U8_PLAY_KEY_SEPARATOR + System.currentTimeMillis(), GlobalConfig.M3U8_PLAY_AES_KEY, true);
        String m3u8Url = "";
        try {
            key = URLEncoder.encode(key, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
         m3u8Url = "/api/disk/mu/getMyM3u8.m3u8" + getM3u8Param(sourceId.toString(), busType, key);
        return m3u8Url;
    }

    private static String getM3u8Param(String sourceId, String busType, String m3u8Key){
        return "?sourceID=" + sourceId
                + "&sourceType=" + busType + "&key=" + m3u8Key;
    }

}
