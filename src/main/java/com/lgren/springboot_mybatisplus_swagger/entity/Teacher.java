package com.lgren.springboot_mybatisplus_swagger.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.jsonzou.jmockdata.annotation.MockIgnore;
import com.lgren.springboot_mybatisplus_swagger.entity.enums.JobTitleEnum;
import com.lgren.springboot_mybatisplus_swagger.entity.enums.SubjectEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Lgren
 * @since 2018-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Teacher", description="")
public class Teacher extends Model<Teacher> {

    private static final long serialVersionUID = 1L;

    @MockIgnore
    private Long id;

    private String username;

    private String password;

    private String realName;

//    @JsonValue
    private Integer subject;

//    @JsonValue
    private Integer jobTitle;

    private LocalDateTime birthday;

    @MockIgnore
    @ApiModelProperty(value = "班级")
    private Long clazzId;

    private String tag;

    @TableField(exist = false)
    private List<UserCity> userCityList;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
