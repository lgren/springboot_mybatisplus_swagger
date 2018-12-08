package com.lgren.springboot_mybatisplus_swagger.service;

import java.util.List;

public interface IRedisService {

    boolean set(String key, String value);

    String get(String key);

    // expire=0时可当做删除
    boolean expire(String key, long expire);

    <T> boolean setList(String key, List<T> list);

    <T> List<T> getList(String key, Class<T> clz);

    // 将一个或多个值 value 插入到列表 key 的表头
    long lPush(String key, Object obj);

    // 将一个或多个值 value 插入到列表 key 的表尾(最右边)
    long rPush(String key, Object obj);

    // 删除并返回key中存储的列表的第一个元素。
    String lPop(String key);

}
