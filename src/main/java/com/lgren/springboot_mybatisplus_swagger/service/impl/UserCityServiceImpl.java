package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.lgren.springboot_mybatisplus_swagger.entity.UserCity;
import com.lgren.springboot_mybatisplus_swagger.mapper.UserCityMapper;
import com.lgren.springboot_mybatisplus_swagger.service.IUserCityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
@Service
public class UserCityServiceImpl extends ServiceImpl<UserCityMapper, UserCity> implements IUserCityService {

}
