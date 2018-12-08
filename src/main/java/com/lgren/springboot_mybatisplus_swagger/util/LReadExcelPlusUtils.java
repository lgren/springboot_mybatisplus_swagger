package com.lgren.springboot_mybatisplus_swagger.util;

import com.lgren.springboot_mybatisplus_swagger.common.CommResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;


public class LReadExcelPlusUtils {
    private static DecimalFormat df = new DecimalFormat("0");// 格式化 number String 字符
    private static DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字


//    /**
//     * 获取Excel文件数据 第一个sheet
//     * @param inp excel流
//     */
//    @NonNull
//    public static CommResult<Map<String, Map<Object, Map<Object, Object>>>> readWorkbook(InputStream inp) throws IOException {
//        CommResult<Workbook> wbResult = getWorkbook(inp);
//        if (wbResult.isSuccess()) {
//            Map<String, Map<Object, Map<Object, Object>>> map = getWorkbookValue(wbResult.getData());
//            return CommResult.newSuccess(map).setErrorMsg(wbResult.getErrorMsg());
//        }
//        return CommResult.newFailure(wbResult.getErrorCode(), wbResult.getErrorMsg());
//    }

    /** 通过输入流inp获取Workbook */
    @NonNull
    public static CommResult<Workbook> getWorkbook(InputStream inp) throws IOException {
        Workbook workbook;
        CommResult<Workbook> result = new CommResult<>();
        try (InputStream mpbinp = inp.markSupported() ? inp : new PushbackInputStream(inp, 8);
                InputStream newInp = FileMagic.prepareToCheckMagic(mpbinp)){
            FileMagic fileMagic = FileMagic.valueOf(newInp);
            if (fileMagic == FileMagic.OLE2) {
                try {
                    POIFSFileSystem fs = new POIFSFileSystem(newInp);
                    workbook = new HSSFWorkbook(fs);
                    result.setErrorMsg("xls");
                } catch (Exception e) {
                    return result.setErrorCode("-1").setErrorMsg("读取失败！");
                }
            } else if (fileMagic == FileMagic.OOXML) {
                try {
                    workbook = new XSSFWorkbook(newInp);
                    result.setErrorMsg("xlsx");
                } catch (Exception e) {
                    return result.setErrorCode("-1").setErrorMsg("读取失败！");
                }
            } else {
                return result.setErrorCode("-1").setErrorMsg("不支持的文件类型！");
            }
        }
        return result.setSuccess(true).setData(workbook);
    }

