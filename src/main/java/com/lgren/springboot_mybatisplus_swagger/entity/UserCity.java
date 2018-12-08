package com.lgren.springboot_mybatisplus_swagger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

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
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserCity对象", description="")
public class UserCity extends Model<UserCity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "城市Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "标签")
    private String tag;

    @TableField(exist = false)
    private List<TagInfo> tagInfoList;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
