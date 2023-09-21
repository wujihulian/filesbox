package com.svnlan.home.utils;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/9/8 17:10
 */
@Component
public class CopyFileSaveUtil {

    public void saveParentLevelSource(CheckFileDTO updateFileDTO, Map<String, IOSource> parentMap, IoSourceDao ioSourceDao , LoginUser loginUser
    , Integer targetType, List<IOSource> copyListByLevelCo, int s){

        LogUtil.info("saveParentLevelSource s=" + s + "，parenatMp=" + JsonUtils.beanToJson(parentMap) + "，copyListByLevelCo=" + JsonUtils.beanToJson(copyListByLevelCo));
        if (s >= 50){
            LogUtil.info("saveParentLevelSource s=" + s);
            return;
        }
        List<IOSource> copyListByLevel = new ArrayList<>();
        List<IOSource> nextCopyListByLevel = new ArrayList<>();
        if (!CollectionUtils.isEmpty(copyListByLevelCo)) {
            for (IOSource source : copyListByLevelCo) {

                if (source.getParentID() > 0) {
                    source.setOldParentID(source.getParentID());
                    source.setOldSourceID(source.getSourceID());
                    source.setOldSourceLevel(source.getParentLevel() + source.getSourceID() + ",");
                    source.setOldParentLevel(source.getParentLevel());
                }
                source.setParentID(0L);
                source.setParentLevel(",0,");

                source.setTargetID(loginUser.getUserID());
                source.setCreateUser(loginUser.getUserID());
                source.setModifyUser(loginUser.getUserID());
                source.setTargetType(targetType);

                source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
                source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));

                if (!ObjectUtils.isEmpty(parentMap) && parentMap.containsKey(source.getOldParentLevel())) {
                    IOSource so = parentMap.get(source.getOldParentLevel());
                    source.setParentID(so.getSourceID());
                    source.setParentLevel(so.getParentLevel() + so.getSourceID() + ",");
                }
                LogUtil.info("saveParentLevelSource 移动 parentID=" + source.getParentID() +"  updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO) + " copyListByLevelCo source=" + JsonUtils.beanToJson(source));
                if (source.getParentID() <= 0) {
                    LogUtil.error("saveParentLevelSource 移动出错 updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO) + " copyListByLevelCo source=" + JsonUtils.beanToJson(source));
                    nextCopyListByLevel.add(source);
                    continue;
                }
                source.setSourceID(null);

                copyListByLevel.add(source);
            }
        }

        if (!CollectionUtils.isEmpty(copyListByLevel)) {
            try {
                ioSourceDao.batchInsert(copyListByLevel);
            } catch (Exception e) {
                LogUtil.error(e, "复制出错 copyListByLevel");
                throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
            }
            for (IOSource source : copyListByLevel) {
                parentMap.put(source.getOldSourceLevel(), source);
            }

            LogUtil.info("saveParentLevelSource 移动 nextCopyListByLevel=" + JsonUtils.beanToJson(nextCopyListByLevel) +"  updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO) );
            if (!CollectionUtils.isEmpty(nextCopyListByLevel)){
                saveParentLevelSource(updateFileDTO, parentMap, ioSourceDao , loginUser
                        , targetType, nextCopyListByLevel, s+1);
            }
        }


    }
}
