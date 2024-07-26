package com.svnlan.home.executor;

import com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoResponseBody;
import com.svnlan.home.utils.DingUtil;
import com.svnlan.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/21 11:27
 */
@Slf4j
public class PartDingDownloadTaskExecutor implements Callable<Long> {

    private GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody;

    private String path;

    private String range;


    public PartDingDownloadTaskExecutor() {
        super();
    }

    public PartDingDownloadTaskExecutor(GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody, String path, String range) {
        this.getFileDownloadInfoResponseBody = getFileDownloadInfoResponseBody;
        this.path = path;
        this.range = range;
    }

    @Override
    public Long call() throws Exception {
        LogUtil.info("PartDingDownloadTaskExecutor 当前正在下载的文件是：" + path + ":range=" + range);
        Long partSizeTotal = 0L;
        DingUtil.dingDownloadFile(getFileDownloadInfoResponseBody, path , range);
        return partSizeTotal;
    }
}
