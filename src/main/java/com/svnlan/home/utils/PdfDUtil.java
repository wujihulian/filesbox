package com.svnlan.home.utils;

import com.aspose.pdf.Document;
import com.aspose.pdf.ImageType;
import com.aspose.pdf.License;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.facades.PdfConverter;
import com.svnlan.common.GlobalConfig;
import com.svnlan.utils.LogUtil;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: PDF转换工具类
 */
public class PdfDUtil {

    /**
     * @description: 将PDF转PNG或JPG（有压缩）
     * @param prefix
     * @param sourcePdfPath 要转的源PDF路径
     * @param targetFilePath 目标的PNG文件前缀路径
     * @param accuracy 压缩质量：0~1
     * @param maxWidth
     * @param maxHeight
     * @return java.lang.String 最后一张图片地址
     */

    public static String pdfToPngOrJpg(String prefix, String sourcePdfPath, String targetFilePath, double accuracy,
                                       int maxWidth, int maxHeight) {
        return pdfToPngOrJpg(prefix, sourcePdfPath, targetFilePath, accuracy, maxWidth, maxHeight, false, null);
    }
    public static String pdfToPngOrJpg(String prefix, String sourcePdfPath, String targetFilePath, double accuracy,
                                       int maxWidth, int maxHeight, boolean isReturnList, List<String> pathList) {
        String lastImagePath = null;
        File file = new File(sourcePdfPath);

        PDDocument doc = null;
        try {
            doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                PDPage pdPage = doc.getPage(i);
                PDRectangle mediaBox = pdPage.getMediaBox();
                Double dpi = mediaBox.getWidth() * 25.4 / 72;
                BufferedImage image = renderer.renderImageWithDPI(i, dpi.floatValue());
                String pngImagePath = targetFilePath + (i+1) + ".png";
                lastImagePath = pngImagePath;

                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();

                OutputStream out = null;
                try {
                    out = new FileOutputStream(new File(lastImagePath));
                    ImageIO.write(image, "png", out);
                    if (isReturnList && accuracy > 1){
                        pathList.add(lastImagePath);
                    }
                } catch (Exception e) {
                    LogUtil.error(e, prefix);
                } finally {
                    if(null != out) {
                        out.close();
                    }
                }

                //若质量小于1，则进行压缩成JPG图片
                if(accuracy < 1 || maxWidth > 0 || maxHeight > 0) {
                    float widthScale = (float) maxWidth / imageWidth;
                    float heightScale = (float) maxHeight / imageHeight;
                    float scale = 1;
                    if(widthScale < 1 || heightScale < 1) {
                        if(widthScale < heightScale) {
                            scale = widthScale;
                        } else {
                            scale = heightScale;
                        }
                    }

                    String targetJpgPath = targetFilePath + (i+1) + ".jpg";

                    //压缩
                    Boolean success = ImageDUtil.compressImage(prefix, lastImagePath, targetJpgPath, accuracy, scale);
                    if(success) {
                        lastImagePath = targetJpgPath;
                        if (isReturnList){
                            pathList.add(targetJpgPath);
                        }
                        //删除源文件
                        FileUtils.deleteQuietly(new File(pngImagePath));
                    } else {
                        lastImagePath = null;
                    }
                }
            }
        } catch (IOException e) {
            LogUtil.error(e, prefix + "pdf转png失败，源文件：" + sourcePdfPath + "，目标文件：" + targetFilePath + "。");
            lastImagePath = null;
        } finally {
            if(null != doc) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lastImagePath;
    }

