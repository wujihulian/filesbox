package com.svnlan.home.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.domain.ScaleInfo;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

import static java.awt.Image.SCALE_SMOOTH;

/**
 * @Author:
 * @Description: 图片缩略图
 */
public class ImageUtil {

    //固定大小map
    private static final Map<String, int[][]> thumbSizeMap = new HashMap<String, int[][]>(){{
        put("fav_icon", new int[][]{{32, 32}});
        put("avatar", new int[][]{{80, 80}});
        put("design_logo", new int[][]{{420, 198}});
        put("common_pic_1", new int[][]{{560, 336}});
        put("common_pic_2", new int[][]{{560, 336}});
        put("common_pic_3", new int[][]{{266, 334}});
        // -1:0.6x, -2:0.4x, -3:0.2x, -4:0.1x
        put("ques_image", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}});
//        put("comments_attachment", new int[][]{{768, 1006}});
        put("comments_attachment", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}});
        put("image_repo", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}});
        put("image_repo_cover", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}});
        put("image", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}});
        put("cloud_4", new int[][]{{-2, -2}, {-3, -3}});
        put("cloud", new int[][]{{-6, -6}, {-7, -7}, {-8, -8}});
        put("works_image", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}, {-5, -5}});
        put("doc", new int[][]{{-2, -2}, {-3, -3}});
        put("attachment", new int[][]{{-2, -2}, {-3, -3}});
        put("course_image", new int[][]{{-1, -1}, {-2, -2},{-3, -3}, {-5, -5}});
        put("ware_image", new int[][]{{-1, -1}, {-2, -2}, {-3, -3}, {-5, -5}});
        put("homework_image_answer", new int[][]{{-2, -2}, {-3, -3}});
        put("flow_ticket", new int[][]{{-2, -2}, {-3, -3}});
        put("flow_cert", new int[][]{{-2, -2}, {-3, -3}});
        put("face_image", new int[][]{{80, 80}});
    }};
    //等比map
    private static final Map<Integer, ScaleInfo> scaleMap = new HashMap<Integer, ScaleInfo>(){{
        put(-1, new ScaleInfo(0.8 ,"large"));
        put(-2, new ScaleInfo(0.4 ,"medium"));
        put(-3, new ScaleInfo(0.2 ,"small"));
        put(-4, new ScaleInfo(0.1 ,"tiny"));
        put(-5, new ScaleInfo(0.6, "secondary"));
        put(-6, new ScaleInfo(200, 200 ,"small"));
        put(-7, new ScaleInfo(300, 300 ,"medium"));
        put(-8, new ScaleInfo(500, 500 ,"secondary"));
        put(0, new ScaleInfo(1.0, "same"));
    }};
    private static final Map<Integer, ScaleInfo> bigImageScaleMap = new HashMap<Integer, ScaleInfo>(){{
        put(-1, new ScaleInfo(0.4 ,"large"));
        put(-2, new ScaleInfo(0.2 ,"medium"));
        put(-3, new ScaleInfo(0.1 ,"small"));
        put(-4, new ScaleInfo(0.05 ,"tiny"));
    }};

    private static final String[] circleCornerTypeArr = new String[]{"course", "ware", "shop", "vip"};

    //普通流程
    public static List<String> createThumb(String filePath, String busType, String classifyType, int[][] customSizeArr,
                                           String markContent, StringRedisTemplate stringRedisTemplate, boolean needMark) {
        return createThumb(filePath, busType, classifyType, customSizeArr,  markContent, stringRedisTemplate, null, needMark);
    }
    //普通流程
    public static List<String> createThumb(String filePath, String busType, String classifyType, int[][] customSizeArr,
                                   String markContent, StringRedisTemplate stringRedisTemplate, Float quality, boolean needMark) {
        List<String> list = createThumb(filePath, busType, classifyType, customSizeArr, markContent, stringRedisTemplate,
                 null, null, true, quality, needMark);
        return list;
    }
    /**  needMark 是否需要水印 */
    private static List<String> createThumb(String filePath, String busType, String classifyType, int[][] customSizeArr
            , String markContent, StringRedisTemplate stringRedisTemplate
            , String sourceDirectoryPath, String targetDirectoryPath, boolean cover, Float quality, boolean needMark) {
        List<String> list = new ArrayList<>();
        String typeString = busType + (StringUtil.isEmpty(classifyType) ? "" : "_" + classifyType);
        int[][] sizeArr = customSizeArr == null ? thumbSizeMap.get(typeString) : customSizeArr;

        if (sizeArr == null){//默认大小
            sizeArr = new int[][]{{-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}};
        }
        String filePathForSave = String.copyValueOf(filePath.toCharArray());
        File imageFile = new File(filePath);
        if (!imageFile.exists()){
            LogUtil.error("缩略图处理失败，原图不存在" + filePath);
            return null;
        }

        //svg处理
        boolean isSvg = filePath.endsWith(".svg");
        boolean isGif = filePath.endsWith(".gif");
        boolean isWebp = filePath.endsWith(".webp");

        BufferedImage bufferedImage = null;
        try {
//            Image src = Toolkit.getDefaultToolkit().createImage(filePath);

            if (!isSvg) {
                if (isWebp){
                    ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

                    // Configure the input on the ImageReader
                    reader.setInput(new FileImageInputStream(imageFile));
                    // Decode the image
                    bufferedImage = reader.read(0);
                }else if (!isGif){
                    bufferedImage = ImageIO.read(imageFile); //会变红
                }else {
                    FileInputStream data = new FileInputStream(imageFile);
                    GifDecoder.GifImage gif = GifDecoder.read(data);
                    int frameCount = gif.getFrameCount();
                    for (int i = 0; i < frameCount; i++) {
                        bufferedImage = gif.getFrame(i);
                        if (bufferedImage != null){
                            break;
                        }
                    }
                }

            } else {
                bufferedImage = null;
            }
//                bufferedImage = toBufferedImage(src);//Image to BufferedImage
//
//            src.flush();//todo
        } catch (Exception e){
            LogUtil.error(e, "图片读取失败" + filePath);
            return null;
        }

        LogUtil.info("createThumb********* isnull=" + (null == bufferedImage));
        if (null == bufferedImage && !isSvg) {
            return null;
        }
        //云盘
        List<String> cloudList = Arrays.asList(BusTypeEnum.CLOUD.getBusType());
        boolean isCloud = cloudList.contains(busType);
        String firstPath = FileUtil.getFirstStorageDevicePath(filePath);

        filePath = filePath.replace(firstPath + "/private/", firstPath + "/common/");
        if (isCloud){
            /*filePath = filePath.replace(firstPath + "/doc/", firstPath + "/common/doc/")
                    .replace(firstPath + "/attachment/", firstPath + "/common/attachment/");*/
        }
        if (sourceDirectoryPath != null && targetDirectoryPath != null){
            filePath = filePath.replace(sourceDirectoryPath, targetDirectoryPath);
        }
        File pDir = new File(filePath).getParentFile();
        if (!pDir.exists()){
            pDir.mkdirs();
        }

        //点号位置
        int dotPosition = filePath.lastIndexOf(".");
        //大小数组循环生成新图片
        for (int[] aSizeArr : sizeArr) {
            int newWidth;
            int newHeight;
            String newPath;
            //等比
            if (aSizeArr[0] <= 0){
                 if (aSizeArr[0]  == -6 || aSizeArr[0]  == -7 || aSizeArr[0]  == -8 ){
                     int imgW = bufferedImage.getWidth();
                     int imgH = bufferedImage.getHeight();
                     int scaleW = scaleMap.get(aSizeArr[0]).getW();
                     int scaleH = scaleMap.get(aSizeArr[0]).getH();

                     if (imgH >= imgW && imgH > scaleH){
                         newHeight = scaleH;
                         double a = (double)scaleH / imgH * imgW;
                         newWidth = Integer.valueOf(String.valueOf(Math.round(a)));
                     }else if (imgW >= imgH && imgW > scaleW){
                         newWidth = scaleW;
                         double a = (double)scaleW / imgW * imgH;
                         newHeight = Integer.valueOf(String.valueOf(Math.round(a)));
                     }else {
                         newWidth = bufferedImage.getWidth();
                         newHeight = bufferedImage.getHeight();
                     }
                 }else {
                     newWidth = (int)(bufferedImage.getWidth() * scaleMap.get(aSizeArr[0]).getScale());
                     newHeight = (int)(bufferedImage.getHeight() * scaleMap.get(aSizeArr[0]).getScale());
                 }
                if (aSizeArr[0] == -1 && stringRedisTemplate != null){
                    //large的真实size存到redis, 瀑布使用
                    stringRedisTemplate.opsForHash().put(GlobalConfig.IMAGE_LARGE_SIZE_KEY, filePath, "" + newWidth + "_" + newHeight);
                }
                newPath = getNewFilePath(filePath, dotPosition, scaleMap.get(aSizeArr[0]).getTypeString());
            } else {//固定大小
                newWidth = aSizeArr[0];
                newHeight = aSizeArr[1];
                //缩略图大于原图, 不处理
//                if (newWidth > bufferedImage.getWidth() && newHeight > bufferedImage.getHeight()){
//                    continue;
//                }
                newPath = getNewFilePath(filePath, dotPosition, "" + newWidth + "_" + newHeight);
            }
            //非覆盖模式, 目标文件已存在的不处理
            if (!cover){
                if (new File(newPath).exists()){
                    continue;
                }
            }
            LogUtil.info("目标缩略图文件，{"+ newWidth +"," + newHeight +"}" + newPath);
            try {
                if (isSvg){
                    Files.copy(Paths.get(filePathForSave), Paths.get(newPath),
                            StandardCopyOption.REPLACE_EXISTING);
                   // Files.createLink(Paths.get(newPath), Paths.get(filePathForSave));
                } else {
                    //生成缩略图
                    doCreateThumb(newPath, filePathForSave, bufferedImage, newWidth, newHeight, needMark, markContent, quality);
                    LogUtil.info("目标缩略图文件 压缩成功，{"+ newWidth +"," + newHeight +"}" + newPath);
                }
                list.add(newPath);
            } catch (Exception e) {
                LogUtil.error(e, "缩略图生成失败,目标缩略图文件{"+ newWidth +"," + newHeight +"}" + newPath);
            }
        }
        //生成水印
        /*if (customSizeArr == null) {
            try {
                String markedPath = getNewFilePath(filePath, dotPosition , "marked");
                doCreateThumb(markedPath, filePathForSave, bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight(),
                        needMark, markContent, null);
                list.add(markedPath);
            } catch (Exception e){
                LogUtil.error(e, "水印图生成失败");
            }
        }*/
        return list;
    }

    public static boolean createPngThumb(String path, int[] sizeArr, String targetPath) throws Exception{
        File imgFile = new File(path);
        if (!imgFile.exists()){
            LogUtil.error("原图不存在, " + path);
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(imgFile);
        if (bufferedImage == null){
            return false;
        }
        if (bufferedImage.getWidth() < sizeArr[0] && bufferedImage.getHeight() < sizeArr[1]){
            FileUtils.copyFile(imgFile, new File(targetPath));
            return false;
        }
        Image scaleImage = bufferedImage.getScaledInstance(sizeArr[0], sizeArr[1], SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(sizeArr[0], sizeArr[1], BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(scaleImage, 0, 0, sizeArr[0], sizeArr[1], null);
        g.dispose();
        File targetFile = new File(targetPath);
        if (!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        return ImageIO.write(newImage, "png", targetFile);
    }

    /**
     * @Description: 新文件路径
     * @params:  [filePath, dotPosition, typeString]
     * @Return:  java.lang.String
     * @Modified:
     */
    private static String getNewFilePath(String filePath, int dotPosition, String typeString) {
        return filePath.substring(0, dotPosition)
                + "!" + typeString
                + filePath.substring(dotPosition);
    }

    private static BufferedImage toBufferedImage(Image image) throws Exception {

        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;

    }

    /**
     * @Description: 创建缩略图
     * @params:  [newPath, sourcePath, bufferedImage, width, height, needMark, psName, isImageRepo, quality]
     * @Return:  void
     * @Modified:
     */
    public static void doCreateThumb(String newPath, String sourcePath, BufferedImage bufferedImage, int width, int height,
                                      boolean needMark, String psName,Float quality) throws IOException {
        //判断宽度是否过小
        if (null != bufferedImage && bufferedImage.getWidth() < 100) {
            //硬链拷贝
            //Files.createLink(Paths.get(newPath), Paths.get(sourcePath));
            Files.copy(Paths.get(sourcePath), Paths.get(newPath),
                    StandardCopyOption.REPLACE_EXISTING);
            return;
        }

        String fileType = null;
        boolean isPng = isPng(sourcePath);
        boolean isWebp = isWebp(sourcePath);
        int imageType = BufferedImage.TYPE_INT_RGB;
        if (isPng){
            fileType = "png";
            imageType = BufferedImage.TYPE_INT_ARGB;
        }else if (isWebp){
            fileType = "webp";
            imageType = ThumbnailParameter.DEFAULT_IMAGE_TYPE;
        }else {
            fileType = "jpg";
        }

        //新建一个image, 把来源image画上
        BufferedImage newImage = new BufferedImage(width, height, imageType);
        Image scaleImage = bufferedImage.getScaledInstance(width, height, SCALE_SMOOTH);
//        Image temp = bufferedImage.getScaledInstance(width, height, SCALE_SMOOTH);
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(scaleImage, 0, 0, width, height, null);
        if (needMark && !StringUtil.isEmpty(psName) ) {//其他水印
            addMark(g, width, height, psName);
        }
        g.dispose();

        //输出图片
        ImageIO.write(newImage, fileType, new File(newPath));
        quality = 0.6f;
        //若要对图片进行质量压缩
        if(null != quality) {
            try {
                //
                Thumbnails.of(newPath).scale(1f).outputQuality(quality).toFile(newPath);
            } catch (Exception e) {
                LogUtil.error(e, "压缩图片失败，路径：" + newPath);
            }
        }
    }

    /**
     * @description 判断是否PNG
     * @params [picPath]
     * @return boolean
     */
    private static boolean isPng(String picPath) {
        byte[] bytes = new byte[5];
        try (FileInputStream fileInputStream = new FileInputStream(picPath)) {
            int read = fileInputStream.read(bytes);
            if (read < 4) {
                return false;
            }
            if (bytes[1] == (byte) 'P' && bytes[2] == (byte) 'N' && bytes[3] == (byte) 'G') {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }
    /**
     * @description 判断是否webp
     * @params [picPath]
     * @return boolean
     */
    private static boolean isWebp(String picPath) {
        if (picPath.toLowerCase().endsWith(".webp")){
            return true;
        }
        return false;
    }

    /**
     * @Description: 创建供分享图使用的缩略图
     * @params:  [sourcePath, newPath]
     * @Return:  void
     * @Modified:
     */
    public static BufferedImage createThumbForShare(String sourcePath, String newPath){

        File sourceFile = new File(sourcePath);
        File newFile = new File(newPath);
        //原图不存在
        if (!sourceFile.exists()){
            return null;
        }
        try {
            //目标已存在, 直接返回
            if (newFile.exists()){
                return ImageIO.read(newFile);
            }
            BufferedImage bufferedImage = ImageIO.read(sourceFile);
            return createThumbForShareByImage(bufferedImage, newFile);
        } catch (Exception e){
            LogUtil.error(e, "生成供分享使用的缩略图失败, sourcePath:" + sourcePath + ", newPath:" + newPath);
        }
        return null;
    }

    /**
       * @Description: 分享图缩略图
       * @params:  [bufferedImage, newFile]
       * @Return:  java.awt.image.BufferedImage
       * @Modified:
       */
    private static BufferedImage createThumbForShareByImage(BufferedImage bufferedImage, File newFile) throws Exception {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double scale;
        if (width < 1000){
            return bufferedImage;
        } else if (width <= 2000){
            scale = 0.6;
        } else if (width <= 3000){
            scale = 0.5;
        } else if (width <= 5000){
            scale = 0.4;
        } else {
            scale = 0.3;
        }
        if (newFile.exists()){
            return ImageIO.read(newFile);
        }
        width = (int) (width * scale);
        height = (int) (height * scale);
        Image scaleImage = bufferedImage.getScaledInstance(width , height, SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(scaleImage, 0, 0, width, height, null);
        g.dispose();
        ImageIO.write(newImage, "jpg", newFile);
        return newImage;
    }

    /**
       * @Description: 图库水印
       * @params:  [g, width, height, psName]
       * @Return:  void
       * @Modified:
       */
    private static void addImageRepoMark(Graphics2D g, int width, int height, String psName) {
        //字体缩放比例
        float fontScale = 24;
        float fontSizePre = height / fontScale;
        float fontSize = fontSizePre * 2;
        Font font = new Font("黑体", Font.BOLD, 30);
        font = font.deriveFont(Font.BOLD, fontSize);
        g.setFont(font);
        //字实际长度
        int length = getWatermarkLength(psName, g);
        boolean overWidth = false;
        //字长超过图片宽度
        if (length + fontSize >= width){
            overWidth = true;
            //重设字体大小
            fontSize = fontSize / ((1.0f * length + fontSize) / width) * 0.9f;
            font = font.deriveFont(Font.BOLD, fontSize);
            g.setFont(font);
            length = getWatermarkLength(psName, g);
        }
        int backgroundHeight = (int) (fontSize * 1.5f);
        int backgroundWidth = (length + (int)fontSize) > width ? width : (length + (int)fontSize) ;

        BufferedImage markBackground = new BufferedImage(backgroundWidth, backgroundHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = markBackground.createGraphics();
        //画透明区域
        bg.setColor(new Color(102, 102, 102, 90));
        bg.fillRect(0,0,width,backgroundHeight);
        //画字
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bg.setFont(font);
        bg.setColor(new Color(255, 255, 255, 150));
        bg.drawString(psName, fontSize / 2 , fontSize);
        bg.dispose();
        //水印区域垂直 在图片下半部分的居中位置
        int backgroundY = (int) (height * 0.75 - backgroundHeight / 2);

        g.drawImage(markBackground, width - backgroundWidth, backgroundY, backgroundWidth , backgroundHeight, null);
    }

    private static void addMark(Graphics2D g, int width, int height, String content) {
        addMark(g, width, height, content, false);
    }

    /**
       * @Description: 加水印
       * @params:  [g, width, height, content, second]
       * @Return:  void
       * @Modified:
       */
    private static void addMark(Graphics2D g, int width, int height, String content, boolean second) {
        g.setColor(new Color(second ? 0xFFFFFF : 0));
        g.setBackground(Color.GREEN);
        if (width <= 100){//小于80的不加水印
            return;
        }
        int fontSize;
        int cLength = content.length();
        double markScale = (double) 250 * 2 / cLength;

        if (width * 2  / cLength > 14){
            fontSize = (int) (width / markScale);
//            fontSize = 14;
        } else {
            fontSize = width * 2 / cLength;
        }
        Font font = new Font("宋体", Font.PLAIN, fontSize);
        g.setFont(font);
        int offset;
        if (width <= 200) {
            offset = second ? 0 : 1;
        } else if (width <= 500){ // 201 - 499, 2 - 8
            int b = ((width - 100) / 100 ) * 2;
            offset = second ? b - 1 : b;
        } else if (width <= 1100){ // 501 - 1100, 12 - 30
            int b = ((width - 100) / 100 ) * 3;
            offset = second ? b - 1 : b;
        } else {
            offset = second ? 29: 30;
        }
        int x = width - getWatermarkLength(content, g) - offset;
        int y = height - offset;
        if (y < (double)height / 1.5){
            y = height;
        }
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
//                0.5f));
        g.drawString(content, x, y); // 添加水印的文字和设置水印文字出现的内容
        if (!second) {
            addMark(g, width, height, content, true);
        }
    }

    /**
     * @Description: 获取字占用长度
     * @params:  [content, g]
     * @Return:  int
     * @Modified:
     */
    private static int getWatermarkLength(String content, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(content.toCharArray(), 0, content.length());
    }

    private static int getCharLen(char c, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }

    /**
     * @Description: 十六进制获取颜色, 默认黑
     * @params:  [color]
     * @Return:  java.awt.Color
     * @Modified:
     */
    private static Color getColorByHexString(String color) {
        if (StringUtil.isEmpty(color)){
            return Color.BLACK;
        }
        int colorInt;
        try {
            colorInt = Integer.valueOf(color, 16);
        } catch (Exception e){
            return Color.BLACK;
        }
        return new Color(colorInt);
    }



    /**
     * 计算旋转参数
     */
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree,we need to do some conversion.
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_data_width = Math.atan((double) src.height / src.width);
        double angel_data_height = Math.atan((double) src.width / src.height);

        int len_data_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_data_width));
        int len_data_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_data_height));
        int des_width = src.width + len_data_width * 2;
        int des_height = src.height + len_data_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }


    /**
       * @Description: 获取图片分辨率
       * @params:  [path]
       * @Return:  java.lang.String
       * @Modified:
       */
    public static String getResolution(String path) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(path));
        } catch (Exception e) {
            LogUtil.error( "读取图片失败" + path + ", " + e.getMessage());
            return "0*0";
        }
        if (bufferedImage == null){
            return "0*0";
        }
        return bufferedImage.getWidth() + "*" + bufferedImage.getHeight();
    }

    /**
     * 获取图片正确显示需要旋转的角度（顺时针）
     * @return
     */
    public static String RotateImg(String filePath,String newFilePath,boolean delete){
        try {
            File file = new File(filePath);
            //测试发现文件大于7Mb以上时会出现读取速率很慢，找时间再改改；
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
            int orientation=0;
            // Exif信息中有保存方向,把信息复制到缩略图
            // 原图片的方向信息
            if(directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)){
                orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
                LogUtil.info("查看是否需要旋转filePath=" + filePath + " , orientation=" + orientation);
            }
            int angle=0;
            if(6 == orientation ){
                //6旋转90
                angle = 90;
            }else if( 3 == orientation){
                //3旋转180
                angle = 180;
            }else if( 8 == orientation){
                //8旋转90
                angle = 270;
            }
            if (angle > 0){
                BufferedImage src = ImageIO.read(file);
                BufferedImage des = Rotate(src, angle);
                String filename = file.getName();
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                ImageIO.write(des,ext, new File(newFilePath));
                if (delete){
                    try {
                        file.delete();
                    }catch (Exception e){
                    }
                }
                return newFilePath;
            }
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return filePath;
    }

    public static BufferedImage Rotate(Image src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

}
