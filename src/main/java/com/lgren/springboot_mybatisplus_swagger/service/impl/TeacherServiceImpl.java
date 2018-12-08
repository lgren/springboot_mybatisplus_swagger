package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.lgren.springboot_mybatisplus_swagger.entity.Teacher;
import com.lgren.springboot_mybatisplus_swagger.mapper.StudentMapper;
import com.lgren.springboot_mybatisplus_swagger.mapper.TeacherMapper;
import com.lgren.springboot_mybatisplus_swagger.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgren.springboot_mybatisplus_swagger.util.LgrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-14
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public int insertBatchAutoCalLen(Set<Teacher> teaAddList, int batchCount) {
        return LgrenUtil.getAllList(new ArrayList<>(teaAddList), 4 * 1024 * 1024 / batchCount).parallelStream()
                .mapToInt(teacherMapper::insertBatch).sum();
    }

    @Override
    public List<Teacher> testSelect() {
        return teacherMapper.testSelect();
    }

    @Override
    public List<Teacher> testSelectTwo() {
        return teacherMapper.testSelectTwo();
    }

    @Override
    public List<Teacher> testSelectThree() {
        return teacherMapper.testSelectThree();
    }

    @Override
    public List<Teacher> testSelectFour() {
        return teacherMapper.testSelectFour();
    }

    @Override
    public List<Teacher> selectWithCity(Teacher teacher) {
        return teacherMapper.selectWithCity(teacher);
    }

    @Override
    public List<Teacher> selectListWithCity(List<Long> teacherIdList) {
        return teacherMapper.selectListWithCity(teacherIdList);
    }
}
