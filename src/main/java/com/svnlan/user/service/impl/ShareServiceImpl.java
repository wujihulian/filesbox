package com.svnlan.user.service.impl;

import com.github.pagehelper.PageInfo;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.AdminShareDao;
import com.svnlan.user.dto.ShareDTO;
import com.svnlan.user.service.ShareService;
import com.svnlan.user.vo.ShareVo;
import com.svnlan.utils.LoginUserUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.svnlan.enums.LogTypeEnum.fileShareToRemove;
import static com.svnlan.enums.LogTypeEnum.shareLinkRemove;

/**
 * 分享相关 实现
 *
 * @author lingxu 2023/04/04 14:37
 */
@Slf4j
@Service("adminShareService")
public class ShareServiceImpl implements ShareService {

    @Resource
    private AdminShareDao adminShareDao;

    @Resource
    private FileOptionTool fileOptionTool;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private IoSourceDao sourceDao;

    @Resource
    private SystemLogTool systemLogTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public PageInfo<ShareVo> shareListPage(ShareDTO dto) {

        if (!ObjectUtils.isEmpty(dto.getSortField()) && !Arrays.asList("createTime","timeTo","numDownload","numView").contains(dto.getSortField())){
            dto.setSortField("createTime");
        }
        int total = Optional.ofNullable(adminShareDao.shareListCount(dto)).orElse(0);
        PageInfo<ShareVo> pageInfo = new PageInfo<>();
        pageInfo.setTotal(total);
        pageInfo.setPages(dto.getCurrentPage());
        pageInfo.setPageSize(dto.getPageSize());
        if (total <= 0) {
            return pageInfo;
        }
        dto.setStartIndex((dto.getCurrentPage() - 1) * dto.getPageSize());
        List<ShareVo> list = adminShareDao.shareList(dto, false);
        if (!CollectionUtils.isEmpty(list)){
            for (ShareVo vo : list){
                if (!ObjectUtils.isEmpty(vo.getAvatar())){
                    vo.setAvatar(FileUtil.getShowAvatarUrl(vo.getAvatar(), vo.getName()));
                }
            }
        }
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public List<ShareVo> shareList(ShareDTO dto) {
        return adminShareDao.shareList(dto, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelShare(Long id) {
        ShareVo shareVo = adminShareDao.getById(id);
        Assert.notNull(shareVo, "该分享链接资源不存在");
        // 删除该条分享咨询
        Assert.isTrue(adminShareDao.cancelShare(id) == 1, "分享链接" + id + "取消失败");

        stringRedisTemplate.delete(GlobalConfig.my_share_key + shareVo.getUserID());
        // 写事件
        writeLog(shareVo);
    }

    private void writeLog(ShareVo shareVo) {
        // 查询资源
        String name = null;
        CommonSource commonSource = fileOptionTool.getSourceInfo(shareVo.getSourceID());
        name = commonSource.getName();
        if (!StringUtils.hasText(name)) {
            // 表示为文件夹
            IOSource ioSource = sourceDao.getSourceNameBySourceId(shareVo.getSourceID());
            name = ioSource.getName();
        }
        LoginUser loginUser = loginUserUtil.getLoginUser();
        fileOptionTool.addSourceEvent(shareVo.getSourceID(), commonSource.getParentID(), loginUser.getUserID(), name, EventEnum.shareLinkRemove);
        // 异步写日志
        setSysLog(commonSource, loginUser, shareVo.getIsLink());
    }

    @Override
    public void cancelShare(ArrayList<Integer> idList) {
        List<ShareVo> shareVoList = adminShareDao.getByIds(idList);
        // 只能取消还存在的资源
        Assert.notEmpty(shareVoList, "该分享链接资源不存在");

        adminShareDao.cancelMultiShare(shareVoList.stream().map(ShareVo::getShareID).collect(Collectors.toList()));
        // 删除缓存
        Set<String> keys = new HashSet<>();
        shareVoList.forEach(n -> keys.add(GlobalConfig.my_share_key + n.getUserID()));
        stringRedisTemplate.delete(keys);

        shareVoList.forEach(this::writeLog);

    }

    /**
     * 异步写日志
     *
     * @param commonSource
     * @param loginUser
     */
    private void setSysLog(CommonSource commonSource, LoginUser loginUser, Integer type) {

        Map<String, Object> reMap = new HashMap<>(4);
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap.put("sourceID", commonSource.getSourceID());
        reMap.put("sourceParent", commonSource.getParentID());
        reMap.put("type", "share");
        reMap.put("pathName", commonSource.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, Objects.equals(type, 1) ? shareLinkRemove.getCode() : fileShareToRemove.getCode(), paramList, systemLogTool.getRequest());
    }

}
