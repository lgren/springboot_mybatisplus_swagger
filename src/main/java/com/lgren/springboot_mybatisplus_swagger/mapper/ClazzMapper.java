package com.lgren.springboot_mybatisplus_swagger.mapper;

import com.lgren.springboot_mybatisplus_swagger.entity.Clazz;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lgren
 * @since 2018-11-15
 */
public interface ClazzMapper extends BaseMapper<Clazz> {

    int insertBatch(List<Clazz> clazzes);
}
