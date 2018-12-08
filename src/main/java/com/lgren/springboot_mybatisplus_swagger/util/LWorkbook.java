package com.lgren.springboot_mybatisplus_swagger.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.util.Optional.ofNullable;


/**
 * 简化opi操作
 * @author Lgren
 * @create 2018-10-30 10:12
 **/
public class LWorkbook {
    private Workbook wb;

    //region Workbook方法区(部分方法略有改动)
    public Font createFont() {
        return wb.createFont();
    }

    public CellStyle createCellStyle() {
        return wb.createCellStyle();
    }

    public void write(OutputStream outputStream) throws IOException {
        wb.write(outputStream);
    }

    public void write(String name) throws IOException {
        name = ofNullable(name).orElse(String.valueOf(System.currentTimeMillis()));
        name = name.contains(".") ? name : (name + "." + (this.getWorkbook().getClass().equals(HSSFWorkbook.class) ? "xls" : "xlsx"));
        try (FileOutputStream fileOut = new FileOutputStream(name)) {
            wb.write(fileOut);
        } catch (IOException e) {
            throw e;
        }
    }
    //endregion

    //region 封装方法区
    public LFont font() {
        return new LFont(wb.createFont());
    }

    public LCellStyle cellStyle() {
        return new LCellStyle(wb.createCellStyle());
    }

    private LCellStyle cellStyle;

    public LCellStyle center() {
        if (cellStyle == null) {
            cellStyle = new LCellStyle(wb.createCellStyle());
            cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        }
        return cellStyle;
    }

    public LSheet sheet(String sheetName) {
        Objects.requireNonNull(sheetName);
        return new LSheet(ofNullable(wb.getSheet(sheetName)).orElseGet(() -> wb.createSheet(sheetName)));
    }

    //    public LRow row(String sheetName, int rowNum) {
    //        Objects.requireNonNull(sheetName);
    //        Sheet sheet = ofNullable(wb.getSheet(sheetName)).orElseGet(() -> wb.createSheet(sheetName));
    //        return new LRow(ofNullable(sheet.getRow(rowNum)).orElseGet(() -> sheet.createRow(rowNum)));
    //    }
    //endregion

    //region LWorkbook构造方法区
    public LWorkbook(Workbook wb) {
        this.wb = wb;
    }
    public static LWorkbook createXLS() {
        return new LWorkbook(new HSSFWorkbook());
    }
    public static LWorkbook createXLSX() {
        return new LWorkbook(new XSSFWorkbook());
    }
    public static LWorkbook createSXLSX(int rowAccessWindowSize) {
        return new LWorkbook(new SXSSFWorkbook(Math.min(rowAccessWindowSize, 100)));
    }
    //endregion

    //region get方法
    public Workbook getWorkbook() {
        return wb;
    }

    //endregion

    public class LSheet {
        private Sheet sheet;

        //region Sheet方法区(略有修改)
        public void addMergedRegion(CellRangeAddress region) {
            sheet.addMergedRegion(region);
        }

        public void setColumnWidth(int columnIndex, int width) {
            sheet.setColumnWidth(columnIndex, width);
        }

        public void autoSizeColumn(int[] column) {
            autoSizeColumn(false, column);
        }
        public void autoSizeColumn(int columnNum) {
            autoSizeColumn(false, columnNum);
        }

        public void autoSizeColumnByFirstCol() {
            short lastCellNum = ofNullable(this.row(0)).map(r -> r.getRow().getLastCellNum()).orElse((short) -1);
            if (lastCellNum > 0) {
                autoSizeColumnByFirstCol(false, lastCellNum);
            }
        }

        public void autoSizeColumn(boolean useMergedCells, int[] columnArr) {
            for (int i = 0; i < columnArr.length; i++) {
                autoSizeColumn(useMergedCells, columnArr[i]);
            }
        }

        public void autoSizeColumnByFirstCol(boolean useMergedCells, short lastCellNum) {
            for (int i = 0; i < lastCellNum; i++) {
                autoSizeColumn(useMergedCells, i);
            }
        }

