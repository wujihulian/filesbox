package com.svnlan.user.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.common.Result;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.user.dto.ShareDTO;
import com.svnlan.user.service.ShareService;
import com.svnlan.user.vo.ShareVo;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 后台分享管理
 *
 * @author lingxu 2023/04/04 13:26
 */
@RestController("adminShareController")
@RequestMapping("api/adminShare")
public class ShareController {

    @Resource
    private ShareService adminShareService;

    /**
     * 分享链接列表
     */
    @VisitRecord
    @PostMapping("page")
    public Result shareListPage(@RequestBody ShareDTO dto) {
        dto.initValue();
        return Result.returnSuccess(adminShareService.shareListPage(dto));
    }

    /**
     * 取消链接分享
     */
    @PostMapping("cancel")
    public Result cancelShare(@RequestBody JSONObject obj) {
        Object id = Optional.ofNullable(obj.getObject("id", Object.class)).orElseThrow(() -> new SvnlanRuntimeException("id 不能为空"));
        if (id instanceof Integer) {
            adminShareService.cancelShare(((Integer) id).longValue());
        } else if (id instanceof ArrayList) {
            ArrayList<Integer> idList = (ArrayList<Integer>) id;
            adminShareService.cancelShare(idList);
        }else{
            throw new IllegalArgumentException("参数有误");
        }
        return Result.returnSuccess();
    }

    /**
     * 导出链接列表
     */
    @SneakyThrows
    @PostMapping("export")
    public void exportShareList(@RequestBody ShareDTO dto, HttpServletResponse resp) {
        dto.initValue();
        List<ShareVo> shareVoPageList = adminShareService.shareList(dto);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("分享管理", "sheet1"), ShareVo.class, shareVoPageList);

        // 设置下载的头信息 使用URLEncoder.encode是为了解决编码问题
        resp.setHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode("分享管理.xls", "utf-8"));

        ServletOutputStream os = resp.getOutputStream();
        // 用workbook对象直接写出输出流就可以
        workbook.write(os);
        workbook.close();
    }
}
