package com.svnlan.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.user.vo.OptionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 13:53
 */
public interface SystemOptionDao extends BaseMapper<OptionVo> {

    List<OptionVo> getSystemConfig();
    List<OptionVo> getSystemOtherConfig(String type);
    String getSystemConfigByKey(@Param("key") String key);
    OptionVo getSystemOtherConfigByKey(@Param("type") String type, @Param("key") String key);
    int updateSystemOption(@Param("type") String type, @Param("list") List<OptionVo> list);
    List<OptionVo> getSystemConfigByKeyList(@Param("list") List<String> list);
    OptionVo getSystemOtherConfigById(Integer id);
    int updateSystemOptionById(@Param("id") Integer id, @Param("value") String value);
    int batchInsert(List<OptionVo> list);
    List<String> checkSystemConfigByKeyList(@Param("list") List<String> list);
}
