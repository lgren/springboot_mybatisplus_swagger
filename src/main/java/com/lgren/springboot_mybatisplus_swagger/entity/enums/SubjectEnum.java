package com.lgren.springboot_mybatisplus_swagger.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 学科枚举类
 * @author Lgren
 * @create 2018/11/07 13:32
 **/
public enum SubjectEnum implements IEnum<Integer> {
    CHINESE(1, "语文"),
    MATH(2, "数学"),
    ENGLISH(3, "英语");

    private int value;
    private String desc;

    SubjectEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return desc;
    }
}
