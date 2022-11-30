package com.learn.srb.core.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoSearchVO {
    @ApiModelProperty(value = "状态（0：锁定 1：正常）")
    private Integer status;
    @ApiModelProperty(value = "1：出借人 2：借款人")
    private Integer userType;
    @ApiModelProperty(value = "手机号码")
    private String mobile;
}
