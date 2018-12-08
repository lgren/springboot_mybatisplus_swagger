package com.lgren.springboot_mybatisplus_swagger.service;

import com.lgren.springboot_mybatisplus_swagger.entity.Student;
import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-14
 */
public interface ITeacherService extends IService<Teacher> {
    int insertBatchAutoCalLen(Set<Teacher> teaAddList, int i);

    List<Teacher> testSelect();
    List<Teacher> testSelectTwo();
    List<Teacher> testSelectThree();
    List<Teacher> testSelectFour();

    List<Teacher> selectWithCity(Teacher teacher);

    List<Teacher> selectListWithCity(List<Long> teacherIdList);
}
