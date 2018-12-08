package com.lgren.springboot_mybatisplus_swagger.mapper;

import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lgren
 * @since 2018-11-14
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    int insertBatch(List<Teacher> teachers);

    List<Teacher> testSelect();

    List<Teacher> testSelectTwo();

    List<Teacher> testSelectThree();

    List<Teacher> testSelectFour();

    List<Teacher> selectWithCity(Teacher teacher);

    List<Teacher> selectListWithCity(List<Long> teacherIdList);
}
