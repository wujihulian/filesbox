package com.svnlan.user.dao;

import com.svnlan.user.vo.OptionVo;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 13:53
 */
public interface SystemOptionDao {

    List<OptionVo> getSystemConfig();

    List<OptionVo> getSystemOtherConfig(String type, Long tenantId);

    String getSystemConfigByKey(String key);
    String getSystemConfigByKey(String key, Long tenantId);

    OptionVo getSystemOtherConfigByKey(String type, String key);

    void updateSystemOption(String type, List<OptionVo> list, Long tenantId);

    OptionVo getSystemOtherConfigById(Integer id);

    int updateSystemOptionById(Integer id, String value);

    List<OptionVo> selectListByType(String storageType);

    List<OptionVo> getSystemConfigByKeyListBy(List<String> list);

    int insert(OptionVo entity);

    OptionVo selectById(Integer id);

    OptionVo selectOneByKeyString(String keyString);

    List<OptionVo> selectOneByTypeAndKeyString(String type, String keyString, Boolean isInclude);

    void updateById(OptionVo optionVo);

    void updateByKeyStringAndType(OptionVo optionVoUpdate);

    int deleteById(Integer id);


    int batchInsert(List<OptionVo> list);
    List<String> checkSystemConfigByKeyList(List<String> list, Long tenantId);
    void initSourceStorageId(Integer storageId);
    List<OptionVo> getStorageList(String type);


}
