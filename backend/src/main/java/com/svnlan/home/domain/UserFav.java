package com.svnlan.home.domain;

import com.svnlan.jooq.tables.pojos.UserFavModel;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/20 10:20
 */
@Data
public class UserFav extends UserFavModel {

    public UserFav() {
    }

    public UserFav(Long userID, Integer tagID, String name, String path, String type, Integer sort) {
        setUserId(userID);
        setTagId(tagID);
        setName(name);
        setPath(path);
        setType(type);
        setSort(sort);
    }


}
