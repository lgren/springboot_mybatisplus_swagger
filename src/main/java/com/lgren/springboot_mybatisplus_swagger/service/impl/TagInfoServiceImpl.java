package com.lgren.springboot_mybatisplus_swagger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.HashBiMap;
import com.lgren.springboot_mybatisplus_swagger.entity.TagInfo;
import com.lgren.springboot_mybatisplus_swagger.entity.TagLeaf;
import com.lgren.springboot_mybatisplus_swagger.mapper.TagInfoMapper;
import com.lgren.springboot_mybatisplus_swagger.service.ITagInfoService;
import com.lgren.springboot_mybatisplus_swagger.service.ITagLeafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 标签明细信息表，只包含最末级标签 服务实现类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
@Service
public class TagInfoServiceImpl extends ServiceImpl<TagInfoMapper, TagInfo> implements ITagInfoService {
    @Autowired
    private TagInfoMapper tagInfoMapper;
    @Autowired
    private ITagLeafService tagLeafService;

    @Override
    @Cacheable(value = "tagInfoService", key = "#root.targetClass.simpleName + '.' + #root.methodName", unless = "#result eq null")
    public List<TagInfo> selectAll() {
        return tagInfoMapper.selectList(null);
    }

    @Override
    @Cacheable(value = "tagInfoService", key = "#root.targetClass.simpleName + '.' + #root.methodName", unless = "#result eq null")
    public Map<Integer, TagInfo> selectAllMap() {
        return selectAll().stream().collect(HashMap::new, (map, t) -> map.put(t.getId(), t), HashMap::putAll);
    }

    @Override
    @Cacheable(value = "tagInfoService", key = "#root.targetClass.simpleName + '.' + #root.methodName", unless = "#result eq null")
    public Map<Integer, TagInfo> selectAllTagMap() {
        List<TagLeaf> leafList = tagLeafService.selectAll();
        return selectAll().stream()
                .peek(tInfo -> {
                    List<TagLeaf> tagLeafList = new ArrayList<>(leafList.size());
                    HashBiMap<Integer, TagLeaf> tagLeafMap = HashBiMap.create(leafList.size());
                    leafList.forEach(leaf -> {
                        if (Objects.equals(leaf.getTagId(), tInfo.getId())) {
                            tagLeafList.add(leaf);
                            tagLeafMap.put(leaf.getTagIdIndex(), leaf);
                        }
                    });
                    tInfo.setTagLeafList(tagLeafList);
                    tInfo.setTagLeafMap(tagLeafMap);
                })
                .collect(HashMap::new, (map, t) -> map.put(t.getId(), t), HashMap::putAll);
    }
}
