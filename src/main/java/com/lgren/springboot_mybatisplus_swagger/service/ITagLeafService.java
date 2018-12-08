package com.lgren.springboot_mybatisplus_swagger.service;

import com.lgren.springboot_mybatisplus_swagger.entity.TagLeaf;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 叶子标签信息表 服务类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
public interface ITagLeafService extends IService<TagLeaf> {

    List<TagLeaf> selectAll();

    Map<Integer, TagLeaf> selectAllMap();
}