        public void autoSizeColumn(boolean useMergedCells, int columnNum) {
            double width = getColumnWidth(sheet, columnNum, useMergedCells, sheet.getFirstRowNum(), sheet.getLastRowNum());
            if (width != -1.0D) {
                width *= 256.0D;
                int maxColumnWidth = '\uff00';
                if (width > (double) maxColumnWidth) {
                    width = (double) maxColumnWidth;
                }
                sheet.setColumnWidth(columnNum, (int) width);
            }
        }

        private double getColumnWidth(Sheet sheet, int column, boolean useMergedCells, int firstRow, int lastRow) {
            DataFormatter formatter = new DataFormatter();
            int defaultCharWidth = SheetUtil.getDefaultCharWidth(sheet.getWorkbook());

            double width = -1;
            for (int rowIdx = firstRow; rowIdx <= lastRow; ++rowIdx) {
                Row row = sheet.getRow(rowIdx);
                if (row != null) {
                    double cellWidth = getColumnWidthForRow(row, column, defaultCharWidth, formatter, useMergedCells);
                    width = Math.max(width, cellWidth);
                }
            }
            return width;
        }

        private double getColumnWidthForRow(Row row, int column, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells) {
            if (row == null) {
                return -1;
            }
            Cell cell = row.getCell(column);
            if (cell == null) {
                return -1;
            }
            //region 获取中文字符长度
            int cnNum = cell.toString().replaceAll("[^\\u4e00-\\u9fa5]", "").length();
            //endregion
            return SheetUtil.getCellWidth(cell, defaultCharWidth, formatter, useMergedCells) + cnNum * 0.931818;
        }
        //endregion

        //region 封装方法区
        public LRow row(int rowNum) {
            return new LRow(ofNullable(sheet.getRow(rowNum)).orElseGet(() -> sheet.createRow(rowNum)));
        }

        public LCell cell(int rowNum, int cellNum) {
            Row row = ofNullable(sheet.getRow(rowNum)).orElseGet(() -> sheet.createRow(rowNum));
            return new LCell(ofNullable(row.getCell(cellNum)).orElseGet(() -> row.createCell(cellNum)));
        }
        //endregion

        //region LSheet构造方法区
        public LSheet(Sheet sheet) {
            this.sheet = sheet;
        }
        //endregion

        //region get方法区
        public Sheet getSheet() {
            return sheet;
        }
        //endregion
    }

    public class LRow {
        private Row row;

        //region 构造方法区
        public LRow(Row row) {
            this.row = row;
        }
        //endregion

        //region Row方法区
        public LRow setRowStyle(CellStyle style) {
            row.setRowStyle(style);
            return this;
        }

        public LRow setRowStyle(LCellStyle style) {
            row.setRowStyle(style.getCellStyle());
            return this;
        }
        //endregion

        //region 封装方法区
        public LCell cell(int cellNum) {
            return new LCell(ofNullable(row.getCell(cellNum)).orElseGet(() -> row.createCell(cellNum)));
        }
        //endregion

        //region get方法区
        public Row getRow() {
            return row;
        }
        //endregion
    }

    public class LCell {
        private Cell cell;

        //region Cell方法区
        public LCell setCellValue(double value) {
            cell.setCellValue(value);
            return this;
        }

        public LCell setCellValue(Date value) {
            cell.setCellValue(value);
            return this;
        }

        public LCell setCellValue(Calendar value) {
            cell.setCellValue(value);
            return this;
        }

        public LCell setCellValue(RichTextString value) {
            cell.setCellValue(value);
            return this;
        }

        public LCell setCellValue(String value) {
            cell.setCellValue(value);
            return this;
        }

        public LCell setCellValue(boolean value) {
            cell.setCellValue(value);
            return this;
        }

        public LCell setCellStyle(CellStyle style) {
            cell.setCellStyle(style);
            return this;
        }

        public LCell setCellStyle(LCellStyle style) {
            cell.setCellStyle(style.getCellStyle());
            return this;
        }
        //endregion

        //region 构造方法区
        public LCell(Cell cell) {
            this.cell = cell;
        }
        //endregion

        //region get方法区
        public Cell getCell() {
            return cell;
        }
        //endregion
    }

    public class LFont {
        private Font font;

