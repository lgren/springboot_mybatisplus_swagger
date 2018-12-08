package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.lgren.springboot_mybatisplus_swagger.entity.Clazz;
import com.lgren.springboot_mybatisplus_swagger.mapper.ClazzMapper;
import com.lgren.springboot_mybatisplus_swagger.service.IClazzService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgren.springboot_mybatisplus_swagger.util.LgrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-15
 */
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements IClazzService {
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public int insertBatchAutoCalLen(Set<Clazz> claAddList, int batchCount) {
        return LgrenUtil.getAllList(new ArrayList<>(claAddList), 4 * 1024 * 1024 / batchCount).parallelStream()
                .mapToInt(clazzMapper::insertBatch).sum();
    }
}
