package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.user.dto.ShareReportDTO;
import com.svnlan.user.vo.ShareReportVo;

import java.util.List;

/**
 * 分享资源举报 服务层
 *
 * @author lingxu 2023/04/06 09:45
 */
public interface ShareReportService {

    JSONObject shareReportListPage(ShareReportDTO dto);

    List<ShareReportVo> shareReportList(ShareReportDTO dto);

    void shareReportOperate(List<Long> ids, Integer operateType);

    void checkIfShareLinkPermit(ShareVo shareVo);

    void shareReport(ShareReportDTO dto);
}
