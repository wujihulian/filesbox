package com.svnlan.home.controller;

import com.svnlan.annotation.CreateGroup;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.ShareDTO;
import com.svnlan.home.service.ShareService;
import com.svnlan.home.vo.HomeExplorerResult;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.ShareReportDTO;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.ShareReportService;
import com.svnlan.user.vo.SelectAuthVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description: 分享/协作
 * @Date: 2023/3/3 13:33
 */
@RestController
public class ShareController {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    ShareService shareService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * @Description: 添加外部分享
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/3 14:41
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveShare(@Valid @RequestBody ShareDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            shareService.saveShare(shareDTO, loginUser);
            stringRedisTemplate.delete(GlobalConfig.my_share_key + loginUser.getUserID());
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "保存分享失败" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 取消外部分享
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/3 14:41
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String cancelShare(@Valid @RequestBody ShareDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            shareService.cancelShare(shareDTO, loginUser);
            stringRedisTemplate.delete(GlobalConfig.my_share_key + loginUser.getUserID());
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "取消分享失败" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 获得单个分享链接详情
     * @params: [labelDto]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/3 13:37
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShare(ShareDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            ShareVo re = shareService.getShare(shareDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "获得单个分享链接详情失败" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 分享列表
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/8 14:17
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShareList(HomeExplorerDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();

        HomeExplorerResult re = null;
        try {
            re = shareService.getShareList(shareDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "分享列表" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 对外查询分享配置
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/6 16:11
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/share/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShareFile(ShareDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            ShareVo re = shareService.getShareFile(shareDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, " 对外查询分享配置失败" + JsonUtils.beanToJson(shareDTO));
            if (e instanceof IllegalArgumentException) {
                result = new Result(false, e.getMessage());
            }else{
                result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            }
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 查询分享下文件夹列表
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/7 17:30
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/share/pathList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShareFileList(HomeExplorerDTO shareDTO) {
        Result result;
        try {
            HomeExplorerResult re = shareService.getLinkShareList(shareDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, " 对外查询分享配置失败" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/share/menu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShareMenuList(HomeExplorerDTO shareDTO) {
        Result result;
        try {
            shareDTO.setIsFolder(1);
            HomeExplorerResult re = shareService.getLinkShareList(shareDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, " 对外查询分享左边菜单列表失败" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 与我协作列表
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/8 14:17
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/shareToMe", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String shareToMe(HomeExplorerDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();

        HomeExplorerResult re = null;
        try {
            re = shareService.shareToMeList(shareDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "与我协作列表" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 选择协作部门或用户列表
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/9 9:10
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/groupList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String userGroupList(HomeExplorerDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();

        SelectAuthVo re = null;
        try {
            re = shareService.userGroupList(shareDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "选择协作部门或用户列表 error" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/share/previewNum", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String previewNum(ShareDTO shareDTO) {
        Result result;
        Map<String, Object> re = null;
        String key = "previewNum_" + shareDTO.getShareCode();
        try {
            final Boolean setFlag = this.stringRedisTemplate.opsForValue().setIfAbsent(key, "1");
            if (setFlag == null || !setFlag) {
                // 流程处理中
                re = new HashMap<>(1);
                re.put("numView", 0);
            } else {
                re = shareService.previewNum(shareDTO);
            }
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, " 对外查询分享配置失败" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } finally {
            this.stringRedisTemplate.delete(key);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 选择协作部门或用户列表 (用于搜索，只返回用户)
     * @params: [shareDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/5/8 16:54
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/userShare/userList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String userList(UserDTO shareDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();

        PageResult re = null;
        try {
            re = shareService.getUserList(loginUser, shareDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "选择用户列表 error" + JsonUtils.beanToJson(shareDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @Resource
    private ShareReportService shareReportService;

    /**
     * 对分享的举报
     * 可以匿名举报
     */
    @PostMapping("api/disk/share/report")
    public Result shareReport(@RequestBody @Validated(CreateGroup.class) ShareReportDTO dto) {
        shareReportService.shareReport(dto);
        return Result.returnSuccess();
    }
}
