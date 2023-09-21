package com.svnlan.home.utils.zip;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip压缩工具类
 *  @className: UnZipUtils
 *  @author:
 *  @date:
 */
@Component
public class UnZipUtils {

    private static final int BUFFER = 1024;
    private static final String CODING_UTF8 = "UTF-8";
    private static final String CODING_GBK = "GBK";

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
        //创建数据输入流
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());

        //创建压缩输入流
         ZipInputStream zis = new ZipInputStream(cis, Charset.forName(CODING_GBK));
         //ZipInputStream zis = new ZipInputStream(cis, Charset.forName(CODING_UTF8));
        //异常捕获的方式判断编码格式

       /* try {
            //判断代码，如果此代码未抛出异常，则表示编码为UTF-8
            zis1.getNextEntry().getName();
        } catch (Exception e) {
            //如果乱码会抛异常，抛异常重新创建输入流，重新设置编码格式
            cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
            zis = new ZipInputStream(cis, Charset.forName(CODING_GBK));
        }*/
        try {
            //解压zip
            decompressZis(destFile, zis, fileList, userID);
        }catch (Exception e){
            LogUtil.error(e, "decompressFile 解压error");
        }finally {
            //关闭流
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    LogUtil.error(e,"zos error");
                }
            }
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException e) {
                    LogUtil.error(e,"cis error");
                }
            }
        }
    }

    /**
     * 文件 解压缩执行方法
     *
     * @param destFile 目标文件
     * @param zis ZipInputStream
     * @throws Exception
     */
    private static void decompressZis(File destFile, ZipInputStream zis, List<ChangeSourceVo> fileList, Long userID)   throws Exception {
        ZipEntry entry;

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


                //如果不是文件夹，数据流输出，生成文件
                decompressFile(new File(finalFilePath), zis);

                String serverChecksum = DigestUtils.md5DigestAsHex(zis);

                fileList.add(new ChangeSourceVo(fileName, 0, suffix, finalFilePath , filePath
                        , entry.getSize(), serverChecksum));
                // Integer isFolder, String fileType, String path, String filePath, Long size, String hashMd5

            }
            i ++;
            //关闭当前的ZIP条目并定位流
            zis.closeEntry();
        }
    }

    /**
     * 文件探针，当父目录不存在时，创建目录
     *
     * @param dirFile ZIP条目路径
     */
    private static void fileProber(File dirFile) {
        //获取此路径的父目录
        File parentFile = dirFile.getParentFile();
        //判断是否存在
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            fileProber(parentFile);
            //直至存在，递归执行创建文件夹
            parentFile.mkdir();

        }

    }

    private static void fileProberParent(String parentPath, String destPath, String suffix, List<ChangeSourceVo> fileList, Map<String, Integer> reMap) {
        if (!ObjectUtils.isEmpty(reMap) && reMap.containsKey(parentPath)){
            return;
        }
        List<String> parentList = Arrays.asList(parentPath.split("/")).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());;

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

    /**
     * 生成文件
     *
     * @param destFile 目标文件
     * @param zis ZipInputStream
     * @throws Exception
     */
    private static void decompressFile(File destFile, ZipInputStream zis) throws Exception {
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

    /*public static void main(String[] args){

        try {

        List<ChangeSourceVo> fileList = new ArrayList<>();
            decompress("E:\\zip测试打包\\999__.zip", "/uploads/private/cloud/2023_4/1/2806/", fileList, 1L);
            fileList = fileList.stream().sorted(Comparator.comparing(ChangeSourceVo::getPathLength)).collect(Collectors.toList());
            System.out.println(JsonUtils.beanToJson(fileList));
        } catch (Exception e) {
            LogUtil.error(e);
            e.printStackTrace();
        }
    }*/

}
