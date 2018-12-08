package com.lgren.springboot_mybatisplus_swagger.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisMapperRefresh;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lgren.springboot_mybatisplus_swagger.mapper.fill.MyMetaObjectHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootConfiguration
public class MyBatisPlusConfig {
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    @Bean
    public MybatisMapperRefresh mapperRefresh() {
        org.springframework.core.io.Resource[] resources;
        try {
            resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:com/lgren/springboot_mybatisplus_swagger/mapper/*.xml");
        } catch (IOException e) {
            resources = new org.springframework.core.io.Resource[0];
            e.printStackTrace();
        }
        return new MybatisMapperRefresh(resources ,sqlSessionFactory, 5, 10, true);
    }
}