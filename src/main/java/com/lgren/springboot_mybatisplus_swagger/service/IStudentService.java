package com.lgren.springboot_mybatisplus_swagger.service;

import com.lgren.springboot_mybatisplus_swagger.entity.Clazz;
import com.lgren.springboot_mybatisplus_swagger.entity.Student;
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
public interface IStudentService extends IService<Student> {
    int insertBatchAutoCalLen(Set<Student> stuAddList, int i);

    List<Student> testSelect();
}
