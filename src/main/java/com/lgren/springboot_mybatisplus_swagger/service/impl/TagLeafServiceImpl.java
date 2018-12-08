package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.lgren.springboot_mybatisplus_swagger.entity.TagLeaf;
import com.lgren.springboot_mybatisplus_swagger.mapper.TagLeafMapper;
import com.lgren.springboot_mybatisplus_swagger.service.ITagLeafService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 叶子标签信息表 服务实现类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
@Service
public class TagLeafServiceImpl extends ServiceImpl<TagLeafMapper, TagLeaf> implements ITagLeafService {
    @Autowired
    private TagLeafMapper tagLeafMapper;

    @Override
    @Cacheable(value = "tagLeafService", key = "#root.targetClass.simpleName + '.' + #root.methodName", unless = "#result eq null")
    public List<TagLeaf> selectAll() {
        return tagLeafMapper.selectList(null);
    }

    @Override
    @Cacheable(value = "tagLeafService", key = "#root.targetClass.simpleName + '.' + #root.methodName", unless = "#result eq null")
    public Map<Integer, TagLeaf> selectAllMap() {
        return selectAll().stream().collect(HashMap::new, (map, t) -> map.put(t.getId(), t), HashMap::putAll);
    }
}
