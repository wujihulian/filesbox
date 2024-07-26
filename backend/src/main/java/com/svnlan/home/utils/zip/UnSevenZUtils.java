package com.svnlan.home.utils.zip;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/6 10:58
 */
public class UnSevenZUtils {

    private static final int BUFFER = 1024;

    /**
     * 解压入口方法
     *
     * @param zipPath zip文件物理地址
     * @param unzipPath 解压后存放路径
     * @throws Exception
     */
    public static void decompress(String zipPath, String unzipPath, List<ChangeSourceVo> fileList, Long userID)  throws Exception {
        //解压缩执行方法
        decompressFile(new File(zipPath), new File(unzipPath), fileList, userID);
    }

    /**
     * 解压缩执行方法
     *
     * @param srcFile 压缩文件File实体
     * @param destFile 解压路径File实体
     * @throws Exception
     */
    public static void decompressFile(File srcFile, File destFile, List<ChangeSourceVo> fileList, Long userID) throws Exception {

        //创建压缩输入流

        SevenZFile zis = null;
        try {
            zis = new SevenZFile (srcFile);
            //解压zip
            decompressZis(destFile, zis, fileList, userID);
        }catch (Exception e){
            LogUtil.error(e, "decompressFile 解压error");
        }finally {
            //zis1.close();
            //关闭流
            zis.close();
        }
    }
    /**
     * 生成文件
     *
     * @param destFile 目标文件
     * @param zis TarArchiveInputStream
     * @throws Exception
     */
    private static void decompressFile(File destFile, SevenZFile zis) throws Exception {
        //创建输出流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        //转成byte数组
        int count;
        byte[] data = new byte[BUFFER];
        //读取并写入文件
        while ((count = zis.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        //关闭数据流
        bos.close();
    }
    /**
     * 文件 解压缩执行方法
     *
     * @param destFile 目标文件
     * @param zis TarArchiveInputStream
     * @throws Exception
     */
    private static void decompressZis(File destFile, SevenZFile zis, List<ChangeSourceVo> fileList, Long userID)   throws Exception {
        SevenZArchiveEntry entry;
        Map<String, Integer> reMap = new HashMap<>(1);

        int i = 1;
        while (!ObjectUtils.isEmpty(entry = zis.getNextEntry())) {
            //获取当前的ZIP条目路径
            String dir = destFile.getPath() + File.separator + entry.getName();
            //判断是否是文件夹
            if (entry.isDirectory()) {
                //如果是，创建文件夹
                // dirFile.mkdirs();
                if (ObjectUtils.isEmpty(reMap) || !reMap.containsKey(entry.getName()) ) {
                    fileList.add(new ChangeSourceVo(entry.getName().endsWith("/") ? entry.getName().substring(0,entry.getName().length() -1) : entry.getName(),
                            1, dir));
                }
                reMap.put(entry.getName(), 1);
                reMap.put(dir, 1);
            } else {

                String suffix = FileUtil.getFileExtension(entry.getName());
                String filePath = entry.getName().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());


                //递归检查文件路径，路径上没有文件夹则创建，保证整条路径在本地存在
                fileProberParent(filePath.replace(fileName, ""), destFile.getPath() + File.separator, suffix, fileList, reMap);

                //最终文件路径  最终文件目录路径+毫秒+.后缀
                String finalFilePath = destFile.getPath() + File.separator
                        + RandomUtil.getuuid() + System.currentTimeMillis()
                        + "_" + i +  "_" + userID + "." + suffix;


                File f = new File(finalFilePath);
                //如果不是文件夹，数据流输出，生成文件
                decompressFile(f, zis);

                String serverChecksum = DigestUtils.md5DigestAsHex(new FileInputStream(f));

                fileList.add(new ChangeSourceVo(fileName, 0, suffix, finalFilePath , filePath
                        , entry.getSize(), serverChecksum));
                // Integer isFolder, String fileType, String path, String filePath, Long size, String hashMd5

            }
            i ++;
        }
    }


    private static void fileProberParent(String parentPath, String destPath, String suffix, List<ChangeSourceVo> fileList, Map<String, Integer> reMap) {
        if (!ObjectUtils.isEmpty(reMap) && reMap.containsKey(parentPath)){
            return;
        }
        List<String> parentList = Arrays.asList(parentPath.split("/")).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(parentList)){
            StringBuilder sb = new StringBuilder();
            for (String parent : parentList){
                if (parent.endsWith(suffix)){
                    continue;
                }
                sb.append(parent + "/");
                if (ObjectUtils.isEmpty(reMap) || !reMap.containsKey(sb.toString())) {
                    fileList.add(new ChangeSourceVo(parent, 1, destPath + sb.toString()));
                }
                reMap.put(sb.toString(), 1);
            }
        }
    }

    public static void main(String[] args) {
        String unzipPath = "E:\\zip测试打包\\解压7z\\";
        String zipPath = "E:\\zip测试打包\\42_243_144.7z";
        List list = new ArrayList();

        try  {
            decompress(zipPath, unzipPath,list , 1L);
            LogUtil.info("list=" + JsonUtils.beanToJson(list));
        } catch (Exception e){

        }
    }
}
