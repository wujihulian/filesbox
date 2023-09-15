package com.svnlan.user.tools;

import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.vo.GroupParentPathDisplayVo;
import com.svnlan.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/31 16:11
 */
@Component
public class GroupTool {

    @Resource
    GroupDao groupDao;

    public Map<Long, String> getParentPathDisplayMap(Set<String> pList){
        Map<Long, String> map = null;
        if (!CollectionUtils.isEmpty(pList)){
            List<GroupParentPathDisplayVo> list = groupDao.getGroupParentPathDisplay(new ArrayList<>(pList));
            if (!CollectionUtils.isEmpty(list)){
                map = new HashMap<>(1);
                List<Long> idList = null;
                List<String> nameList = null;
                for (GroupParentPathDisplayVo vo : list){
                    if(ObjectUtils.isEmpty(vo.getParentIDStr())){
                        continue;
                    }
                    idList = Arrays.asList(vo.getParentIDStr().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    nameList = Arrays.asList(vo.getParentLevelName().split(",")).stream().collect(Collectors.toList());
                    if (idList.size() != nameList.size()){
                        continue;
                    }
                    int j = 0;
                    for (Long id : idList){
                        map.put(id, nameList.get(j));
                        j++;
                    }
                }
            }
        }
        return map;
    }

    public String setParentPathDisplay(Map<Long, String> map, String parentLevel){

        List<Long> idList = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)){
            return "";
        }
        List<String> nameList = new ArrayList<>();
        for (Long id: idList){
            if (map.containsKey(id)){
                nameList.add(map.get(id));
            }
        }
        return CollectionUtils.isEmpty(nameList) ? "" : StringUtil.joinString(nameList,"/");
    }
}
