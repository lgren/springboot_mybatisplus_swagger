package com.lgren.springboot_mybatisplus_swagger.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.github.jsonzou.jmockdata.annotation.MockIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

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
@ApiModel(value="Student", description="")
public class Student extends Model<Student> {
    private static final long serialVersionUID = 1L;
    //region 忽略一部分
    @MockIgnore
    @ApiModelProperty(value = "学生ID")
    private Long id;

    @Length(min = 6, max = 18, message = "用户名长度为6-18")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;

    @MockIgnore
    @ApiModelProperty(value = "班级")
    private Long clazzId;

    @MockIgnore
    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateTime;
    //endregion

    @MockIgnore
    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastUpdateWorker;

    @MockIgnore
    @ApiModelProperty(value = "添加时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime insertTime;

    @MockIgnore
    @ApiModelProperty(value = "添加人")
    @TableField(fill = FieldFill.INSERT)
    private String insertWorker;

    @MockIgnore
    @ApiModelProperty(value = "状态0.未删除 1.已删除")
    @TableLogic
    private Boolean delStatus;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
