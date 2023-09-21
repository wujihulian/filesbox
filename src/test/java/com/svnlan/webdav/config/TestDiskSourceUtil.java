package com.svnlan.webdav.config;

import com.svnlan.home.vo.IOSourceVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class TestDiskSourceUtil {

    @Resource
    private DiskSourceUtil diskSourceUtil;

    @Test
    public void testGetIoSource() {
        Path path = Paths.get("/其他/文档(1)/sdfsdf");
        IOSourceVo ioSource = diskSourceUtil.getIoSource(path, 135L, 14250L);
        log.info("testGetIoSource ioSource => {}", ioSource);
    }
}
