package com.lgren.springboot_mybatisplus_swagger.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Stopwatch;
import com.lgren.springboot_mybatisplus_swagger.common.CommResult;
import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;
import com.lgren.springboot_mybatisplus_swagger.service.ITeacherService;
import com.lgren.springboot_mybatisplus_swagger.util.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

/**
 * <p>
 * 前端控制器
 * </p>
 * @author Lgren
 * @since 2018-11-14
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
    //region 自动注入区域
    private final ITeacherService teacherService;

    @Autowired
    public TeacherController(ITeacherService teacherService) {this.teacherService = teacherService;}

    @Resource
    private HttpServletResponse response;
    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpSession session;
    //endregion

    @GetMapping("toExport")
    public String toExport(Map<String, Object> map) {
        map.put("test", "tes111t");
        return "teacher/export";
    }

    @PostMapping("upload.do")
    @ResponseBody
    public CommResult<String> uploadDo(MultipartFile userFile) throws Exception {
        InputStream is = userFile.getInputStream();
        String fileName = userFile.getOriginalFilename();
        LWorkbook wb = null;
        BufferedReader reader = null;
        if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
//            CommResult<Map<String, Map<Integer, List<Object>>>> result = LReadExcelUtils.readWorkbook(is);
            CommResult<Workbook> workbook = LReadExcelUtils.getWorkbook(is);
            LReadExcelPlusUtils.getWorkbookValue(workbook.getData(), true, true);
            LReadExcelPlusUtils.getColValue(workbook.getData().getSheetAt(0), 0, true);
            Map<Object, Map<Object, Object>> sheetColMap = LReadExcelPlusUtils.getSheetValueByCol(workbook.getData().getSheetAt(0), true);

            System.out.println();
        } else {
            reader = new BufferedReader(new InputStreamReader(is));
            reader.readLine();
        }

        return CommResult.newSuccess("success");
    }

    @GetMapping("allTeacherList.do")
    @ResponseBody
    public List<Teacher> selectAllTeacher() {
        List<Teacher> teacherList = teacherService.list(null);
        return teacherList;
    }

    @ApiOperation(value = "新增老师", notes = "新增老师notes", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "学生用户名", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "学生密码", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "学生对象", required = true, paramType = "form", dataType = "String")
    })
    @PostMapping("saveTeacher.do")
    @ResponseBody
    public boolean saveTeacher(Teacher teacher) {
        //        List<Teacher> teacherList = teacherService.list(null);
        return teacherService.save(teacher);
    }

    //    http://localhost:8082/teacher/exportTeacher
    @GetMapping("exportTeacher.do")
    @ResponseBody
    public void exportTeacher() {
        //        List<Teacher> teacherList = teacherService.list(new QueryWrapper<Teacher>().eq("subject", 1000));
        Stopwatch sw = Stopwatch.createStarted();
        List<Teacher> teacherList = teacherService.list(new QueryWrapper<Teacher>().lambda().eq(Teacher::getSubject, 1000));
        //        List<Teacher> teacherList = teacherService.list(null).subList(0, 100_0000);
        System.out.println("查询" + teacherList.size() + "Teacher耗时:" + sw.elapsed(TimeUnit.MILLISECONDS) + "ms");
        sw.reset().start();
        //region 生成excel
        LWorkbook lxwb = LWorkbook.createSXLSX(500);
        Field[] fields = Teacher.class.getDeclaredFields();
        fields = Arrays.copyOfRange(fields, 1, fields.length);
        List<Method> methodList = new ArrayList<>(fields.length);// 所有字段对应的get方法
        LWorkbook.LCellStyle center = lxwb.cellStyle().center();//水平居中 垂直居中
        // 表头
        for (int i = 0; i < fields.length; i++) {
            ApiModelProperty rowNameApi = fields[i].getAnnotation(ApiModelProperty.class);
            String rowName = ofNullable(rowNameApi).map(ApiModelProperty::value).orElse(fields[i].getName());
            methodList.add(ReflectionUtils.findMethod(Teacher.class, "get" + StringUtils.capitalize(fields[i].getName())));
            lxwb.sheet("one").cell(0, i).setCellStyle(center).setCellValue(rowName);
        }
        lxwb.sheet("one").getSheet().createFreezePane(0, 1, 0, 1);// 冻结第一行
        // teacherList中的对象的所有methodList方法的值如果不为null 则赋值给excel
        System.out.println("生成表头等耗时:" + sw.elapsed(TimeUnit.MILLISECONDS) + "ms");
        sw.reset().start();

        LgrenUtil.forEach(teacherList, (t, i) ->
                LgrenUtil.forEach(methodList, (m, j) ->
                        lxwb.sheet("one").cell(i + 1, j)
                                .setCellValue(ofNullable(ReflectionUtils.invokeMethod(m, t)).map(Object::toString).orElse("--"))
                                .setCellStyle(lxwb.center())
                )
        );
        System.out.println("获取数据耗时:" + sw.elapsed(TimeUnit.MILLISECONDS) + "ms");
        sw.reset().start();
        //        lxwb.sheet("one").autoSizeColumnByFirstCol();
        //        System.out.println("自动调整宽度耗时:" + sw.elapsed(TimeUnit.MILLISECONDS) + "ms");sw.reset().stt();
        //endregion

        Workbook wb = lxwb.getWorkbook();
        String fileName = "测试";
        WebUtil.exportExcel(wb, fileName, response);
    }
    //    http://localhost:8082/teacher/exportTeacher

}
