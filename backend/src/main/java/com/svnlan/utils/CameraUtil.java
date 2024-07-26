package com.svnlan.utils;

import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFileMeta;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.vo.FileMetaVo;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/25 9:44
 */
@Component
public class CameraUtil {

    public static boolean cameraToImg(String prefix, String upFilePath) {
        // 创建一个List集合来保存转换文件的命令
        List<String> convert = new ArrayList<>();
        convert.add("simple_dcraw"); // 添加转换工具路径
        convert.add("-e"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(upFilePath); // 添加要转换格式的文件的路径
        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        try {
            StringBuilder sb = new StringBuilder();
            for (String a : convert){
                sb.append(a);
                sb.append(" ");
            }
            String cmdStr = sb.toString();
            LogUtil.info(prefix + "cameraToImg cmd: " + cmdStr);
            builder.command(convert);
            Process process =builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            in.close();
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error(prefix + "转Img异常, " + errorString + cmdStr);
            }
        } catch (Exception e) {
            mark = false;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error(prefix + "关闭流失败");
                }
            }
        }
        return mark;
    }


    public static String cameraToImgPath(String filePath, String suffix ) {
        String path = "";
        switch (suffix){
            case "arw":
            case "mrw":
            case "erf":
            case "raf":
            case "cr2":
            case "nrw":
            case "nef":
            case "orf":
            case "rw2":
            case "pef":
            case "srf":
                path = filePath + ".thumb.jpg";
                break;
            case "dcr":
            case "kdc":
            case "dng":
                path = filePath + ".thumb.ppm";
                break;
            case "":
                path = filePath + ".thumb.jpg";
                break;
                default:
                    break;
        }
        return path;
    }

    public static void finishCameraToImg(String prefix, CommonSource commonSource, String sourceImgPath, Long thumbSize, IoSourceDao ioSourceDao, IoFileDao ioFileDao) {

        if (thumbSize > 0){
            commonSource.setThumbSize(thumbSize);
            try {
                ioSourceDao.updateSourceThumbSize(commonSource.getSourceID(), thumbSize);
            }catch (Exception e){
                LogUtil.error(e, "finishCameraToImg thumbSize error");
            }
        }

        try {
            //更新文件表
            commonSource.setThumbSize(thumbSize);
            commonSource.setIsH264Preview(1);
            //
            ioFileDao.updateCameraConvert(commonSource);
            //特定业务的处理

            updateCameraValue(Arrays.asList(commonSource.getFileID()), sourceImgPath, ioFileDao);

        } catch (Exception e){
            LogUtil.error(e, prefix + "转码入库失败, commonSource:"+ JsonUtils.beanToJson(commonSource)
                    +  "sourceImgPath:"+ sourceImgPath);
        }
    }

    public static void updateCameraValue(List<Long> fileIDList, String sourceThumbPath, IoFileDao ioFileDao){
        if (CollectionUtils.isEmpty(fileIDList) || ObjectUtils.isEmpty(sourceThumbPath)){
            return;
        }
        File f = new File(sourceThumbPath);
        if (!f.exists()){
            LogUtil.error("updateCameraValue sourceThumbPath file not exist");
            return;
        }

        List<IOFileMeta> list = ioFileDao.getFileUrlValueList(fileIDList);
        if (!CollectionUtils.isEmpty(list)){
            try {
                FileMetaVo fileMetaVo = null;
                for (IOFileMeta meta : list) {
                    if (ObjectUtils.isEmpty(meta.getValue())){
                        fileMetaVo = new FileMetaVo();
                    }else {
                        fileMetaVo = JsonUtils.jsonToBean(meta.getValue(), FileMetaVo.class);
                    }
                    if (!ObjectUtils.isEmpty(sourceThumbPath)) {
                        fileMetaVo.setThumb(sourceThumbPath);
                    }
                    meta.setValue(JsonUtils.beanToJson(fileMetaVo));
                }
                ioFileDao.updateFileUrlValue(list);
            }catch (Exception e){
                LogUtil.error(e, "updateCameraValue fileMeta error " + JsonUtils.beanToJson(list));
            }
        }
    }

    public static  boolean ppmConvertToJpg(String filePath, String toPath){

        boolean writeSuccess = false;
        try {
            String outFormat = "%-17s: %s%n";

            LogUtil.info(outFormat +  "supported formats" +  Arrays.toString(ImageIO.getWriterFormatNames()));

            Path inputFile = Paths.get(filePath);
            LogUtil.info(outFormat +  "input file" + inputFile.toAbsolutePath());
            InputStream is = Files.newInputStream(inputFile, StandardOpenOption.READ);
            BufferedImage image = ImageIO.read(is);

            File outputFile = Paths.get(toPath).toAbsolutePath().toFile();
            LogUtil.info(outFormat, "output file", outputFile.getAbsolutePath());

            writeSuccess = ImageIO.write(image, "JPEG", outputFile);
            LogUtil.info(outFormat + "write successful" +  writeSuccess);
        }catch (Exception e){
            LogUtil.error(e);
        }

        return writeSuccess;
    }
}
