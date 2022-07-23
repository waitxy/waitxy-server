package com.waitxy.weixin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "wx_error_code")
@Data
@TableName(value = "wx_error_code")
public class WxErrorCode {
    public static final String COL_CODE = "code";
    public static final String COL_DESC = "desc";
    @TableField(value = "err_code")
    @ApiModelProperty(value = "")
    private String errCode;

    @TableField(value = "err_msg")
    @ApiModelProperty(value = "")
    private String errMsg;

    public static final String COL_ERR_CODE = "err_code";

    public static final String COL_ERR_MSG = "err_msg";
}