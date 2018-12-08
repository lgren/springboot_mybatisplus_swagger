package com.lgren.springboot_mybatisplus_swagger;

import com.lgren.springboot_mybatisplus_swagger.entity.TagInfo;
import com.lgren.springboot_mybatisplus_swagger.service.*;
import com.lgren.springboot_mybatisplus_swagger.util.TagUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class UseTest {
    //region 自动注入的类
    @Autowired
    private ITagLeafService tagLeafService;
    @Autowired
    private ITagInfoService tagInfoService;
    @Autowired
    private IUserCityService userCityService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IRedisService redisService;
    //endregion
    private Map<Integer, TagInfo> allTag;
    private TagUtil tagUtil;
    @Before
    public void before() {
        allTag = tagInfoService.selectAllTagMap();
        tagUtil = new TagUtil(allTag);
    }

    @Test
    public void test1() {
        System.out.println(allTag);
        System.out.println(tagUtil);
        Map<Integer, List<Integer>> condition = tagUtil.randomTag();
        System.out.println();
    }
}
