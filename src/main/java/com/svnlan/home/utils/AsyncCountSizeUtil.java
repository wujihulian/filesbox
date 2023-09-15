package com.svnlan.home.utils;

import com.svnlan.home.dao.ExplorerOperationsDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/9/9 11:33
 */
@Component
public class AsyncCountSizeUtil {
    @Resource
    ExplorerOperationsDao operationsDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Async(value = "asyncTaskExecutor")
    public void asyncCountSize(CheckFileDTO updateFileDTO) {
        String key = "countSize_taskID_key_" + updateFileDTO.getTaskID();
        stringRedisTemplate.opsForValue().set(key, "0", 1, TimeUnit.HOURS );
        List<Long> sourceIds = Arrays.asList(updateFileDTO.getOperation().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sourceIds)){
            stringRedisTemplate.opsForValue().set("countSize_taskID_key_" + updateFileDTO.getTaskID(), "2", 1, TimeUnit.HOURS );
            return;
        }
        List<IOSource> countUpdateList = null;
        List<IOSource> countList = null;
        Map<Long, Long> countMap = null;
        List<IOSource> list = ioSourceDao.copySourceList(sourceIds);
        for (IOSource source : list){
            countMap = new HashMap<>();
            countList =  operationsDao.getSourceListByLevelToContSize(source.getParentLevel()+source.getSourceID()+",", ObjectUtils.isEmpty(updateFileDTO.getStatus()) ? 0 : updateFileDTO.getStatus());
            try {
                for (IOSource vo : countList){
                    List<Long> fromSourceIds = Arrays.asList(vo.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    for (Long pId : fromSourceIds){
                        if (countMap.containsKey(pId)){
                            countMap.put(pId, (countMap.get(pId) + vo.getSize()));
                        }else {
                            countMap.put(pId, vo.getSize());
                        }
                    }
                }
                if (!ObjectUtils.isEmpty(countMap)){
                    countUpdateList = new ArrayList<>();
                    for (Map.Entry<Long, Long> entry : countMap.entrySet()) {
                        countUpdateList.add(new IOSource(entry.getKey(), entry.getValue()));
                    }
                    operationsDao.batchUpdateSizeByCountSize(countUpdateList);
                }
            }catch (Exception e){
                LogUtil.error(e, " 统计错误 source=" + JsonUtils.beanToJson(source));
            }
        }
        stringRedisTemplate.opsForValue().set(key, "1", 1, TimeUnit.HOURS );
    }

}