    public static String pdfToPngOrJpg(String prefix, String sourcePdfPath, String targetFilePath,
                                       boolean isReturnList, List<String> pathList, String redisKey, Boolean isProgress,
                                       String suffix, StringRedisTemplate stringRedisTemplate, int progressType) {
        String lastImagePath = null;
        File file = new File(sourcePdfPath);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                PDPage pdPage = doc.getPage(i);
                PDRectangle mediaBox = pdPage.getMediaBox();
                Double dpi = mediaBox.getWidth() * 25.4 / 72;
                BufferedImage image = renderer.renderImageWithDPI(i, dpi.floatValue());
                String pngImagePath = targetFilePath + (i+1) + "." + suffix;
                lastImagePath = pngImagePath;
                 OutputStream out = null;
                try {
                    out = new FileOutputStream(new File(lastImagePath));
                    ImageIO.write(image, suffix, out);
                    if (isReturnList){
                        pathList.add(lastImagePath);
                    }
                } catch (Exception e) {
                    LogUtil.error(e, prefix);
                } finally {
                    if(null != out) {
                        out.close();
                    }
                }
                if (isProgress){
                    String p = PdfDUtil.calculateProgress(i, pageCount, 1);
                    double r = Double.valueOf(p) / 2;
                    if (progressType == 2){
                        double r1 = r / 2;
                        stringRedisTemplate.opsForValue().set(redisKey, String.format("%.1f",(50 + r1)), 60, TimeUnit.SECONDS);
                    }else {
                        stringRedisTemplate.opsForValue().set(redisKey, p, 60, TimeUnit.SECONDS);
                    }
                }

            }
        } catch (IOException e) {
            LogUtil.error(e, prefix + "pdf转图片失败，源文件：" + sourcePdfPath + "，目标文件：" + targetFilePath + "。");
            lastImagePath = null;
        } finally {
            if(null != doc) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lastImagePath;
    }

    public static void pdf2Doc(String prefix, String sourcePdfPath, String targetFilePath, boolean isDocX, String redisKey, Boolean isProgress,
                               StringRedisTemplate stringRedisTemplate){
        File pdfFile = new File(targetFilePath);
        try {
            long old = System.currentTimeMillis();
            Document document = new Document(sourcePdfPath);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            document.save(outputStream, isDocX ? SaveFormat.DocX : SaveFormat.Doc);
            outputStream.close();
            long now = System.currentTimeMillis();
            if (isProgress){
                stringRedisTemplate.opsForValue().set(redisKey, 100+"", 60, TimeUnit.SECONDS);
            }
            System.out.println("PDF转化WORD共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            LogUtil.error(e, prefix + " pdf2Doc 转化失败");
        }
    }

    public static String calculateProgress(Integer num, Integer total, int dis){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(dis); //保留几位小数填写几
        //注意需要将Integer转换为float进行计算
        String percent = numberFormat.format((float) num / (float) total * 100);
        return percent;
    }

    public static String pdfToPngOrJpgConverter(String prefix, String sourcePdfPath, String targetFilePath,
                                       boolean isReturnList, List<String> pathList, String redisKey, Boolean isProgress,
                                       String suffix, StringRedisTemplate stringRedisTemplate, int progressType) {

        PdfConverter converter = new PdfConverter();
        converter.bindPdf(sourcePdfPath);
        String lastImagePath = null;
        try {
            converter.doConvert();

            int pageCount = converter.getPageCount();
            int i=0;
            while (converter.hasNextImage()) {
                converter.getNextImage(targetFilePath + (i+1) + "." + suffix,  "png".equals(suffix) ? ImageType.getPng(): ImageType.getJpeg());
                lastImagePath = targetFilePath + (i+1) + "." + suffix;
                LogUtil.info("convertImg i=" + i +" lastImagePath=" + lastImagePath);
                if (isReturnList) {
                    pathList.add(lastImagePath);
                }
                if (isProgress){
                    String p = PdfDUtil.calculateProgress(i, pageCount, 1);
                    double r = Double.valueOf(p) / 2;
                    if (progressType == 2){
                        double r1 = r / 2;
                        stringRedisTemplate.opsForValue().set(redisKey, String.format("%.1f",(50 + r1)), 60, TimeUnit.SECONDS);
                    }else {
                        stringRedisTemplate.opsForValue().set(redisKey, p, 60, TimeUnit.SECONDS);
                    }
                }
                i ++;
            }

        }catch (Exception e){
            LogUtil.error(e, prefix + "pdf转图片失败，源文件：" + sourcePdfPath + "，目标文件：" + targetFilePath + "。");
            lastImagePath = null;
        }finally {
            if(null != converter) {
                try {
                    converter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return lastImagePath;
    }

}
