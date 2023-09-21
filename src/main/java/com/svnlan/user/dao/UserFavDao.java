package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.home.domain.UserFav;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.home.vo.UserFavVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:06
 */
public interface UserFavDao {

    int insert(UserFav userFav);
    int batchInsert(List<UserFav> list);
    Integer getFavMaxSort(Long userID);
    int removeUserFav(@Param("userID") Long userID, @Param("list") List<Long> list);
    List<String> checkIsFav(List<String> list);
    List<String> checkIsFavByUserId(Long userID);
    int removeFileTag(@Param("tagID") Integer tagID, @Param("list") List<String> list);
    List<UserFavVo> getFileTagByUserId(Long userID);
    List<UserFavVo> getFileTagBySourceID(@Param("userID") Long userID, @Param("list") List<String> list);
    int removeUserTag(@Param("tagID") Integer tagID, @Param("userID") Long userID);
    int removeInfoTag(@Param("tagID") Integer tagID);
    int removeInfoTagByID(@Param("path") String path);

    /** 收藏夹排序 **/
    int addSortAll(@Param("userID") Long userID);
    int subtractSortAll(@Param("userID") Long userID);
    int updateFavSort(@Param("userID") Long userID, @Param("path") String path, @Param("sort") int sort);


    Integer getTagMaxSort(Long userID);
    /** 标签排序 **/
    int addTagSortAll(@Param("userID") Long userID);
    int subtractTagSortAll(@Param("userID") Long userID);
    int updateTagSort(@Param("userID") Long userID, @Param("path") String path, @Param("sort") int sort);
    int removeUserFavByIdList(@Param("userID") Long userID, @Param("list") List<Long> list);

    int updateFavName(@Param("id") Long id, @Param("name") String name);
    List<String> getFavNameList(@Param("userID") Long userID);
    List<CommonLabelVo> geTagListByInfoID(@Param("path") String path);

//    @Select("SELECT CAST(path AS SIGNED) sourceId FROM user_fav WHERE userID = #{userId} AND tagID = 0 AND type = 'source'")
    List<JSONObject> selectFavorSourceId(@Param("userId") Long userId, @Param("name") String name);

}
