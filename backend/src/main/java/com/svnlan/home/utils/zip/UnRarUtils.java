package com.svnlan.home.utils.zip;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/6 10:58
 */
public class UnRarUtils {

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

        Archive zis = null;
        try {
            zis = new Archive (new FileInputStream(srcFile));
            //解压zip
            decompressZis(destFile, zis, fileList, userID);
        }catch (Exception e){
            LogUtil.error(e, "decompressFile 解压error");
        }finally {
            //关闭流
            try {
                if(zis != null){
                    zis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 文件 解压缩执行方法
     *
     * @param destFile 目标文件
     * @param zis TarArchiveInputStream
     * @throws Exception
     */
    private static void decompressZis(File destFile, Archive zis, List<ChangeSourceVo> fileList, Long userID)   throws Exception {
        FileHeader entry;
        Map<String, Integer> reMap = new HashMap<>(1);
        OutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            int i = 1;
            while (!ObjectUtils.isEmpty(entry = zis.nextFileHeader())) {
                //获取当前的ZIP条目路径
                String dir = destFile.getPath() + File.separator + entry.getFileNameW();
                //判断是否是文件夹
                if (entry.isDirectory()) {
                    //如果是，创建文件夹
                    // dirFile.mkdirs();

                    if (ObjectUtils.isEmpty(reMap) || (!reMap.containsKey(entry.getFileNameW()) && !reMap.containsKey(entry.getFileNameW() + "/") )) {
                        fileList.add(new ChangeSourceVo(entry.getFileNameW().endsWith("/") ? entry.getFileNameW().substring(0,entry.getFileNameW().length() -1) : entry.getFileNameW(),
                                1, dir));
                    }
                    reMap.put(entry.getFileNameW(), 1);
                    reMap.put(dir, 1);
                } else {

                    String suffix = FileUtil.getFileExtension(entry.getFileNameW());
                    String filePath = entry.getFileNameW().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO);
                    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());


                    //递归检查文件路径，路径上没有文件夹则创建，保证整条路径在本地存在
                    fileProberParent(filePath.replace(fileName, ""), destFile.getPath() + File.separator, suffix, fileList, reMap);

                    //最终文件路径  最终文件目录路径+毫秒+.后缀
                    String finalFilePath = destFile.getPath() + File.separator
                            + RandomUtil.getuuid() + System.currentTimeMillis()
                            + "_" + i +  "_" + userID + "." + suffix;


                    File f = new File(finalFilePath);
                    //如果不是文件夹，数据流输出，生成文件
                    outputStream = new FileOutputStream(f);
                    inputStream = new FileInputStream(f);
                    zis.extractFile(entry, outputStream);


                    String serverChecksum = DigestUtils.md5DigestAsHex(inputStream);

                    fileList.add(new ChangeSourceVo(fileName, 0, suffix, finalFilePath , filePath
                            , entry.getDataSize(), serverChecksum));

                }
                i ++;
            }
        }catch (Exception e){
            LogUtil.error(e, "decompressFile 解压error");
        }finally {
            //关闭流
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    /*public static void main(String[] args) {
        String unzipPath = "E:\\zip测试打包\\解压rar\\";
        String zipPath = "E:\\zip测试打包\\主观题答案.rar";
        List list = new ArrayList();

        try  {
            decompress(zipPath, unzipPath,list , 1L);
            LogUtil.info("list=" + JsonUtils.beanToJson(list));
        } catch (Exception e){

            LogUtil.error(e, "error");

        }
    }*/
}
