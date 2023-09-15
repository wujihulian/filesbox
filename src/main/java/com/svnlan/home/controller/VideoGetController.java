package com.svnlan.home.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.service.VideoGetService;
import com.svnlan.home.service.VideoImagesService;
import com.svnlan.home.utils.ConvertUtil;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author: sulijuan
 * @Description: 视频剪辑
 * @Date: 2023/5/17 11:27
 */
@RestController
public class VideoGetController {

    @Resource
    VideoGetService videoGetService;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    VideoImagesService videoImagesService;

    /**
       * @Description: 根据帧数获取图片
       * @params:  [videoCommonDto]
       * @Return:  java.lang.String
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/video/imgList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoList(VideoCommonDto videoCommonDto){
        Result result;
        List re = null;
        try {
            re = videoGetService.getVideoShearListAll(videoCommonDto);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "获取视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/video/checkImgCut", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkImgCut(VideoCommonDto videoCommonDto){
        Result result;
        boolean re = false;
        try {
            re = videoGetService.checkImgCut(videoCommonDto);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "获取视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 视频剪切
       * @params:  [videoCommonDto]
       * @Return:  java.lang.String
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/video/cut", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoCut(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/cut videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.cutVideo(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "cut剪切视频失败 " + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 视频剪切
       * @params:  [videoCommonDto]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/5/18 16:56
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/video/cutSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoCutSave(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/cutSave videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.cutVideoSave(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            if (re.containsKey("convertList")){
                List<CommonSource> list = ObjUtil.objectToList(re.get("convertList"), CommonSource.class);
                // 视频转码
                if (!CollectionUtils.isEmpty(list)){
                    for (CommonSource source : list){
                        ConvertDTO convertDTO = new ConvertDTO();
                        convertDTO.setBusId(source.getSourceID());
                        convertDTO.setBusType("cloud");
                        convertDTO.setOtherType("cutVideo");
                        String serverUrl = HttpUtil.getRequestRootUrl(null);
                        source.setDomain(serverUrl);
                        convertDTO.setDomain(serverUrl);
                        convertUtil.doConvert(convertDTO, source);
                    }
                }
                re.put("convertList", null);
            }
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "cutSave剪切视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
    /**
     * @Description: 视频拆分
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/splitSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoSplitSave(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/cutSave videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.splitVideoSave(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            if (re.containsKey("convertList")){
                List<CommonSource> list = ObjUtil.objectToList(re.get("convertList"), CommonSource.class);
                // 视频转码
                if (!CollectionUtils.isEmpty(list)){
                    String serverUrl = HttpUtil.getRequestRootUrl(null);
                    for (CommonSource source : list){
                        ConvertDTO convertDTO = new ConvertDTO();
                        convertDTO.setBusId(source.getSourceID());
                        convertDTO.setBusType("cloud");
                        convertDTO.setOtherType("splitVideo");
                        source.setDomain(serverUrl);
                        convertDTO.setDomain(serverUrl);
                        convertUtil.doConvert(convertDTO, source);
                    }
                }
                re.put("convertList", null);
            }
            result = new Result(true, CodeMessageEnum.success.getCode(), re);

        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "splitSave拆分视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 视频合并
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/mergeSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoMergeSave(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/videoMergeSave videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.mergeVideoSave(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);

        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "videoMergeSave合并视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 视频转码
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/convertSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoConvertSave(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/videoConvertSave videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.convertVideoSave(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);

        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "videoConvertSave转码视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 视频设置
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/setting", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoConfigSave(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/videoConfigSave videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.videoConfigSave(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);

        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "videoConvertSave转码视频失败" + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 视频剪切-照相
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/cutImg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoCutImg(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/cutImg videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.cutVideoImg(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "cut剪切视频失败 " + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 视频剪切-抽取音频
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/copyAudio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String videoCopyAudio(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/copyAudio videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoGetService.copyAudio(videoCommonDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "cut剪切视频失败 " + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
    /**
     * @Description: 图片转视频
     * @params:  [videoCommonDto]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/video/imagesToVideo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String imagesToVideo(@RequestBody VideoCommonDto videoCommonDto){
        Result result;
        Map<String, Object> re = null;
        try {
            if (ObjectUtils.isEmpty(videoCommonDto.getTaskID())){
                videoCommonDto.setTaskID(RandomUtil.getuuid());
            }
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info("/api/disk/video/imagesToVideo videoCommonDto=" + JsonUtils.beanToJson(videoCommonDto));
            re = videoImagesService.imagesToVideo(videoCommonDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "imagesToVideo  " + JsonUtils.beanToJson(videoCommonDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/video/imagesToVideo/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String imagesToVideoTaskAction(HttpServletResponse response, CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = videoImagesService.taskAction(updateFileDTO, resultMap, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "imagesToVideo taskAction 失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        if (!ObjectUtils.isEmpty(resultMap) && resultMap.containsKey("status") && "1".equals(resultMap.get("status").toString())) {
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        }
        return JsonUtils.beanToJson(result);
    }
}
