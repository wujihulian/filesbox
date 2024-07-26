package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.domain.User;
import com.svnlan.user.vo.UserVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:13
 */

public interface UserDao {

    int insert(User user);

    int update(User user);

    UserVo getUserInfo(Long userID);

    List<UserVo> getUserListByParam(Map<String, Object> map);


    Long getUserListByParamCount(Map<String, Object> map);

    int updateUserState(List<Long> list, Integer status);

    int updateNickname(String nickname, Long userID);

    int updateSex(String sex, Long userID);

    int updateAvatar(String avatar, Long userID);

    int updateAvatarList(List<Long> list, String avatar);

    int updateEmail(String email, Long userID);

    int updatePassword(String password, Long userID);

    int findByEmail(String email);

    int findByNickname(String nickname);

    UserVo getUserSimpleInfo(Long userID);

    List<UserVo> findByName(String name);

    List<UserVo> getUserBaseInfo(List<Long> list);

    UserVo getUserBaseOneInfo(Long userID);

    //    @Select("SELECT SUM(sizeUse) FROM users")
    Long sumUserSpaceUsed();

    //    @Select("SELECT count(1) FROM users WHERE status = 1 AND is_system = 0")
    Long selectUserCount();

    List<UserVo> findByPhone(String phone);

    int updatePhone(String phone, Long userID);

    //    @Select("SELECT password from users where name = #{username} AND status != 2")
    String getPasswordByUserName(String username);

    //    @Select("SELECT userID, name, password, status, sex from users where name = #{username}")
    UserVo getUserByUserName(String username);

    List<UserVo> queryUserInfoByOpenIdOrMobile(String openId, String mobile, String code, String unionId, String dingUserId);

    //    @Select("SELECT userID, wechatOpenId, enWechatOpenId, avatar, nickname,phone,email, status FROM users WHERE userID =#{userId}")
    UserVo queryUserByUserId(Long userId);

    //    @Update("UPDATE users SET ${value}OpenId = NULL WHERE userID = #{userID} AND ${value}OpenId IS NOT NULL")
    int removeOpenId(Long userID, String value);

    List<UserVo> getUserSimpleInfoByIds(Collection<Long> userIds);

    //    @Select("SELECT createTime FROM users WHERE userID = #{userId}")
    Long getCreateTime(Long userId);

    List<Long> getUserIdByRoleId(List<Long> roleIdList);

    //    @Select("SELECT userID AS uid, ifnull(if(length(nickname)>0,nickname,null),name) n, avatar a, sizeMax sm, sizeUse s " +
//            "FROM users ORDER BY s DESC LIMIT #{topCount}")
    List<JSONObject> getUserSpaceRank(Integer topCount, Long tenantId);

    User selectAvatarAndNickName(Long userId);

    void setUserDingUnionid(UserVo userVo);

    Integer getAllUserCount(Long tenantId);

    List<String> getAllUserByDingUnionId(Long tenantId);
    List<String> getAllUserByEnWebChat(Long tenantId);
    List<String> getAllUserByCube(Long tenantId);
    Long getUserIdByPhone(String phone, Long tenantId);
    Long getUserIdByImUserId(String phone, Long tenantId);
    void setUserUnionidByType(Long userId, String openId, String unionId, String code);
    Long getUserIdByTypeId(String code, String openId, String unionId, Long tenantId);
    void setImUserId(Long userId, String imUserId);
}
