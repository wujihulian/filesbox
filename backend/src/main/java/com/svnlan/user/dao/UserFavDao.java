package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.home.vo.UserFavVo;
import com.svnlan.jooq.tables.pojos.UserFavModel;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:06
 */
public interface UserFavDao {

    int insert(UserFavModel userFav);

    int batchInsert(List<UserFavModel> list);

    Integer getFavMaxSort(Long userID);

    int removeUserFav(Long userID, List<Long> list);

    List<String> checkIsFavByUserId(Long userID);

    int removeFileTag(Integer tagID, List<String> list);

    List<UserFavVo> getFileTagByUserId(Long userID);

    List<UserFavVo> getFileTagBySourceID(Long userID, List<String> list);

    int removeUserTag(Integer tagID, Long userID);

    int removeInfoTag(Integer tagID);

    int removeInfoTagByID(String path);

    /**
     * 收藏夹排序
     **/
    int addSortAll(Long userID);

    int subtractSortAll(Long userID);

    int updateFavSort(Long userID, String path, int sort);


    Integer getTagMaxSort(Long userID);

    /**
     * 标签排序
     **/
    int addTagSortAll(Long userID);

    int subtractTagSortAll(Long userID);

    int updateTagSort(Long userID, String path, int sort);

    int removeUserFavByIdList(Long userID, List<Long> list);

    int updateFavName(Long id, String name);

    List<String> getFavNameList(Long userID);

    List<CommonLabelVo> geTagListByInfoID(String path);

    //    @Select("SELECT CAST(path AS SIGNED) sourceID FROM user_fav WHERE userID = #{userId} AND tagID = 0 AND type = 'source'")
    List<JSONObject> selectFavorSourceId(Long userId, String name);

}
