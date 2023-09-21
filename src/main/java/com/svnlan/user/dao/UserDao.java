package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.user.domain.User;
import com.svnlan.user.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:13
 */
public interface UserDao extends BaseMapper<User> {

    int insert(User user);
    int update(User user);

    UserVo getUserInfo(Long userID);

    List<UserVo> getUserListByParam(Map<String, Object> map);
    int updateUserState(@Param("list") List<Long> list, @Param("status") Integer status);
    int updateNickname(@Param("nickname") String nickname, @Param("userID") Long userID);
    int updateSex(@Param("sex") String sex, @Param("userID") Long userID);
    int updateAvatar(@Param("avatar") String avatar, @Param("userID") Long userID);
    int updateAvatarList(@Param("list") List<Long> list, @Param("avatar") String avatar);
    int updateEmail(@Param("email") String email, @Param("userID") Long userID);
    int updatePassword(@Param("password") String password, @Param("userID") Long userID);
    int findByEmail(@Param("email") String email);
    int findByNickname(@Param("nickname") String nickname);
    UserVo getUserSimpleInfo(Long userID);
    List<UserVo> findByName(@Param("name") String name);
    List<UserVo> getUserBaseInfo(List<Long> list);
    UserVo getUserBaseOneInfo(Long userID);

    @Select("SELECT SUM(sizeUse) FROM user")
    Long sumUserSpaceUsed();

    @Select("SELECT count(1) FROM user WHERE `status` = 1 AND is_system = 0")
    Long selectUserCount();

    List<UserVo> findByPhone(@Param("phone") String phone);
    int updatePhone(@Param("phone") String phone, @Param("userID") Long userID);

    @Select("SELECT password from `user` where name = #{username} AND `status` != 2")
    String getPasswordByUserName(@Param("username") String username);

    @Select("SELECT userID, name, password, status, sex from `user` where name = #{username}")
    UserVo getUserByUserName(@Param("username") String username);

    List<UserVo> queryUserInfoByOpenIdOrMobile(@Param("openId")String openId, @Param("mobile")String mobile, @Param("code") String code);

    @Select("SELECT userID, wechatOpenId, enWechatOpenId, avatar, nickname,phone,email, `status` FROM `user` WHERE userID =#{userId}")
    UserVo queryUserByUserId(@Param("userId") Long userId);

    @Update("UPDATE user SET ${value}OpenId = NULL WHERE userID = #{userID} AND ${value}OpenId IS NOT NULL")
    int removeOpenId(Long userID, String value);
    List<UserVo> getUserSimpleInfoByIds(@Param("userIds") Collection<Long> userIds);

    @Select("SELECT createTime FROM `user` WHERE userID = #{userId}")
    Long getCreateTime(@Param("userId") Long userId);

    List<Long> getUserIdByRoleId(@Param("roleIdList")  List<Long> roleIdList);

    @Select("SELECT userID AS uid, ifnull(if(length(nickname)>0,nickname,null),name) n, avatar a, sizeMax sm, sizeUse s " +
            "FROM user ORDER BY s DESC LIMIT #{topCount}")
    List<JSONObject> getUserSpaceRank(Integer topCount);
}
