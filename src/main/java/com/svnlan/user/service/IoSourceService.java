package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.vo.IOSourceVo;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * 资源 服务
 *
 * @author lingxu 2023/04/10 15:26
 */
public interface IoSourceService extends IService<IOSource> {

    JSONObject fileInfoOverview();

    List<JSONObject> getFileTypeProportion();

    IOSourceVo getUserRootDirectory(Long userID);

    List<Long> getSourceIdByNameAndUserId(List<String> pathList, Long userId, List<Pair<IOSourceVo, List<Long>>> pairList, Long rootId, int isFolder);

    List<JSONObject> getFileTypeProportionByUserId(Long userID);

    IOFile getFileContentByNameAndUserId(String canonicalName, Long userId, Boolean isVideoFile);

    String deriveProperName(String parentLevel, String lastPath);

    boolean createDirectory(Long userId, String name, String parentLevel, Long parentId);
}
