//package com.lgren.springboot_mybatisplus_swagger.util;
//
//import com.alibaba.fastjson.JSONObject;
//import com.lgren.springboot_mybatisplus_swagger.common.CommResult;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.FileMagic;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PushbackInputStream;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ExcelReaderUtils {
//    private static DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
//    private static DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
//
//
//    /**
//     * 获取Excel文件数据
//     * @param inp
//     * @return
//     * @throws Exception
//     */
//    public static CommResult<List<JSONObject>> readWorkbook(InputStream inp, String... propertys) throws Exception {
//        List<JSONObject> list;
//        if (propertys == null || propertys.length < 1) {
//            return CommResult.newFailure("-1", "属性不能为空");
//        }
//        try {
//            if (!inp.markSupported()) {
//                inp = new PushbackInputStream(inp, 8);
//            }
//            InputStream newInp = FileMagic.prepareToCheckMagic(inp);
//            FileMagic fileMagic = FileMagic.valueOf(newInp);
//            if (fileMagic == FileMagic.OLE2) {
//                try {
//                    list = read2003Excel(newInp, propertys);
//                } catch (Exception e) {
//                    return CommResult.newFailure("-1", "读取失败！");
//                }
//            } else if (fileMagic == FileMagic.OOXML) {
//                try {
//                    list = read2007Excel(newInp, propertys);
//                } catch (Exception e) {
//                    return CommResult.newFailure("-1", "读取失败！");
//                }
//            } else {
//                return CommResult.newFailure("-1", "不支持的文件类型");
//            }
//        } finally {
//            inp.close();
//            inp = null;
//        }
//        return CommResult.newSuccess(list);
//    }
//
//    /**
//     * 读取 office 2003 excel
//     * @throws IOException IO异常
//     */
//    private static List<JSONObject> read2003Excel(InputStream is, String[] propertys) throws IOException {
//        List<JSONObject> list = new ArrayList<JSONObject>();
//        POIFSFileSystem fs = new POIFSFileSystem(is);
//        HSSFWorkbook hwb = new HSSFWorkbook(fs);
//        HSSFSheet sheet = hwb.getSheetAt(0);
//        Object value = null;
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        //第二行开始读
//        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
//            row = sheet.getRow(i);
//            if (row == null) {
//                continue;
//            }
//            JSONObject json = new JSONObject();
//            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//                cell = row.getCell(j);
//
//                if (cell == null) {
//                    continue;
//                }
//                switch (cell.getCellTypeEnum()) {
//                    case STRING:
//                        value = cell.getStringCellValue();
//                        break;
//                    case NUMERIC:
//                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
//                            value = df.format(cell.getNumericCellValue());
//                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
//                            value = nf.format(cell.getNumericCellValue());
//                        } else {
//                            value = cell.getNumericCellValue();
//                        }
//                        break;
//                    case BOOLEAN:
//                        value = cell.getBooleanCellValue();
//                        break;
//                    case BLANK:
//                        value = "";
//                        break;
//                    default:
//                        value = cell.toString();
//                }
//                if (value == null || StringUtils.isEmpty(value.toString())) {
//                    continue;
//                }
//                if (j < propertys.length) {
//                    json.put(propertys[j], value.toString().trim());
//                }
//            }
//            if (!"{}".equals(json.toJSONString())) {
//                list.add(json);
//            }
//        }
//        return list;
//    }
//
//    /**
//     * 读取Office 2007 excel
//     * @throws IOException IO异常
//     */
//    private static List<JSONObject> read2007Excel(InputStream is, String[] propertys) throws Exception {
//
//        List<JSONObject> list = new ArrayList<JSONObject>();
//        // 构造 XSSFWorkbook 对象
//        XSSFWorkbook xwb = new XSSFWorkbook(is);
//
//        // 读取第一章表格内容
//        XSSFSheet sheet = xwb.getSheetAt(0);
//        Object value = null;
//        XSSFRow row = null;
//        XSSFCell cell = null;
//        //第二行开始读
//        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
//            row = sheet.getRow(i);
//            if (row == null) {
//                continue;
//            }
//            JSONObject json = null;
//            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//                cell = row.getCell(j);
//                // 创建实例
//                if (cell == null) {
//                    continue;
//                }
//                switch (cell.getCellTypeEnum()) {
//                    case STRING:
//                        value = cell.getStringCellValue();
//                        break;
//                    case NUMERIC:
//                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
//                            value = df.format(cell.getNumericCellValue());
//                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
//                            value = nf.format(cell.getNumericCellValue());
//                        } else {
//                            value = cell.getNumericCellValue();
//                        }
//                        break;
//                    case BOOLEAN:
//                        value = cell.getBooleanCellValue();
//                        break;
//                    case BLANK:
//                        value = "";
//                        break;
//                    default:
//                        value = cell.toString();
//                }
//                if (value == null || StringUtils.isEmpty(value.toString())) {
//                    continue;
//                }
//                if (j < propertys.length) {
//                    if (json == null) {
//                        json = new JSONObject();
//                    }
//                    json.put(propertys[j], value.toString().trim());
//                }
//            }
//            if (json != null && !"{}".equals(json.toJSONString())) {
//                list.add(json);
//            }
//        }
//        return list;
//    }
//
//}
