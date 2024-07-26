package com.svnlan.jwt.service.impl;


import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.dto.ImageVerificationDto;
import com.svnlan.jwt.service.CaptchaService;
import com.svnlan.jwt.tool.CaptchaCacheTool;
import com.svnlan.jwt.tool.ImageVerificationUtil;
import com.svnlan.jwt.vo.ImageRead;
import com.svnlan.jwt.vo.ImageVerificationVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 验证码业务实现类
 * -------------------
 */

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    CaptchaCacheTool captchaCacheTool;
    /**
     * 源图路径前缀
     */
    @Value("${slide-verification-code.path.origin-image:classpath:static/targets}")
    private String verificationImagePathPrefix;

    /**
     * 模板图路径前缀
     */
    @Value("${slide-verification-code.path.template-image:classpath:static/templates}")
    private String templateImagePathPrefix;




    /**
     * 获取request对象
     *
     * @return 返回request对象
     */
    protected static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取response对象
     *
     * @return 返回response对象
     */
    protected static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }


    /**
     * 根据类型获取验证码
     *
     * @param imageVerificationDto 用户信息
     * @return 图片验证码
     */
    @Override
    public ImageVerificationVo selectImageVerificationCode(ImageVerificationDto imageVerificationDto, HttpServletResponse response) {

        ImageVerificationVo imageVerificationVo = null;
        String type = null;

        try {
            imageVerificationVo = selectSlideVerificationCode(imageVerificationDto, response);

        } catch (Exception e) {
            LogUtil.error(e, e.getMessage());
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        return imageVerificationVo;
    }


    /**
     * 获取滑动验证码
     *
     * @param imageVerificationDto 验证码参数
     * @return 滑动验证码
     */
    public ImageVerificationVo selectSlideVerificationCode(ImageVerificationDto imageVerificationDto, HttpServletResponse response) {


        ImageVerificationVo imageVerificationVo = null;
        try {


            //  随机取得原图文件夹中一张图片
            ImageRead originImageRead = readTargetImage();
            //  获取模板图片文件
            ImageRead templateImageRead = readTemplateImage(templateImagePathPrefix.concat("/template.png"));
            //  获取描边图片文件
            ImageRead borderImageRead = readBorderImageFile(templateImagePathPrefix.concat("/border.png"));

            //  获取原图文件类型
            String originImageFileType = originImageRead.getFileExtension();
            //  获取模板图文件类型
            String templateImageFileType = templateImageRead.getFileExtension();
            //  获取边框图文件类型
            String borderImageFileType = borderImageRead.getFileExtension();

            //  读取原图
            BufferedImage verificationImage = originImageRead.getImage();
            //  读取模板图
            BufferedImage readTemplateImage = templateImageRead.getImage();

            //  读取描边图片
            BufferedImage borderImage = borderImageRead.getImage();


            //  获取原图感兴趣区域坐标
            imageVerificationVo = ImageVerificationUtil.generateCutoutCoordinates(verificationImage, readTemplateImage);

            int y = imageVerificationVo.getY();
            //  在分布式应用中，可将session改为redis存储
            //getRequest().getSession().setAttribute("imageVerificationVo", imageVerificationVo);


            String uuid = RandomUtil.getUUID();
            Cookie cookie = new Cookie(GlobalConfig.IMAGE_CAPTCHA_CODE_COOKIE_KEY, uuid);
            cookie.setPath("/");
            response.addCookie(cookie);

            String key = GlobalConfig.IMAGE_CAPTCHA_CODE_COOKIE_KEY + "_" + uuid;
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            if (redisTemplate.hasKey(key)){
                LogUtil.error("selectSlideVerificationCode keyExists : {}, {}", key, JsonUtils.beanToJson(imageVerificationVo));
            }
            valueOperations.set(key, JsonUtils.beanToJson(imageVerificationVo), GlobalConfig.IMAGE_CAPTCHA_CODE_TTL, TimeUnit.SECONDS);


            //  根据原图生成遮罩图和切块图
            imageVerificationVo = ImageVerificationUtil.pictureTemplateCutout(verificationImage, originImageRead.getInputStream(), originImageFileType, readTemplateImage, templateImageFileType, imageVerificationVo.getX(), imageVerificationVo.getY());

            //   剪切图描边
            imageVerificationVo = ImageVerificationUtil.cutoutImageEdge(imageVerificationVo, borderImage, borderImageFileType);
            imageVerificationVo.setY(y);
            imageVerificationVo.setType(imageVerificationDto.getType());


            //  =============================================
            //  输出图片
//            HttpServletResponse response = getResponse();
//            response.setContentType("image/jpeg");
//            ServletOutputStream outputStream = response.getOutputStream();
//            outputStream.write(oriCopyImages);
//            BufferedImage bufferedImage = ImageIO.read(originImageFile);
//            ImageIO.write(bufferedImage, originImageType, outputStream);
//            outputStream.flush();
            //  =================================================

        } catch (Exception e) {
            LogUtil.error(e, " selectSlideVerificationCode error");
            return null;
        }

        return imageVerificationVo;
    }

    /**
     * 读取目标图
     *
     * @return
     * @throws
     */
    public ImageRead readTargetImage() {
        ImageRead imageRead = null;

        try {
            Random random = new Random(System.currentTimeMillis());
            if (verificationImagePathPrefix.indexOf("classpath") >= 0) {
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources(verificationImagePathPrefix.concat("/*"));

                if (resources == null) {
                    throw new RuntimeException("not found target image");
                }
                int i = random.nextInt(resources.length);
                imageRead = new ImageRead();
                imageRead.setImage(ImageIO.read(resources[i].getInputStream()));
                String extension = resources[i].getFilename().substring(resources[i].getFilename().lastIndexOf(".") + 1);
                imageRead.setInputStream(resources[i].getInputStream());
                imageRead.setFileExtension(extension);


            } else {
                File importImage = new File(verificationImagePathPrefix);
                if (importImage == null) {
                    throw new RuntimeException("not found target image");
                }
                File[] files = importImage.listFiles();
                int i = random.nextInt(files.length);
                imageRead = new ImageRead();
                imageRead.setImage(ImageIO.read(files[i]));
                String extension = files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1);
                imageRead.setFileExtension(extension);
                imageRead.setInputStream(new FileInputStream(files[i]));
            }


        } catch (Exception e) {
            LogUtil.error(e, " readTargetImage error");
            return null;
        }
        return imageRead;
    }

    /**
     * 读取模板图
     *
     * @param path
     * @return
     * @throws
     */
    public ImageRead readTemplateImage(String path) {
        ImageRead templateImageFile = null;
        try {
            if (templateImageFile != null) {
                return templateImageFile;
            }
            templateImageFile = new ImageRead();
            if (verificationImagePathPrefix.indexOf("classpath") >= 0) {
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource resource = resolver.getResource(path);
                if (resource == null) {
                    throw new RuntimeException("not found template image");
                }
                templateImageFile.setImage(ImageIO.read(resource.getInputStream()));
                String extension = resource.getFilename().substring(resource.getFilename().lastIndexOf(".") + 1);
                templateImageFile.setInputStream(resource.getInputStream());
                templateImageFile.setFileExtension(extension);
            } else {
                File file = new File(path);
                templateImageFile.setImage(ImageIO.read(file));
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                templateImageFile.setInputStream(new FileInputStream(file));
                templateImageFile.setFileExtension(extension);

            }

        } catch (Exception e) {
            LogUtil.error(e, " readTemplateImage error");
            return null;
        }
        return templateImageFile;
    }

    /**
     * 读取边框图
     *
     * @param path
     * @return
     * @throws
     */
    public ImageRead readBorderImageFile(String path) {
        ImageRead borderImageFile = null;
        try {
            if (borderImageFile != null) {
                return borderImageFile;
            }
            borderImageFile = new ImageRead();
            if (templateImagePathPrefix.indexOf("classpath") >= 0) {
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource resource = resolver.getResource(path);
                if (resource == null) {
                    throw new RuntimeException("not found template image");
                }
                borderImageFile.setImage(ImageIO.read(resource.getInputStream()));
                String extension = resource.getFilename().substring(resource.getFilename().lastIndexOf(".") + 1);
                borderImageFile.setInputStream(resource.getInputStream());
                borderImageFile.setFileExtension(extension);
            } else {
                File file = new File(path);
                borderImageFile.setImage(ImageIO.read(file));
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                borderImageFile.setInputStream(new FileInputStream(file));
                borderImageFile.setFileExtension(extension);
            }

        } catch (Exception e) {
            LogUtil.error(e, " readBorderImageFile error");
            return null;
        }
        return borderImageFile;
    }

    /**
     * 滑动验证码验证方法
     *
     * @param x x轴坐标
     * @param y y轴坐标
     * @return 滑动验证码验证状态
     */
    @Override
    public boolean checkVerificationResult(String x, String y) {
        int threshold = 5;
        ImageVerificationVo imageVerificationVo = null;
        String redisValue = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //ImageVerificationVo imageVerificationVo = (ImageVerificationVo) request.getSession().getAttribute("imageVerificationVo");
            redisValue = captchaCacheTool.getRedisCaptchaAlphabets(request, GlobalConfig.IMAGE_CAPTCHA_CODE_COOKIE_KEY );
            if (null == redisValue){
                throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
            }
            imageVerificationVo = JsonUtils.jsonToBean(redisValue, ImageVerificationVo.class);
            if (imageVerificationVo != null) {
                if ((Math.abs(Integer.parseInt(x) - imageVerificationVo.getX()) <= threshold) && y.equals(String.valueOf(imageVerificationVo.getY()))) {
                    LogUtil.error("checkVerificationResult 验证成功");
                    return true;
                } else {
                    LogUtil.error("checkVerificationResult 验证失败 imageVerificationVo=" + redisValue + "，X=" + x + "，Y=" + y);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            LogUtil.error(e, " checkVerificationResult error redisValue="+ redisValue + "，X=" + x + "，Y=" + y);
            return false;
        }
    }


}