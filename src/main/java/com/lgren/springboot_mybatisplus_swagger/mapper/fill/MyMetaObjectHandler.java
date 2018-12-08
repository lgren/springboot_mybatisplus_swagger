package com.lgren.springboot_mybatisplus_swagger.mapper.fill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


/**
 * 自动填充的类
 * @author Lgren
 * @create 2018-11-07 16:39
 **/
public class MyMetaObjectHandler implements MetaObjectHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        LOGGER.info("start insert fill ....");
        this.setFieldValByName("insertTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("insertWorker", "lgrenInsert", metaObject);
        this.setFieldValByName("lastUpdateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("lastUpdateWorker", "lgrenUpdate", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LOGGER.info("start update fill ....");
        this.setFieldValByName("lastUpdateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("lastUpdateWorker", "lgrenUpdate", metaObject);
    }
}
