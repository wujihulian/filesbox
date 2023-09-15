package com.svnlan.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.svnlan.user.domain.ShareReport;
import com.svnlan.user.dto.ShareReportDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lingxu
 * @description 针对表【share_report】的数据库操作Mapper
 * @createDate 2023-05-26 13:59:33
 * @Entity com.svnlan.user.domain.ShareReportCopy
 */
public interface ShareReportDao extends BaseMapper<ShareReport> {

    IPage<ShareReport> selectPage(IPage<ShareReport> page, @Param("dto") ShareReportDTO dto);

    List<ShareReport> selectList(@Param("dto") ShareReportDTO dto);

    List<ShareReport> selectListByIds(@Param("ids") List<Long> ids);
}




