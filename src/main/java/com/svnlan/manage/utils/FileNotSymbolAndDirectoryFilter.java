package com.svnlan.manage.utils;

import com.svnlan.utils.LogUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class FileNotSymbolAndDirectoryFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        try {
            return !FileUtils.isSymlink(file) && !file.isDirectory();
        } catch (Exception e){
            LogUtil.error(e, "判断是否链接及目录失败");
        }
        return false;
    }
}
