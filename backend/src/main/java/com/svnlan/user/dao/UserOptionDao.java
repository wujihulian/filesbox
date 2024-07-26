package com.svnlan.user.dao;

import com.svnlan.user.domain.UserOption;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserOptionVo;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:07
 */
public interface UserOptionDao {

    List<OptionVo> getUserSystemConfig(Long userID);

    int batchInsert(List<UserOption> list);

    int updateOptionValueByKey(Long userID, String key, String value, String type);

    int updateSystemOptionValueByKey(Long userID, String key, String value);

    OptionVo getUserConfigVoByKey(Long userID, String key);

    String getUserOtherConfigByKey(Long userID, String type, String key);

    List<UserOptionVo> getUserSortConfig(Long userID, List<String> list);

}
