package com.svnlan.home.utils;

import com.svnlan.common.Result;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import com.svnlan.utils.StringUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @description: 图片工具类
 */
public class ImageDUtil {

    /**
     * @description: wmf转png图片(使用libreoffice命令)
     * @param prefix
     * @param sourceFilePath
     * @param targetDir
     * @return java.lang.Boolean
     */
    public static Boolean wmfToPng(String prefix, String sourceFilePath, String targetDir) {
        prefix += String.format("wmf转png(wmfToPng:%s)>>>", RandomUtil.getuuid());
        Boolean success = Boolean.TRUE;

        List<String> convert = new ArrayList<>();
        convert.add("libreoffice");
        convert.add("--headless");
        convert.add("--convert-to");
        convert.add("png");
        convert.add(sourceFilePath);
        convert.add("--outdir");
        convert.add(targetDir);

        ProcessBuilder builder = new ProcessBuilder();
        InputStream in = null;
        try {
            StringBuilder sb = new StringBuilder();
            for (String a : convert){
                sb.append(a);
                sb.append(" ");
            }
            LogUtil.info(prefix + "转换命令: " + sb.toString());
            builder.command(convert);
            Process process =builder.start();
            in = process.getErrorStream();
            String errorString = IOUtils.toString(in, "UTF-8");
            in.close();
            if (!StringUtil.isEmpty(errorString)){
                LogUtil.error(prefix + "转换返回错误信息" + errorString);
            }
        } catch (Exception e) {
            LogUtil.error(e, prefix + "转换异常返回信息");
            success = false;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e){
                    LogUtil.error(prefix + "关闭流失败");
                }
            }
        }
        return success;
    }

    /**
     * @description: 将PNG图片背景色变成透明
     * @param prefix
     * @param imgSrc
     * @return java.awt.image.BufferedImage
     */
    public static BufferedImage transferAlpha2BufferedImage(String prefix, String imgSrc) {
        File file = new File(imgSrc);
        InputStream is = null;
        BufferedImage bufferedImage = null;
        prefix += String.format("将PNG图片背景色变成透明(transferAlpha2BufferedImage:%s)>>>", RandomUtil.getuuid());
        try {
            is = new FileInputStream(file);
            // 如果是MultipartFile类型，那么自身也有转换成流的方法：is = file.getInputStream();
            BufferedImage bi = ImageIO.read(is);
            Image image = (Image) bi;
            ImageIcon imageIcon = new ImageIcon(image);
            bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
        } catch (Exception e) {
            LogUtil.error(e,prefix + "发生异常");
            bufferedImage = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //不用做什么
                }
            }
        }
        return bufferedImage;
    }

    /**
     * @description: 将Image图像中的透明/不透明部分转换为Shape图形
     * @param img 图片信息
     * @param transparent 是否透明
     * @return java.awt.Shape
     */
    public static Shape getImageShape(Image img, boolean transparent) throws InterruptedException {
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        // 首先获取图像所有的像素信息
        PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
        pgr.grabPixels();
        int pixels[] = (int[]) pgr.getPixels();

        // 循环像素
        for (int i = 0; i < pixels.length; i++) {
            // 筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中
            int alpha = (pixels[i] >> 24) & 0xff;
            if (alpha == 0) {
                continue;
            } else {
                x.add(i % width > 0 ? i % width - 1 : 0);
                y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
            }
        }

        // 建立图像矩阵并初始化(0为透明,1为不透明)
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }

        // 导入坐标ArrayList中的不透明坐标信息
        for (int c = 0; c < x.size(); c++) {
            matrix[y.get(c)][x.get(c)] = 1;
        }

        /*
         * 逐一水平"扫描"图像矩阵的每一行，将透明（这里也可以取不透明的）的像素生成为Rectangle，
         * 再将每一行的Rectangle通过Area类的rec对象进行合并， 最后形成一个完整的Shape图形
         */
        Area rec = new Area();
        int temp = 0;
        //生成Shape时是1取透明区域还是取非透明区域的flag
        int flag = transparent ? 0 : 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix[i][j] == flag) {
                    if (temp == 0)
                        temp = j;
                    else if (j == width) {
                        if (temp == 0) {
                            Rectangle rectemp = new Rectangle(j, i, 1, 1);
                            rec.add(new Area(rectemp));
                        } else {
                            Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
                            rec.add(new Area(rectemp));
                            temp = 0;
                        }
                    }
                } else {
                    if (temp != 0) {
                        Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
                        rec.add(new Area(rectemp));
                        temp = 0;
                    }
                }
            }
            temp = 0;
        }
        return rec;
    }

    /**
     * @description: 图片裁切处理
     * @param prefix
     * @param bufferedImage
     * @param x
     * @param y
     * @param width
     * @param height
     * @return java.awt.image.BufferedImage
     */
    public static BufferedImage cutImage(String prefix, BufferedImage bufferedImage, int x,
                              int y, int width, int height) {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
        ImageReader reader = readers.next();
        ImageInputStream imageInputStream = null;
        BufferedImage bi = null;
        prefix += String.format("裁切图片（cutImage:%s）>>>", RandomUtil.getuuid());
        try {
            //将BufferedImage转换为ImageInputStream
            imageInputStream = bufferedImageToInputStream(bufferedImage);
            reader.setInput(imageInputStream, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            bi = reader.read(0, param);
        } catch (Exception e) {
            LogUtil.error(e,prefix + "发生异常");
        } finally {
            if(null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    //不用做什么
                }
            }
        }
        return bi;
    }

    /**
     * @description: 将BufferedImage转换为ImageInputStream
     * @param image
     * @return javax.imageio.stream.ImageInputStream
     */
    public static ImageInputStream bufferedImageToInputStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(os.toByteArray()));
        return input;
    }

    /**
     * @description: 抽取png图片的非透明内容区域裁切出来
     * @param prefix
     * @param imgSrc
     * @param imgTarget
     * @return com.svnlan.homework.base.Result
     */
    public static Result cutPngContent(String prefix, String imgSrc, String imgTarget) {
        String code = "200";
        String message = "";
        Boolean success = Boolean.TRUE;

        //检测文件是否生成
        File file = new File(imgSrc);
        int retryCount = 10;
        int retryIndex = 0;
        while (retryIndex < retryCount) {
            if (!file.exists()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
            retryIndex++;
        }

        //将PNG图片背景色变成透明
        BufferedImage bufferedImage = ImageDUtil.transferAlpha2BufferedImage(prefix, imgSrc);
        if(null == bufferedImage) {
            code = "TransferAlphaFail";
            message = "将PNG图片背景色变成透明失败";
        }

        //得PNG图片非透明的区域
        Shape shape = null;
        if("200".equals(code)) {
            try {
                shape = ImageDUtil.getImageShape(bufferedImage, false);
            } catch (Exception e) {
                code = "GetImageShapeFail";
                message = "得PNG图片非透明的区域(getImageShape)发生异常";
                LogUtil.error(e, prefix + message);
            }
        }

        //图片裁切最后写入
        if("200".equals(code)) {
            try {
                double shapeW = shape.getBounds().getWidth();
                double shapeH = shape.getBounds().getHeight();
                int width = (int) Math.ceil(shapeW);
                int height = (int) Math.ceil(shapeH);
                int x = (int) Math.ceil(shape.getBounds().getX());
                int y = (int) Math.ceil(shape.getBounds().getY());
                //图片裁切处理
                BufferedImage bi = ImageDUtil.cutImage(prefix, bufferedImage, x, y, width, height);
                if(null == bi) {
                    code = "CutImageFail";
                    message = "图片裁切处理发生异常";
                }

                if("200".equals(code)) {
                    //来源与目标是同一个，则先删除源文件
                    if(imgSrc.equals(imgTarget)) {
                        File sourceFile = new File(imgSrc);
                        if (sourceFile.exists()) {
                            sourceFile.delete();
                        }
                    }

                    //将BufferedImage写入目标图片
                    ImageIO.write(bi, "png", new FileOutputStream(imgTarget));
                }
            } catch (Exception e) {
                code = "WriteImageFail";
                message = "写入目标文件发生异常";
                LogUtil.error(e, prefix + "写入目标文件发生异常");
            }
        }
        if(!"200".equals(code)) {
            success = Boolean.FALSE;
        }
        Result result = new Result(code, null);
        return result;
    }

    /**
     * @description: 压缩图片
     * @param prefix
     * @param sourcePath
     * @param targetPath
     * @param accuracy
     * @param scale
     * @return java.lang.Boolean
     */
    public static Boolean compressImage(String prefix, String sourcePath, String targetPath, double accuracy, double scale) {
        Boolean success = Boolean.TRUE;
        byte[] imageBytes = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            Thumbnails.of(sourcePath).scale(scale).outputQuality(accuracy).outputFormat("jpg").toOutputStream(out);
            imageBytes = out.toByteArray();

            //字节数组转存文件
            FileUtil.bytesToFile(prefix, imageBytes, targetPath);
        } catch (IOException e) {
            LogUtil.error(e,prefix + "按质量压缩图片失败，源文件：" + sourcePath + "，目标文件：" + targetPath);
            success = Boolean.FALSE;
        } finally {
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

}
