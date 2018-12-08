package com.lgren.springboot_mybatisplus_swagger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.google.common.collect.HashBiMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 标签明细信息表，只包含最末级标签
 * </p>
 *
 * @author Lgren
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TagInfo对象", description="标签明细信息表，只包含最末级标签")
public class TagInfo extends Model<TagInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "更新周期，1分钟，2小时，3每日，4每周，5周月，6按季度，7按半年，8按年")
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    @ApiModelProperty(value = "规则类型1文本 2年月日 3年月 4数字")
    @TableField("TAG_TYPE")
    private Boolean tagType;

    @ApiModelProperty(value = "存放类型：1用户表，2单个标签表")
    @TableField("DATA_TYPE")
    private Integer dataType;

    @ApiModelProperty(value = "对应标签表，表名")
    @TableField("DATA_TABLE")
    private String dataTable;

    @ApiModelProperty(value = "标签名称")
    @TableField("TAG_NAME")
    private String tagName;

    @ApiModelProperty(value = "业务规则")
    @TableField("TAG_RULE")
    private String tagRule;

    @ApiModelProperty(value = "产出方法描述")
    @TableField("METHOD_DESC")
    private String methodDesc;

    @ApiModelProperty(value = "标签表字段(对应用户表es属性)")
    @TableField("TAG_FIELDS")
    private String tagFields;

    @ApiModelProperty(value = "叶子标签数")
    @TableField("LEAF_NUM")
    private Integer leafNum;

    @ApiModelProperty(value = "标签覆盖用户数")
    @TableField("COVER_NUM")
    private Long coverNum;

    @ApiModelProperty(value = "状态：1正常，2下架")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "创建时间 申请时间")
    @TableField("INSERT_TIME")
    private LocalDateTime insertTime;

    @ApiModelProperty(value = "最后更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<TagLeaf> tagLeafList;

    @TableField(exist = false)
    private HashBiMap<Integer, TagLeaf> tagLeafMap;

    public TagInfo() {
    }
    public TagInfo(TagInfo tagInfo) {
        BeanUtils.copyProperties(tagInfo, this);
        this.setTagLeafList(null);
        this.setTagLeafMap(null);
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
