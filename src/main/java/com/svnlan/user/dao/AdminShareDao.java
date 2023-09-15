package com.svnlan.user.dao;

import com.svnlan.user.dto.ShareDTO;
import com.svnlan.user.vo.ShareVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingxu
 * @description 针对表【share】的数据库操作Mapper
 * @createDate 2023-04-04 14:52:31
 * @Entity com.svnlan.user.vo.ShareVo
 */
public interface AdminShareDao {

    List<ShareVo> shareList(@Param("dto") ShareDTO dto, @Param("needAll") boolean needAll);

    Integer shareListCount(@Param("dto") ShareDTO dto);

    int cancelShare(@Param("id") Long id);

    void cancelMultiShare(@Param("ids") List<Long> ids);

    ShareVo getById(@Param("id")  Long id);

    List<ShareVo> getByIds(@Param("ids") ArrayList<Integer> idList);
}




