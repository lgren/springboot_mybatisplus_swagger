package com.lgren.springboot_mybatisplus_swagger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 叶子标签信息表
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
@Data
@Accessors(chain = true)
@ApiModel(value="TagLeaf对象", description="叶子标签信息表")
public class TagLeaf extends Model<TagLeaf> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标签ID")
    @TableField("TAG_ID")
    private Integer tagId;

    @ApiModelProperty(value = "序列号")
    @TableField("TAG_ID_INDEX")
    private Integer tagIdIndex;

    @ApiModelProperty(value = "叶子标签名称")
    @TableField("LEAF_NAME")
    private String leafName;

    @ApiModelProperty(value = "叶子标签用户数")
    @TableField("LEAF_COVER")
    private Integer leafCover;

    @ApiModelProperty(value = "叶子标签值(对应用户表es属性值)")
    @TableField("LEAF_VALUE")
    private String leafValue;

    @ApiModelProperty(value = "标签的业务描述")
    @TableField("LEAF_DESC")
    private String leafDesc;

    @ApiModelProperty(value = "城市ID")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "状态：1正常，2下架")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "最后修改时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TagLeaf tagLeaf = (TagLeaf) o;
        return Objects.equals(id, tagLeaf.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id);
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
