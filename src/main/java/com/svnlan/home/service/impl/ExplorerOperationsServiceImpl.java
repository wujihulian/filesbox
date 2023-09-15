package com.svnlan.home.service.impl;

import com.svnlan.home.dao.ExplorerOperationsDao;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.ExplorerOperationsService;
import com.svnlan.home.utils.AsyncCountSizeUtil;
import com.svnlan.home.vo.HomeExplorerShareDetailVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.Partition;
import com.svnlan.utils.RandomUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author KingMgg
 * @data 2023/2/7 16:13
 */
@Service
public class ExplorerOperationsServiceImpl implements ExplorerOperationsService {

    @Resource
    ExplorerOperationsDao operationsDao;
    @Resource
    AsyncCountSizeUtil asyncCountSizeUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void initSourcePathLevel(Long sourceID) {

        Map<String, Object> paramMap = new HashMap<>(0);
        if (!ObjectUtils.isEmpty(sourceID) && sourceID > 0){
            paramMap.put("sourceID", sourceID);
        }

        List<HomeExplorerShareDetailVO> list = operationsDao.getAllSourceList(paramMap);

        Map<Long, String> levelMap = new HashMap<>(1);
        levelMap.put(1L, ",0,1,");
        levelMap.put(0L, ",0,");

        levelMap = checkList(levelMap, list);

        for (HomeExplorerShareDetailVO detailVO : list){
            if (detailVO.getParentID() <= 0 ){
                continue;
            }
            detailVO.setParentLevel(levelMap.get(detailVO.getParentID()));
        }

        for (List<HomeExplorerShareDetailVO> subList : Partition.ofSize(list, 1500)) {
            try {
               // operationsDao.batchUpdateLevel(subList);
            } catch (Exception e) {
                LogUtil.error(e, "复制出错");
            }
        }

        System.out.println(true);

    }

    private Map<Long, String> checkList(Map<Long, String> levelMap, List<HomeExplorerShareDetailVO> list){
        List<HomeExplorerShareDetailVO> childList = new ArrayList<>();
        List<String> deList = new ArrayList<>();
        for (HomeExplorerShareDetailVO detailVO : list){
            if (1 == detailVO.getIsFolder()){
                if (detailVO.getParentID() <= 0){
                    levelMap.put(detailVO.getSourceID(), ",0," + detailVO.getSourceID() + ",");
                }else {
                    if (levelMap.containsKey(detailVO.getParentID())){
                        levelMap.put(detailVO.getSourceID(), levelMap.get(detailVO.getParentID()) + detailVO.getSourceID() + ",");
                    }else {

                        if (detailVO.getParentLevel().indexOf("," + detailVO.getParentID() + ",") >= 0) {
                            childList.add(detailVO);
                        }else {
                            deList.add(detailVO.getSourceID() + "");
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(deList)){
            LogUtil.info("deList===========" + deList.stream().collect(Collectors.joining(",", "", "")));
        }
        if (!CollectionUtils.isEmpty(childList)){
            levelMap = checkList(levelMap, childList);
        }
        return levelMap;
    }

    @Override
    public void countSize(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser){

        if (ObjectUtils.isEmpty(updateFileDTO.getTaskID())){
            updateFileDTO.setTaskID(RandomUtil.getuuid());
            resultMap.put("taskID", updateFileDTO);
            asyncCountSizeUtil.asyncCountSize(updateFileDTO);
        }else {
            String a = stringRedisTemplate.opsForValue().get("countSize_taskID_key_" + updateFileDTO.getTaskID() );
            resultMap.put("status", a);
            if ("1".equals(a)){
                stringRedisTemplate.delete("countSize_taskID_key_" + updateFileDTO.getTaskID());
            }
        }

    }
}