    //region 获取全部数据
    /** 获取workbook下的所有cell的数据 通过sheet分组 */
    @Nullable
    public static Map<String, Map<Object, Map<Object, Object>>> getWorkbookValue(Workbook workbook, boolean colFirstToKey, boolean rowFirstToKey) {
        return ofNullable(workbook).map(wb -> {
            Map<String, Map<Object, Map<Object, Object>>> allFile = new LinkedHashMap<>(wb.getNumberOfSheets());
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                Map<Object, Map<Object, Object>> sheetMap = getSheetValue(sheet, colFirstToKey, rowFirstToKey);
                if (sheetMap != null && !sheetMap.isEmpty()) {
                    allFile.put(sheet.getSheetName(), getSheetValue(sheet, colFirstToKey, rowFirstToKey));
                }
            }
            return allFile;
        }).orElse(null);
    }

    /** 获取sheet下的所有cell的数据 通过row分组 */
    @Nullable
    public static Map<Object, Map<Object, Object>> getSheetValue(Sheet sheet, boolean colFirstToKey, boolean rowFirstToKey) {
        return ofNullable(sheet).map(s -> {
            Map<Object, Map<Object, Object>> result = new LinkedHashMap<>(s.getPhysicalNumberOfRows());
            int firstRowNum = s.getFirstRowNum();
            boolean rowFirstToKeyStart = ofNullable(s.getRow(firstRowNum))
                    .map(r -> rowFirstToKey  && (s.getLastRowNum() > firstRowNum) && r.getLastCellNum() > r.getFirstCellNum())
                    .orElse(false);
            Object key = ofNullable(s.getRow(firstRowNum)).filter(r -> rowFirstToKeyStart).map(r -> getCellValue(r.getCell(r.getFirstCellNum()))).orElse(firstRowNum);
            for (int j = firstRowNum + (rowFirstToKeyStart ? 1 : 0); j <= s.getLastRowNum(); j++) {
                Row row = s.getRow(j);
                Map<Object, Object> cellMap = getRowValue(row, colFirstToKey);
                if (cellMap != null && !cellMap.isEmpty()) {
                    result.put(key, cellMap);
                }
            }
            return result;
        }).orElse(null);
    }

    /** 获取sheet下的所有cell的数据 通过row分组 */
    @Nullable
    public static Map<Object, Map<Object, Object>> getSheetValueByCol(Sheet sheet, boolean rowFirstToKey) {
        return ofNullable(sheet).map((Sheet s) -> {
            int firstRowNum = s.getFirstRowNum();
            Map<Object, Map<Object, Object>> result = new LinkedHashMap<>(s.getRow(firstRowNum).getPhysicalNumberOfCells());
            boolean rowFirstToKeyStart = ofNullable(s.getRow(firstRowNum))
                    .map(r -> rowFirstToKey  && (s.getLastRowNum() > firstRowNum) && r.getLastCellNum() > r.getFirstCellNum())
                    .orElse(false);
            Object key = ofNullable(s.getRow(firstRowNum)).filter(r -> rowFirstToKeyStart).map(r -> getCellValue(r.getCell(r.getFirstCellNum()))).orElse(firstRowNum);
            for (int j = firstRowNum + (rowFirstToKeyStart ? 1 : 0); j <= s.getLastRowNum(); j++) {
                Row row = s.getRow(j);
                if (row == null) {
                    continue;
                }
                for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                    Map<Object, Object> cellMap = result.get(k);
                    boolean flag = cellMap == null;
                    if (flag) {
                        cellMap = new LinkedHashMap<>(sheet.getPhysicalNumberOfRows());
                        result.put(k, cellMap);
                    }
                    Cell cell = row.getCell(k);
                    Object cellValue = getCellValue(cell);
                    if (cellValue != null) {
                        cellMap.put(key, getCellValue(cell));
                    }
                    // 如果到最后一次了
                    if (j == (s.getLastRowNum())) {
                        if (cellMap.isEmpty()) {
                            result.remove(k);
                        }
                    }
                }
            }
            return result;
        }).orElse(null);
    }

    /** 获取row下的所有cell的数据 */
    @Nullable
    public static Map<Object, Object> getRowValue(Row row, boolean colFirstToKey) {
        return ofNullable(row).map(r -> {
            Map<Object, Object> cellMap = new LinkedHashMap<>(r.getPhysicalNumberOfCells());
            short firstCellNum = r.getFirstCellNum();
            boolean colFirstToKeyStart = colFirstToKey && (r.getLastCellNum() > firstCellNum);
            Object key = colFirstToKeyStart ? r.getCell(firstCellNum) : firstCellNum;
            for (int k = firstCellNum + (colFirstToKeyStart ? 1 : 0); k < r.getLastCellNum(); k++) {
                Cell cell = r.getCell(k);
                Object cellValue = getCellValue(cell);
                if (cellValue != null) {
                    cellMap.put(key, getCellValue(cell));
                }
            }
            return cellMap;
        }).orElse(null);
    }

    /** 获取cell下的数据 */
    @Nullable
    public static Object getCellValue(Cell cell) {
        return ofNullable(cell).map(c -> {
            Object result;
            switch (c.getCellTypeEnum()) {
                case STRING:
                    result = c.getStringCellValue();
                    break;
                case NUMERIC:
                    if ("@".equals(c.getCellStyle().getDataFormatString())) {
                        result = df.format(c.getNumericCellValue());
                    } else if ("General".equals(c.getCellStyle().getDataFormatString())) {
                        result = nf.format(c.getNumericCellValue());
                    } else {
                        result = c.getNumericCellValue();
                    }
                    break;
                case BOOLEAN:
                    result = c.getBooleanCellValue();
                    break;
                case BLANK:
                    result = "";
                    break;
                default:
                    result = c.toString();
            }
            return result;
        }).orElse(null);
    }
    //endregion

    //region 特殊需求的获取值
    // 1.获取一个sheet(通过sheet编号/名称) getSheetValue
    // 2.获取一行的值 getRowValue
    // 3.获取一列的值 getColValue
    // 4.获取一个值 getCellValue

    /** 获取workbook下的第sheetIndex个sheet的所有cell的数据 通过row分组 */
    @Nullable
    public static Map<Object, Map<Object, Object>> getSheetValue(Workbook workbook, int sheetIndex, boolean colFirstToKey, boolean rowFirstToKey) {
        // 如果 sheetIndex大于workbook中sheet的总数 则返回null
        return ofNullable(workbook).filter(wb -> sheetIndex < wb.getNumberOfSheets())
                // 否则输出对应的Sheet下所有Cell的值
                .map(wb -> getSheetValue(wb.getSheetAt(sheetIndex), colFirstToKey, rowFirstToKey)).orElse(null);
    }

    /** 获取workbook下的名称叫做sheetName的sheet的所有cell的数据 通过row分组 */
    @Nullable
    public static Map<Object, Map<Object, Object>> getSheetValue(Workbook workbook, String sheetName, boolean colFirstToKey, boolean rowFirstToKey) {
        return ofNullable(workbook).map(wb -> getSheetValue(wb.getSheet(sheetName), colFirstToKey, rowFirstToKey)).orElse(null);
    }

    /** 获取sheet下的第rowIndex行的所有cell的数据 */
    @Nullable
    public static Map<Object, Object> getRowValue(Sheet sheet, int rowIndex, boolean colFirstToKey) {
        // 如果 rowIndex大于sheet的最后边的row 则返回null
        return ofNullable(sheet).filter(s -> rowIndex <= s.getLastRowNum())
                // 否则输出对应的Row下所有Cell的值
                .map(s -> getRowValue(s.getRow(rowIndex), colFirstToKey)).orElse(null);
    }

    /** 获取sheet下的第colIndex列的所有cell的数据 */
    @Nullable
    public static Map<Object, Object> getColValue(Sheet sheet, int colIndex, boolean rowFirstToKey) {
        return ofNullable(sheet).map(s -> {
            // 否则输出所有cell的值
            Map<Object, Object> result = null;
            int firstRowNum = s.getFirstRowNum();
            boolean rowFirstToKeyStart = ofNullable(s.getRow(firstRowNum))
                    .map(r -> rowFirstToKey  && (s.getLastRowNum() > firstRowNum) && r.getLastCellNum() > r.getFirstCellNum())
                    .orElse(false);
            Object key = ofNullable(s.getRow(firstRowNum)).filter(r -> rowFirstToKeyStart).map(r -> getCellValue(r.getCell(r.getFirstCellNum()))).orElse(firstRowNum);
            for (int j = firstRowNum + (rowFirstToKeyStart ? 1 : 0); j <= s.getLastRowNum(); j++) {
                Row row = s.getRow(j);
                if (row == null) {
                    continue;
                }
                // 如果 colIndex 不在对应 row的cell范围内 则输出null
                if (colIndex < row.getFirstCellNum() ||  colIndex > row.getLastCellNum() - 1) {
                    break;
                }
                result = ofNullable(result).orElse(new LinkedHashMap<>(s.getPhysicalNumberOfRows()));
                Object cellValue = getCellValue(s.getRow(j), colIndex);
                if (cellValue != null) {
                    result.put(key, getCellValue(s.getRow(j), colIndex));
                }
            }
            return result;
        }).orElse(null);
    }

    /** 获取sheet下的第cellIndex行第colIndex列的cell的数据 */
    @Nullable
    public static Object getCellValue(Sheet sheet, int rowIndex, int colIndex) {
        // 如果 rowIndex大于sheet的最后边的row 则返回null
        return ofNullable(sheet).filter(s -> rowIndex <= s.getLastRowNum())
                // 否则输出对应的Cell的值
                .map(s -> getCellValue(s.getRow(rowIndex), colIndex)).orElse(null);
    }

    /** 获取row下的第cellIndex行的cell的数据 */
    @Nullable
    public static Object getCellValue(Row row, int cellIndex) {
        // 如果 cellIndex大于row的最后边的cell 则返回null
        return ofNullable(row).filter(r -> cellIndex < r.getLastCellNum())
                // 否则输出对应的Cell的值
                .map(r -> getCellValue(r.getCell(cellIndex))).orElse(null);
    }
    //endregion

}
