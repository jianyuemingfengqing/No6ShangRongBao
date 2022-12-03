package com.learn.srb.core.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBindVO {
    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "银行卡号")
    private String bankNo;

    @ApiModelProperty(value = "银行类型")
    private String bankType;

    @ApiModelProperty(value = "手机号")
    private String mobile;
}
