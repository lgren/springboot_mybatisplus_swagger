package com.lgren.springboot_mybatisplus_swagger.util;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 后端网站工具
 * @author Lgren
 * @create 2018-11-28 8:51
 **/
public class WebUtil {
    public static void exportExcel(Workbook wb, String fileName, HttpServletResponse response) {
        // 清空response
        response.reset();
        if (!fileName.contains(".xls") && !fileName.contains(".xlsx")) {
            fileName = fileName + (wb instanceof XSSFWorkbook || wb instanceof SXSSFWorkbook ? ".xlsx" : ".xls");
        }
        if (wb instanceof HSSFWorkbook) {
            response.setHeader("content-Type", "applicatio/vnd.ms-excel");
        } else if (wb instanceof XSSFWorkbook || wb instanceof SXSSFWorkbook) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        } else {
            return;
        }
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (OutputStream os = response.getOutputStream()) {
            wb.write(os);
            os.flush();
            os.close();
        } catch (ClientAbortException e) {
            System.out.println("远程主机强迫关闭了一个现有的连接。");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
