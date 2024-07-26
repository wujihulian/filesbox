package com.svnlan.user.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.Result;
import com.svnlan.user.dto.ShareReportDTO;
import com.svnlan.user.service.ShareReportService;
import com.svnlan.user.vo.ShareReportVo;
import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 分享资源举报 相关
 *
 * @author lingxu 2023/04/06 09:53
 */
@Slf4j
@RestController
@RequestMapping("api/shareAdmin/report")
public class ShareReportController {

    @Resource
    private ShareReportService shareReportService;

    /**
     * 分享链接举报列表
     */
    @PostMapping("page")
    public Result shareReportListPage(@RequestBody ShareReportDTO dto) {
        dto.initTimeRange();
        Assert.isTrue(!Objects.equals(dto.getStatus(), 2), "没有对应的状态");
        return Result.returnSuccess(shareReportService.shareReportListPage(dto));
    }

    /**
     * 对分享举报的操作
     */
    @PostMapping("operate")
    public Result shareReportOperate(@RequestBody JSONObject jsonObj) {
        JSONArray jsonArray = Optional.ofNullable(jsonObj.getJSONArray("ids"))
                .orElseThrow(() -> new IllegalArgumentException("ids 不能为空"));
        List<Long> ids = jsonArray.toJavaList(Long.class);
        Assert.notEmpty(ids, "ids 不能为空");

        Integer operateType = Optional.ofNullable(jsonObj.getInteger("operateType"))
                .orElseThrow(() -> new IllegalArgumentException("operateType,不能为空"));
        shareReportService.shareReportOperate(ids, operateType);
        return Result.returnSuccess();
    }


    /**
     * 导出举报列表
     */
    @SneakyThrows
    @PostMapping("export")
    public void exportShareList(@RequestBody ShareReportDTO dto, HttpServletResponse resp) {
        dto.initTimeRange();
        List<ShareReportVo> reportList = shareReportService.shareReportList(dto);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("举报管理", "sheet1"), ShareReportVo.class, reportList);

        // 设置下载的头信息 使用URLEncoder.encode是为了解决编码问题
        resp.setHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode("举报管理.xls", "utf-8"));

        ServletOutputStream os = resp.getOutputStream();
        // 用workbook对象直接写出输出流就可以
        workbook.write(os);
        workbook.close();
    }

}
