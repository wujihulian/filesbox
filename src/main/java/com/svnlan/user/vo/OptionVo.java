package com.svnlan.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 9:29
 */
@Data
@TableName("system_option")
public class  OptionVo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("`key`")
    private String key;
    @TableField("`value`")
    private String value;
    @TableField("`type`")
    private String type;

    @TableField("createTime")
    private Long createTime;

    @TableField("modifyTime")
    private Long modifyTime;

    public OptionVo(){
    }
    public OptionVo(String key, String value){
        this.key = key;
        this.value = value;
    }
}
