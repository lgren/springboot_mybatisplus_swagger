package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.lgren.springboot_mybatisplus_swagger.entity.Student;
import com.lgren.springboot_mybatisplus_swagger.mapper.ClazzMapper;
import com.lgren.springboot_mybatisplus_swagger.mapper.StudentMapper;
import com.lgren.springboot_mybatisplus_swagger.service.IStudentService;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public int insertBatchAutoCalLen(Set<Student> stuAddList, int batchCount) {
        return LgrenUtil.getAllList(new ArrayList<>(stuAddList), 4 * 1024 * 1024 / batchCount).parallelStream()
                .mapToInt(studentMapper::insertBatch).sum();
    }

    @Override
    public List<Student> testSelect() {
        return studentMapper.testSelect();
    }
}
