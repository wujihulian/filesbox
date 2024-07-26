package com.svnlan.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.AddCloudDirectoryDTO;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.HomeSettingDTO;
import com.svnlan.home.service.HomeExplorerService;
import com.svnlan.home.service.SourceFileService;
import com.svnlan.home.service.SourceHistoryService;
import com.svnlan.home.vo.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.service.IoSourceService;
import com.svnlan.user.service.SystemLogService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.EnWebChatConfigVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author KingMgg
 * @data 2023/2/6 11:43
 */
@Api("首页的文件管理目录")
@RestController
public class HomeExplorerController {

    @Autowired
    HomeExplorerService homeExplorerService;

    @Resource
    private LoginUserUtil loginUserUtil;
    @Resource
    JWTTool jwtTool;
    @Resource
    SourceHistoryService sourceHistoryService;
    @Resource
    IoSourceService ioSourceService;
    @Resource
    SourceFileService sourceFileService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    OptionTool optionTool;
    @Value("${third.search.appId}")
    private String searchAppId;


    @Resource
    private SystemLogService systemLogService;

    /**
     * 获取文件目录
     *
     * @param homeExp
     * @return
     */
    @VisitRecord(isAsync = true)
    @GetMapping("/api/disk/list/path")
    public String homeExplorer(HomeExplorerDTO homeExp, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        HomeExplorerResult explorerResult;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            if (ObjectUtils.isEmpty(loginUser)) {
                result = new Result(false, CodeMessageEnum.bindSignError.getCode(), null);
                return JsonUtils.beanToJson(result);
            }
            explorerResult = homeExplorerService.homeExplorer(homeExp, loginUser);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), e.getMessage());
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), explorerResult);
        return JsonUtils.beanToJson(result);
    }

    /**
     * 获取文件详情
     *
     * @param
     * @return
     */
    @VisitRecord(isAsync = true)
    @GetMapping("/api/disk/getFileDetail")
    public String getFileDetail(HomeExplorerVO homeExplorerVO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        HomeFileDetailVO fileDetail;
        Result result;
        try {
            fileDetail = homeExplorerService.getFileDetail(homeExplorerVO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        result = new Result(true, CodeMessageEnum.success.getCode(), fileDetail);
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 创建文件夹
     * @params: [homeExplorerVO, httpServletRequest]
     * @Return: com.svnlan.common.Result
     * @Modified:
     */
    @PostMapping("/api/disk/createFolder")
    public String createFolder(@RequestBody HomeExplorerVO homeExplorerVO, HttpServletRequest httpServletRequest) {
        String prefix = this.jwtTool.findApiPrefix(httpServletRequest);
        LoginUser loginUser = loginUserUtil.getLoginUser(httpServletRequest);
        if (null == loginUser.getUserID()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.loginFirst.getMsg());
        }
        Result result;
        homeExplorerVO.setTargetID(loginUser.getUserID());
        homeExplorerVO.setCreateUser(loginUser.getUserID());
        homeExplorerVO.setModifyUser(loginUser.getUserID());
        homeExplorerVO.setIsFolder(1);
        try {
            IOSource source = homeExplorerService.createDir(homeExplorerVO, loginUser);
            Map<String, Object> resultMap = new HashMap<>(1);
            resultMap.put("source", source);
            result = new Result(true, CodeMessageEnum.success.getCode(), resultMap);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(),e.getMessage(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 设置文件夹操作（排序、图标大小等）
     * @params: [homeExplorerVO, httpServletRequest]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/2/18 10:51
     * @Modified:
     */
    @PostMapping("/api/disk/folder/setting")
    public String folderSetting(@RequestBody HomeSettingDTO homeExplorerVO, HttpServletRequest httpServletRequest) {
        String prefix = this.jwtTool.findApiPrefix(httpServletRequest);
        LoginUser loginUser = loginUserUtil.getLoginUser(httpServletRequest);
        if (null == loginUser.getUserID()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.loginFirst.getMsg());
        }
        Result result;
        homeExplorerVO.setTargetID(loginUser.getUserID());
        homeExplorerVO.setCreateUser(loginUser.getUserID());
        homeExplorerVO.setModifyUser(loginUser.getUserID());
        homeExplorerVO.setIsFolder(1);
        try {
            homeExplorerService.folderSetting(homeExplorerVO, loginUser);

            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        result = new Result(true, CodeMessageEnum.success.getCode(), true);
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 文件动态
     * @params: [homeExplorerVO, request]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/2/22 16:10
     * @Modified:
     */
    @GetMapping("/api/disk/pathLog")
    public String pathLog(HomeExplorerDTO homeExplorerVO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        PageResult re;
        Result result;
        try {
            re = homeExplorerService.getPathLog(homeExplorerVO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        result = new Result(true, CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }

    /**
     * 获取系统设置
     *
     * @param
     * @return
     */
    @VisitRecord(isAsync = true)
    @GetMapping("/api/disk/home")
    public String home(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Map<String, Object> explorerResult;
        try {
            explorerResult = homeExplorerService.systemOpenInfo();
            explorerResult.put("isShowSearch",!ObjectUtils.isEmpty(searchAppId) ? 1 : 0 );
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), explorerResult);
        return JsonUtils.beanToJson(result);
    }

    /**
     * 获取文件历史记录
     *
     * @param
     * @return
     */
    @GetMapping("/api/disk/history/get")
    public String sourceHistoryList(HttpServletRequest request, HomeExplorerDTO homeExplorerDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        HomeExplorerResult explorerResult;
        try {
            explorerResult = sourceHistoryService.getSourceHistoryList(homeExplorerDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), explorerResult);
        return JsonUtils.beanToJson(result);
    }

    @PostMapping("/api/disk/history/setDetail")
    public String sourceHistorySetDetail(HttpServletRequest request, @RequestBody HomeExplorerDTO homeExplorerDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        try {
            sourceHistoryService.setHistoryDetail(homeExplorerDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), null);
        return JsonUtils.beanToJson(result);
    }


    /**
     * @Description: 删除历史版本
     * @params: [request, homeExplorerDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/31 15:48
     * @Modified:
     */
    @PostMapping("/api/disk/history/delete")
    public String sourceHistoryDelete(HttpServletRequest request, @RequestBody HomeExplorerDTO homeExplorerDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        try {
            sourceHistoryService.deleteHistory(homeExplorerDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), null);
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 历史记录设置当前版本
     * @params: [request, homeExplorerDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/4/20 11:23
     * @Modified:
     */
    @PostMapping("/api/disk/history/setVersion")
    public String setHistoryRevision(HttpServletRequest request, @RequestBody HomeExplorerDTO homeExplorerDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            sourceHistoryService.setHistoryRevision(homeExplorerDTO, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), null);
        return JsonUtils.beanToJson(result);
    }

    @GetMapping("/api/disk/parentSourceList")
    public String parentSourceList(HttpServletRequest request, HomeExplorerDTO homeExplorerDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        List re;
        try {
            re = sourceHistoryService.parentSourceList(homeExplorerDTO, loginUserUtil.getLoginUser());
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }

    @GetMapping("/api/disk/sourcePreview")
    public String parentSourceList2(HttpServletRequest request, HomeExplorerDTO homeExplorerDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        JSONObject re;
        try {
            re = sourceHistoryService.parentSourceList2(homeExplorerDTO, loginUserUtil.getLoginUser());
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }


    /**
     * 获取用户文件类型占比
     */
    @GetMapping("/api/disk/userProportion")
    public Result getFileTypeProportionByUserId() {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        List<JSONObject> jsonList = ioSourceService.getFileTypeProportionByUserId(loginUser.getUserID());

        return Result.returnSuccess(
                new JSONObject().fluentPut("fileTypeProportion", jsonList)
        );
    }

    @RequestMapping(value = "/api/disk/addBatchDirectory", method = RequestMethod.POST)
    public String addBatchDirectory(@Valid @RequestBody AddCloudDirectoryDTO addCloudDirectoryDTO) {
        Result result = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            List<AddSubCloudDirectoryDTO> list = sourceFileService.addBatchDirectory(addCloudDirectoryDTO, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), list);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, CodeMessageEnum.system_error.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            LogUtil.error(e, "云盘文件夹批量创建失败" + JsonUtils.beanToJson(addCloudDirectoryDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/homeExplorer", method = RequestMethod.GET)
    public String homeExplorerVo(HomeExplorerDTO homeExp, HttpServletRequest request) {
        Result result = null;
        try {
            HomeExplorerVO re = homeExplorerService.getHomeExplorer(homeExp, loginUserUtil.getLoginUser());
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/homeExplorer获取详情" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/execImgVideoThumb", method = RequestMethod.GET)
    public String execImgVideoThumb(HomeExplorerDTO homeExp, HttpServletRequest request) {
        Result result = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            HomeExplorerVO re = homeExplorerService.execImgVideoThumb(homeExp, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/execImgVideoThumb" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 用户登陆设置统计
     */
    @RequestMapping(value = "/api/disk/loginDevice/stat", method = RequestMethod.GET)
    public Result userLoginDeviceStat() {
        return Result.returnSuccess(systemLogService.selectUserLoginDeviceStat(30, loginUserUtil.getLoginUserId()));
    }

    /**
       * @Description: 获得文件夹下的图片列表
       * @params:  [homeExp, request]
       * @Return:  java.lang.String
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/getImgList", method = RequestMethod.GET)
    public String getImgList(HomeExplorerDTO homeExp) {
        Result result = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            List re = sourceFileService.getImgList(homeExp, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/getImgList" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/getAboutEnInfo", method = RequestMethod.GET)
    public String getAboutEnInfo(HomeExplorerDTO homeExp) {
        Result result = null;
        try {
            EnWebChatConfigVo config = optionTool.getEnWebChatConfig();
            Map<String, String> re = new HashMap<>(1);
            re.put("agentId", config.getAgentId());
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/getAboutEnInfo" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/setCover", method = RequestMethod.POST)
    public String setCover(@RequestBody HomeExplorerDTO homeExp) {
        Result result = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            boolean re = sourceFileService.setCover(homeExp, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, "/api/disk/setCover svn=" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/setCover e=" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/setDesktopCover", method = RequestMethod.POST)
    public String setDesktopCover(@RequestBody HomeExplorerDTO homeExp) {
        Result result = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            boolean re = sourceFileService.setDesktopCover(homeExp, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/setDesktopCover" + JsonUtils.beanToJson(homeExp));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }



    @RequestMapping(value = "/api/disk/mkDir", method = RequestMethod.POST)
    public String mkDir(@Valid @RequestBody AddCloudDirectoryDTO addCloudDirectoryDTO) {
        Result result = null;
        Long sourceId = 0L;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            sourceId = sourceFileService.mkDir(addCloudDirectoryDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), sourceId);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, CodeMessageEnum.system_error.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            LogUtil.error(e, "云盘文件夹创建失败" + JsonUtils.beanToJson(addCloudDirectoryDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
}