        //region 构造方法区
        public LFont(Font font) {
            this.font = font;
        }
        //endregion

        //region Font方法区(略有改动)
        public LFont setColor(short color) {
            font.setColor(color);
            return this;
        }

        public LFont setBold(boolean bold) {
            font.setBold(bold);
            return this;
        }

        public LFont setFontName(String name) {
            font.setFontName(name);
            return this;
        }

        public LFont setFontHeightInPoints(short height) {
            font.setFontHeightInPoints(height);
            return this;
        }
        //endregion

        //region get方法区
        public Font getFont() {
            return font;
        }
        //endregion
    }

    public class LCellStyle {
        private CellStyle cellStyle;

        //region CellStyle方法区(略有改动)
        public LCellStyle setFont(Font font) {
            cellStyle.setFont(font);
            return this;
        }

        public LCellStyle setFont(LFont font) {
            cellStyle.setFont(font.getFont());
            return this;
        }

        public LCellStyle setFillForegroundColor(short fillForegroundColor) {
            cellStyle.setFillForegroundColor(fillForegroundColor);
            return this;
        }

        public LCellStyle setFillPattern(FillPatternType fillPattern) {
            cellStyle.setFillPattern(fillPattern);
            return this;
        }

        public LCellStyle setAlignment(HorizontalAlignment alignment) {
            cellStyle.setAlignment(alignment);
            return this;
        }

        public LCellStyle setVerticalAlignment(VerticalAlignment verticalAlignment) {
            cellStyle.setVerticalAlignment(verticalAlignment);
            return this;
        }

        public LCellStyle setBorderBottom(BorderStyle borderBottom) {
            cellStyle.setBorderBottom(borderBottom);
            return this;
        }

        public LCellStyle setBorderTop(BorderStyle borderTop) {
            cellStyle.setBorderTop(borderTop);
            return this;
        }

        public LCellStyle setBorderRight(BorderStyle borderRight) {
            cellStyle.setBorderRight(borderRight);
            return this;
        }

        public LCellStyle setBorderLeft(BorderStyle borderLeft) {
            cellStyle.setBorderLeft(borderLeft);
            return this;
        }
        //endregion

        public LCellStyle center() {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            return this;
        }

        //region 构造方法区
        public LCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
        }
        //endregion


        //region get方法区
        public CellStyle getCellStyle() {
            return cellStyle;
        }
        //endregion


    }

    //region 测试
    /*public static void main(String[] args) {
        LWorkbook lxwb = new LWorkbook(LWorkbook.XLSX);
        //region 字体样式
        LWorkbook.LFont font = lxwb.font()
                .setColor(HSSFFont.COLOR_RED)// 设置字体,红色
                .setBold(true)// 字体粗体显示
                .setFontName("宋体")// 字体类型
                .setFontHeightInPoints((short) 22);// 字体大小
        //endregion

        //region 设置样式
        LWorkbook.LCellStyle cellStyle = lxwb.cellStyle()
                .setFont(font)
                //设置单元格背景色
                .setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex())
                .setFillPattern(FillPatternType.SOLID_FOREGROUND)
                //设置居中
                .setAlignment(HorizontalAlignment.CENTER)//水平居中
                .setVerticalAlignment(VerticalAlignment.CENTER)//垂直居中
                //设置边框
                .setBorderBottom(BorderStyle.THIN) //下边框
                .setBorderLeft(BorderStyle.THIN)//左边框
                .setBorderTop(BorderStyle.THIN)//上边框
                .setBorderRight(BorderStyle.THIN);//右边框
        //endregion

        lxwb.row("one", 10).setRowStyle(cellStyle);
        lxwb.cell("one", 11, 0).setCellStyle(cellStyle);

        //region 赋值
        int[] rowArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] cellArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int rowNum : rowArr) {
            for (int cellNum : cellArr) {
                lxwb.cell("one", rowNum, cellNum).setCellValue("测试一下吧1");
            }
        }
        //endregion

        lxwb.sheet("one").autoSizeColumn(cellArr);
//        lxwb.sheet("one").addMergedRegion(new CellRangeAddress(10, 10, 12, 12));


        try {
            lxwb.write("e:\\测试");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    //endregion
}
