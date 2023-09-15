package com.svnlan.user.dao;

import com.svnlan.user.domain.UserOption;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserOptionVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:07
 */
public interface UserOptionDao {

    List<OptionVo> getUserSystemConfig(Long userID);
    List<OptionVo> getUserOtherConfig(@Param("userID") Long userID, @Param("type") String type);
    int batchInsert(List<UserOption> list);
    int updateOptionValueByKey(@Param("userID") Long userID, @Param("key") String key, @Param("value") String value, @Param("type") String type);
    int updateSystemOptionValueByKey(@Param("userID") Long userID, @Param("key") String key, @Param("value") String value);
    String getUserConfigByKey(@Param("userID") Long userID, @Param("key") String key);
    OptionVo getUserConfigVoByKey(@Param("userID") Long userID, @Param("key") String key);
    String getUserOtherConfigByKey(@Param("userID") Long userID, @Param("type") String type, @Param("key") String key);
    List<UserOptionVo> getUserSortConfig(@Param("userID") Long userID, @Param("list") List<String> list);
    int delOptionByUserID(@Param("userID") Long userID, @Param("key") String key);

    @Update("UPDATE system_option SET value = #{value} WHERE id = #{id}")
    int updateValueById(@Param("id") Integer id, @Param("value") String value);
}
