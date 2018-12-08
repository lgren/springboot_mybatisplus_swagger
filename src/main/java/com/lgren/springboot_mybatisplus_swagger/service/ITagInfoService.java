package com.lgren.springboot_mybatisplus_swagger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgren.springboot_mybatisplus_swagger.entity.TagInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签明细信息表，只包含最末级标签 服务类
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
public interface ITagInfoService extends IService<TagInfo> {

    List<TagInfo> selectAll();

    Map<Integer, TagInfo> selectAllMap();

    Map<Integer, TagInfo> selectAllTagMap();
}

