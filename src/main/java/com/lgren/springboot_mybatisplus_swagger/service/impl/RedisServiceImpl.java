package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.alibaba.fastjson.JSON;
import com.lgren.springboot_mybatisplus_swagger.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Override
    public boolean set(final String key, final String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            return true;
        });
    }

    public String get(final String key){
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value =  connection.get(serializer.serialize(key));
            return serializer.deserialize(value);
        });
    }

    @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public <T> boolean setList(String key, List<T> list) {
        String value = JSON.toJSONString(list);
        return set(key,value);
    }

    @Override
    public <T> List<T> getList(String key,Class<T> clz) {
        String json = get(key);
        if(json!=null){
            return JSON.parseArray(json, clz);
        }
        return null;
    }

    @Override
    public long lPush(final String key, Object obj) {
        final String value = JSON.toJSONString(obj);
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            return (long) connection.lPush(serializer.serialize(key), serializer.serialize(value));
        });
    }

    @Override
    public long rPush(final String key, Object obj) {
        final String value = JSON.toJSONString(obj);
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            return (long) connection.rPush(serializer.serialize(key), serializer.serialize(value));
        });
    }

    @Override
    public String lPop(final String key) {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] res =  connection.lPop(serializer.serialize(key));
            return serializer.deserialize(res);
        });
    }

}