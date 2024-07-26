package com.svnlan.user.dao;

import com.svnlan.user.domain.ShareReport;
import com.svnlan.user.dto.ShareReportDTO;

import java.util.List;

/**
 * @author lingxu
 * @description 针对表【share_report】的数据库操作Mapper
 * @createDate 2023-05-26 13:59:33
 * @Entity com.svnlan.user.domain.ShareReportCopy
 */
public interface ShareReportDao {

    Long selectCount(ShareReportDTO dto);

    List<ShareReport> selectList(ShareReportDTO dto);

    List<ShareReport> selectListByIds(List<Long> ids);

    int insert(ShareReport shareReport);
}




