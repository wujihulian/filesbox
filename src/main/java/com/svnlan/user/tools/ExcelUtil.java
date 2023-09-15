package com.svnlan.user.tools;


import com.svnlan.user.vo.ExcelVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: Excel的导入导出工具
 * @Date:
 */
public class ExcelUtil<T> {
    private static final Logger logger = LoggerFactory.getLogger("error");
    Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param sheetSize 每个sheet中数据的行数,此数值必须小于65536
     * @param output    java输出流
     */
    public boolean exportExcel(List<T> list, String sheetName, int sheetSize, OutputStream output) {
        // 得到所有定义字段
        Field[] allFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            //制定的注解在属性上
            if (field.isAnnotationPresent(ExcelVO.class)) {
                fields.add(field);
            }
        }

        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();

        // excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
        if (sheetSize >= 65535 || sheetSize < 1) {
            sheetSize = 65535;
        }
        // 取出一共有多少个sheet
//        double sheetNo = Math.ceil(list.size() / sheetSize);
        BigDecimal listSize = new BigDecimal(list.size());
        BigDecimal sheetSizeBd = new BigDecimal(sheetSize);
        BigDecimal sheetNoBd = listSize.divide(sheetSizeBd, 0, BigDecimal.ROUND_CEILING);
        int sheetNo = sheetNoBd.intValue();
        if (sheetNo == 0){
            sheetNo = 1;
        }
        for (int index = 0; index < sheetNo; index++) {
            logger.info("exportExcel sheetIndex {} , size {}", index, list.size());
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            // 设置工作表的名称.
            workbook.setSheetName(index, sheetName + index);
            HSSFRow row;
            HSSFCell cell;// 产生单元格

            row = sheet.createRow(0);// 产生一行
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelVO attr = field.getAnnotation(ExcelVO.class);
                //不需要导出 则continue
                if (!attr.isExport()) {
                    continue;
                }
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                cell.setCellType(Cell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
                cell.setCellValue(attr.name());// 写入列名
                //写入列宽
                sheet.setColumnWidth(i, attr.width());
            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            // 写入各条记录,每条记录对应excel表中的一行
            if (list.size() > 0) {
                for (int i = startNo; i < endNo; i++) {
                    row = sheet.createRow(i + 1 - startNo);
                    T vo = list.get(i); // 得到导出对象.
                    for (int j = 0; j < fields.size(); j++) {
                        Field field = fields.get(j);// 获得field.
                        field.setAccessible(true);// 设置实体类私有属性可访问
                        ExcelVO attr = field.getAnnotation(ExcelVO.class);
                        try {
                            // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                            if (attr.isExport()) {
                                cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                cell.setCellValue(field.get(vo) == null ? "" : String.valueOf(field
                                        .get(vo)));// 如果数据存在就填入,不存在填入空格.

                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        try {
            output.flush();
            logger.info("exportExcel size {}, workbook sheetNumber {}", list.size(), workbook.getNumberOfSheets());
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet         要设置的sheet.
     * @param promptTitle   标题
     * @param promptContent 内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle,
                                          String promptContent, int firstRow, int endRow,
                                          int firstCol, int endCol) {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow,
                                              int endRow, int firstCol, int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * 返回一个Class类型的解析Map
     *
     * @param clazz
     * @return
     */
    public static <T> Map<String, Field> getFields(Class<T> clazz) {
        Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
        Map<String, Field> fieldsMap = new HashMap<>();// 定义一个map用于存放列的序号和field.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVO.class)) {
                ExcelVO attr = field.getAnnotation(ExcelVO.class);
                String col = attr.column();
                field.setAccessible(true);// 设置类的私有字段属性可访问.
                fieldsMap.put(col, field);
            }
        }
        return fieldsMap;
    }

    /**
     * 从一个Class的解析Map里返回某列的name属性
     *
     * @param fieldsMap
     * @param columnId
     * @return
     */
    public static String getColumnName(Map<String, Field> fieldsMap, String columnId) {
        Field f = fieldsMap.get(columnId);
        if (f.isAnnotationPresent(ExcelVO.class)) {
            ExcelVO attr = f.getAnnotation(ExcelVO.class);
            return attr.name();
        }
        return "";
    }


}
