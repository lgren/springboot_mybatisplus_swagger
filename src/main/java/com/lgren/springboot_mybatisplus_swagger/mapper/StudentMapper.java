package com.lgren.springboot_mybatisplus_swagger.mapper;

import com.lgren.springboot_mybatisplus_swagger.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lgren
 * @since 2018-11-14
 */
public interface StudentMapper extends BaseMapper<Student> {

    int insertBatch(List<Student> students);

    List<Student> testSelect();
}
