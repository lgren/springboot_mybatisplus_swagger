package com.lgren.springboot_mybatisplus_swagger.mapper;

import com.lgren.springboot_mybatisplus_swagger.entity.UserCity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
public interface UserCityMapper extends BaseMapper<UserCity> {
    List<UserCity> selectByUserId(String userId);
}
