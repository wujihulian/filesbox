package com.svnlan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/10 11:21
 */
public class Mp3InitUtils {

    public Mp3InitUtils() {
        this.init();
    }

    public static File file;


    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    static {
        try {
            file = new File("/Cheerfulness.mp3");
        } catch (Exception e) {
            LogUtil.error(e + "静态资源加载ipFile失败 IOException");
        }
    }


    public void init() {
        try {
            try {
                if (!file.exists()) {
                    file = new File(this.getClass().getResource("/Cheerfulness.mp3").getPath());
                    if (!file.exists()) {
                        file = new File("/Cheerfulness.mp3");
                        if (!file.exists()) {
                            InputStream is = this.getClass().getResourceAsStream("/Cheerfulness.mp3");
                            inputstreamtofile(is, file);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                // 如果找不到这个文件，再尝试再当前目录下搜索，这次全部改用小写文件名
                //     因为有些系统可能区分大小写导致找不到ip地址信息文件
                LogUtil.error(e, "未找到文件");
            }

        } catch (Exception e) {
            LogUtil.error(e, "ip初始化异常原因");
        }
    }

    public void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
    public static void main(String[] args) {
    }
}
