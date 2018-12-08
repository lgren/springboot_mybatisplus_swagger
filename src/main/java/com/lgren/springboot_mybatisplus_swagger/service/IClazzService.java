package com.lgren.springboot_mybatisplus_swagger.service;

import com.lgren.springboot_mybatisplus_swagger.entity.Clazz;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-15
 */
public interface IClazzService extends IService<Clazz> {

    int insertBatchAutoCalLen(Set<Clazz> claAddList, int i);
}
