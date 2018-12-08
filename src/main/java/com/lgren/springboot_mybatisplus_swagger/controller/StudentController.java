package com.lgren.springboot_mybatisplus_swagger.controller;


import com.lgren.springboot_mybatisplus_swagger.common.CommResult;
import com.lgren.springboot_mybatisplus_swagger.entity.Student;
import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;
import com.lgren.springboot_mybatisplus_swagger.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * <p>
 * 前端控制器
 * </p>
 * @author Lgren
 * @since 2018-11-14
 */
@Api(tags = "Student")
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @ApiOperation(value = "查看所有学生(通过服务层查询)", notes = "查看所有学生", httpMethod = "GET")
    @GetMapping("allOne")
    @ResponseBody
    public List<Student> selectAllStudentOne() {
        return studentService.list(null);
    }

    @ApiOperation(value = "查看所有学生(ActiveRecord风格)", notes = "查看所有学生", httpMethod = "GET")
    @GetMapping("allTwo")
    @ResponseBody
    public List<Student> selectAllStudentTwo() {
        return new Student().selectAll();
    }

    @ApiOperation(value = "新增学生", notes = "就是新增用户emmmmmm", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "student", value = "不用管", paramType = "form"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", required = false, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "学生用户名", required = false, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "学生密码", required = false, paramType = "form", dataType = "String")
    })
    @PostMapping("insertStudent")
    @ResponseBody
    public CommResult<Student> insertStudent(@Validated Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg =  bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .sorted(Comparator.comparingInt(Objects::hashCode))
                    .reduce("", (result,eMsg) -> result + (StringUtils.isBlank(result) ? eMsg : ( "丨" + eMsg)));
            return CommResult.newFailure("-1", errorMsg);
        }
        student.insert();
        return CommResult.newSuccess(student);
    }

    @ApiOperation(value = "新增学生", notes = "就是新增用户emmmmmm", httpMethod = "POST")
    @PostMapping("insertStudentTwo")
    @ResponseBody
    public Student insertStudentTwo(Student student) {
        student.insert();
        return student;
    }
}
