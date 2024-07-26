package com.svnlan.home.utils.zip;

import com.svnlan.common.GlobalConfig;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/14 16:58
 */
public class ZipUtils {

    private static final int BUFFER_SIZE = 2 * 1024;
    /**
     * @return    suffix  目录名称      pathName  目录根路径
     * @Description 创建一个临时目录
     **/
    public static java.nio.file.Path createDirectory(String suffix, String pathName) {
        java.nio.file.Path path1 = Paths.get(pathName + "/" + suffix);
        //判断目录是否存在
        boolean pathExists = Files.exists(path1, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        if (pathExists) {
            return path1;
        }
        try {
            path1 = Files.createDirectories(path1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path1;
    }

    /**
     * @return  OriginalPathName   源文件    path  目标文件地址    pathName  根目录
     * @Description   复制文件
     **/
    public static void copyFile(String OriginalPathName, java.nio.file.Path path, String pathName) {
        copyFile(pathName + "/" + OriginalPathName, path);
    }
    /**
     * @return  OriginalPath   源文件地址    path  目标文件地址
     * @Description   复制文件
     **/
    public static void copyFile(String OriginalPath, java.nio.file.Path path) {
        java.nio.file.Path originalPath = Paths.get(OriginalPath);

        try {
            Files.copy(originalPath, path,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @return   attachments  这个参数主要是从数据库中获取文件的信息，这里可以根据自己的需求更改
     * @Description  向文件夹中添加文件
     **/
    public static void addFile(List<IOSourceVo> attachments, String finalFolderPath) {
        //遍历所有的文件
        for (IOSourceVo attachment : attachments) {
            if (0 == attachment.getIsFolder()){
                java.nio.file.Path dirPath = createDirectory(attachment.getPathDisplay() + attachment.getName(), finalFolderPath);
                File file = new File(attachment.getPath());
                attachment.setSize(file.length());
                copyFile(attachment.getPath(), dirPath);
            }else {
                createDirectory(attachment.getPathDisplay() + attachment.getName() + "/", finalFolderPath);
            }
        }
    }
    /** 只创建目录 文件则用复制形式*/
    public static void addFileNew(List<IOSourceVo> attachments, String finalFolderPath) {
        //遍历所有的文件
        for (IOSourceVo attachment : attachments) {
            if (1 == attachment.getIsFolder()){
                createDirectory(attachment.getPathDisplay() + attachment.getName() + "/", finalFolderPath);
            }
        }
    }

    /** 压缩目录  KeepDirStructure 一级目录是否保留 nextKeepDirStructure 下级目录是否保留 */
    public static void toZip(String srcDir, String finalFilePath, boolean KeepDirStructure, StringRedisTemplate stringRedisTemplate, String taskID, long totalLength) throws RuntimeException {
        toZip(srcDir, finalFilePath, KeepDirStructure, KeepDirStructure, stringRedisTemplate, taskID, totalLength);
    }
    public static void toZip(String srcDir, String finalFilePath, boolean KeepDirStructure, boolean nextKeepDirStructure, StringRedisTemplate stringRedisTemplate
            , String taskID, long totalLength) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(finalFilePath);
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);

            long nread = 0L;

            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure, nextKeepDirStructure, stringRedisTemplate, taskID, totalLength, nread);
            long end = System.currentTimeMillis();
            LogUtil.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /** 递归压缩  KeepDirStructure 一级目录是否保留 nextKeepDirStructure 下级目录是否保留*/
    public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure
            , StringRedisTemplate stringRedisTemplate, String taskID, long length, long nread)throws Exception{
        compress(sourceFile, zos, name, KeepDirStructure, KeepDirStructure, stringRedisTemplate, taskID, length, nread);
    }
    public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure, boolean nextKeepDirStructure
            , StringRedisTemplate stringRedisTemplate, String taskID, long length, long nread)
            throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
                nread += len;
                updateProgress(nread, length, stringRedisTemplate, taskID);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure, stringRedisTemplate, taskID, length, nread);
                    } else {
                        compress(file, zos, file.getName(), nextKeepDirStructure, stringRedisTemplate, taskID, length, nread);
                    }
                }
            }
        }
    }

    /**
     * 下载文件流
     * @param response
     * @param fileName
     * @param inputData
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse response, String fileName, InputStream inputData) throws IOException {

        BufferedInputStream bins = new BufferedInputStream(inputData);//放到缓冲流里面
        OutputStream outs = response.getOutputStream();//获取文件输出IO流
        BufferedOutputStream bouts = new BufferedOutputStream(outs);
        response.setContentType("application/x-download");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        //开始向网络传输文件流
        while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
            bouts.write(buffer, 0, bytesRead);
        }
        bouts.flush();//这里一定要调用flush()方法
        inputData.close();
        bins.close();
        outs.close();
        bouts.close();

    }
    /** 删除临时的.zip文件*/
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    deleteFile(subFile);
                }
            }
            if (file.exists())
                file.delete(); // 删除文件夹
        } else {
            if (file.exists())
                file.delete();
        }
    }
    /**
     * @return
     * @Description 删除目录及文件
     **/
    public static void deleteDirAndFiles(File file) {
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                deleteDirAndFiles(f);
            }
        }
        file.delete();
        System.out.println("删除成功" + file.getName());
    }


    /**
     * 文件压缩功能 * @param sourcesFiles 源资源，即需要被压缩的文件 * @param zos 压缩包 * @param fileName 压缩包内的文件名称
     */
    public static void compressFile(File sourcesFiles, ZipOutputStream zos, String fileName) throws IOException {
        //1.如果源资源是目录，先判断是否空文件夹
        if (sourcesFiles.isDirectory()) {
            File[] files = sourcesFiles.listFiles();
            //1-1.先判断是否空文件夹
            if (files.length == 0) {
                //1-2.空文件夹，则在压缩包中把目录加上
                zos.putNextEntry(new ZipEntry(fileName + File.separator));
            } else {
                //1-3.如果不是空文件夹，则递归列出里面的文件以及文件夹
                for (File file : files) {
                    //文件名称以目录保持好
                    compressFile(file, zos, fileName + File.separator + file.getName());
                }
            }
        } else {
            //将文件放到压缩文件中，同时保留文件名称
            zos.putNextEntry(new ZipEntry(fileName));
            //IO的固化操作，先读取文件再写入文件压缩输出流中，过程不解释
            FileInputStream inputStream = new FileInputStream(sourcesFiles);
            int len;
            byte[] bytes = new byte[2048];
            while ((len = inputStream.read(bytes)) != -1) {
                zos.write(bytes, 0, len);
            }
            zos.flush();
            //刷新流
            zos.closeEntry();
            inputStream.close();
        }
    }

    /**
       * @Description: 解压zip
       * @params: zipFile 读取文件路径 descDir 输出目标路径
       */
    public String unzip(File zipFile,String descDir) {
        String outPath = null;
        File pathFile = new File(descDir);//创建目标路径
        if (!pathFile.exists()) {//判断是否存在文件夹
            pathFile.mkdirs();//创建文件夹
        }

        try {
            ZipFile zip = new ZipFile(zipFile);//可以使用文件流获取文件路径进行创建流
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                //遍历所有文件
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                outPath = (descDir + zipEntryName).replaceAll("\\\\", "/");

                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();//关闭
                out.flush();//缓冲
                out.close();//关闭输出流
            }
        }catch (Exception e){

        }

        System.out.println("******************解压完毕********************");
        return outPath;//返回路径地址
    }

    public static void updateProgress(long nread, long length, StringRedisTemplate stringRedisTemplate, String taskID){
        if (!ObjectUtils.isEmpty(stringRedisTemplate) && nread > 0 && length > 0) {
            int progress = Math.min(100, (int) Math.ceil(nread * 100 / length));
            stringRedisTemplate.opsForValue().set(GlobalConfig.progress_key_zip_file +  taskID, String.valueOf(progress), 5, TimeUnit.HOURS);
            LogUtil.info("updateProgress=" + nread + "/" + length + "，progress=" + progress);
        }else {
            int progress = 0;
            if (nread > 0 && length > 0){
                progress = Math.min(100, (int) Math.ceil(nread * 100 / length));
            }
            LogUtil.info("updateProgress=" + nread + "/" + length + "，progress=" + progress);
        }
    }

    public static void main(String[] args) {
        /*String finalFolderPath = "E:\\zip测试打包\\999\\gz2\\";
        String targetDir = "E:\\zip测试打包\\999\\";

        String fileName = RandomUtil.getuuid() + System.currentTimeMillis()
                + "_1" + ".zip";
        String finalFilePath = targetDir + fileName;
        try  {
            toZip(finalFolderPath, finalFilePath, true, null, "111", 1011);
        } catch (Exception e){

        }*/
        File zipFile = new File("E:\\zip测试打包\\999__.zip");
        org.apache.commons.compress.archivers.zip.ZipFile zip = null;
        try  {

            zip = new org.apache.commons.compress.archivers.zip.ZipFile(zipFile);
            java.util.Enumeration<? extends ZipArchiveEntry> entries = zip.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                System.out.println(entry.getName() + " : " + entry.getSize() + " bytes");
                if (!entry.isDirectory()) {
                    InputStream is = zip.getInputStream(entry);
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer, "UTF-8");
                    String content = writer.toString();
                    System.out.println(content);
                }
            }

            zip.close();
        } catch (Exception e){
            LogUtil.error(e);
        }

    }

    private static ZipParameters setZipParameters(Integer level){
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionLevel(CompressionLevel.NORMAL);
        if (!ObjectUtils.isEmpty(level)) {
            if (0 == level) {
                parameters.setCompressionLevel(CompressionLevel.NO_COMPRESSION);
            }else if (1 == level){
                parameters.setCompressionLevel(CompressionLevel.FASTEST);
            }else if (2 == level){
                parameters.setCompressionLevel(CompressionLevel.FASTER);
            }else if (3 == level){
                parameters.setCompressionLevel(CompressionLevel.FAST);
            }else if (4 == level){
                parameters.setCompressionLevel(CompressionLevel.MEDIUM_FAST);
            }else if (6 == level){
                parameters.setCompressionLevel(CompressionLevel.HIGHER);
            }else if (7 == level){
                parameters.setCompressionLevel(CompressionLevel.MAXIMUM);
            }else if (8 == level){
                parameters.setCompressionLevel(CompressionLevel.PRE_ULTRA);
            }else if (9 == level){
                parameters.setCompressionLevel(CompressionLevel.ULTRA);
            }
        }
        return parameters;
    }

    public static void toZipNew(String srcDir, String finalFilePath, StringRedisTemplate stringRedisTemplate, CheckFileDTO checkFileDTO
            , long totalLength, List<IOSourceVo> attachments, Map<Long, IOSourceVo> mainAttsMap) throws RuntimeException {
        long nread = 0L;
        String taskID = checkFileDTO.getTaskID();
        Integer level = checkFileDTO.getLevel();
        try {
            ZipParameters parameters = setZipParameters(level);
            net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(finalFilePath);

            for (IOSourceVo vo : attachments){
                if (mainAttsMap.containsKey(vo.getSourceID())){
                    if (1 == vo.getIsFolder()) {
                        zipFile.addFolder(new File(srcDir + vo.getName()));
                    }else {
                        nread = nread + vo.getSize();
                        zipFile.addFile(new File(vo.getPath()), parameters);
                        zipFile.renameFile(vo.getPath().substring(vo.getPath().lastIndexOf("/") + 1), vo.getPathDisplay() + vo.getName());
                        updateProgress(nread, totalLength, stringRedisTemplate, taskID);
                    }
                }else {
                    if (0 == vo.getIsFolder()) {

                        nread = nread + vo.getSize();
                        zipFile.addFile(new File(vo.getPath()), parameters);
                        zipFile.renameFile(vo.getPath().substring(vo.getPath().lastIndexOf("/") + 1), vo.getPathDisplay() + vo.getName());
                    }
                }
            }
        }catch (Exception e){
            LogUtil.error(e, "");
        }
    }

    public static void toZipNewList(String srcDir, String finalFilePath, StringRedisTemplate stringRedisTemplate, CheckFileDTO checkFileDTO
            , boolean isProgress, List<IOSourceVo> attachments, Map<Long, IOSourceVo> mainAttsMap
    , long totalLength) throws RuntimeException {

        String taskID = checkFileDTO.getTaskID();
        Integer level = checkFileDTO.getLevel();
        try {
            ZipParameters parameters = setZipParameters(level);

            String copyTmpFolderPath = checkFileDTO.getTemp()+"/copy/";
            //创建复制文件夹
            createDirectory( "", copyTmpFolderPath);

            List<File> addFolderList = new ArrayList<>();
            Map<String, String> fileNamesMap = new HashMap<>();
            LogUtil.info("zipProgressMonitor_getPercentDone=" + 0);
            ArrayList<File> filesToAdd = new ArrayList();
            ArrayList<String> filesToAddPathList = new ArrayList();
            int i = 0;

            net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(finalFilePath);
            for (IOSourceVo vo : attachments){
                if (mainAttsMap.containsKey(vo.getSourceID())){
                    if (1 == vo.getIsFolder()) {
                        zipFile.addFolder(new File(srcDir + vo.getName()));
                    }else {
                        setFileLists(i,copyTmpFolderPath,vo, filesToAdd, filesToAddPathList
                                ,  fileNamesMap);
                    }
                }else {
                    if (0 == vo.getIsFolder()) {
                        setFileLists(i,copyTmpFolderPath,vo, filesToAdd, filesToAddPathList
                                ,  fileNamesMap);
                    }
                }
                i++;
            }

            long progressLength = 0L ;
            if (!CollectionUtils.isEmpty(filesToAdd)){
                if (!CollectionUtils.isEmpty(filesToAdd) && filesToAdd.size() >= 1){
                    for (File f : filesToAdd) {
                        zipFile.addFile(f, parameters);
                        progressLength = progressLength + f.length();
                        double p = (progressLength / (double)totalLength) * 100;
                        if (p >= 99){
                            p = 99;
                        }
                        LogUtil.info("888888888888888888888888888 " + f.getPath()
                                +"  progressLength="+ progressLength+"  totalLength="+ totalLength+"  progress="+ p);
                        stringRedisTemplate.opsForValue().set(GlobalConfig.progress_key_zip_file + taskID, String.valueOf(p), 5, TimeUnit.HOURS);

                    }
                }
                net.lingala.zip4j.ZipFile zipFileAdd = new net.lingala.zip4j.ZipFile(finalFilePath);
                zipFileAdd.renameFiles(fileNamesMap);
            }
        }catch (Exception e){
            LogUtil.error(e, "");
        }

    }

    /** 复制是因为同个文件夹数据下有相同指向的fileId */
    private static void setFileLists(int i,String copyPath,IOSourceVo vo, ArrayList filesToAdd, ArrayList<String> filesToAddPathList
            ,  Map<String, String> fileNamesMap){
        if (!CollectionUtils.isEmpty(filesToAddPathList) && filesToAddPathList.contains(vo.getPath())){
            String fileName = i + "_" + System.currentTimeMillis() + "_" + vo.getPath().substring(vo.getPath().lastIndexOf("/") + 1) ;
            //获取原文件名称
            File oldName = new File(vo.getPath());

            //先复制文件
            File newName = new File(copyPath +  fileName);
            try {
                copyFile(oldName, newName);
                filesToAdd.add(newName);
                fileNamesMap.put(fileName, vo.getPathDisplay() + vo.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            filesToAddPathList.add(vo.getPath());
            filesToAdd.add(new File(vo.getPath()));
            fileNamesMap.put(vo.getPath().substring(vo.getPath().lastIndexOf("/") + 1), vo.getPathDisplay() + vo.getName());
        }
    }

    /**
     * @Description: 文件复制
     * @Param: resource  原文件路径
     * @Param: target    新文件路径
     * @return:
     */
    public static void copyFile(File resource, File target) throws Exception {
        // 输入流 --> 从一个目标读取数据
        // 输出流 --> 向一个目标写入数据
        long start = System.currentTimeMillis();
        // 文件输入流并进行缓冲
        FileInputStream inputStream = new FileInputStream(resource);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        // 文件输出流并进行缓冲
        FileOutputStream outputStream = new FileOutputStream(target);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        // 缓冲数组
        // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
        byte[] bytes = new byte[1024 * 2];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }
        // 刷新输出缓冲流
        bufferedOutputStream.flush();
        //关闭流
        bufferedInputStream.close();
        bufferedOutputStream.close();
        inputStream.close();
        outputStream.close();
        long end = System.currentTimeMillis();
        LogUtil.info("复制文件:" + resource.getPath() + "  成功 耗时：" + (end - start) / 1000 + " s");
    }
}
