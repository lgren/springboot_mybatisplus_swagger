package com.lgren.springboot_mybatisplus_swagger.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import com.github.jsonzou.jmockdata.annotation.MockIgnore;
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
 * @since 2018-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Clazz对象", description="")
public class Clazz extends Model<Clazz> {

    private static final long serialVersionUID = 1L;

    @MockIgnore
    @ApiModelProperty(value = "班级")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String className;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
