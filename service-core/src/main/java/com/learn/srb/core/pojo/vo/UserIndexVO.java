package com.learn.srb.core.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserIndexVO {
    @ApiModelProperty(value = "1：出借人 2：借款人")
    private Integer userType;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "头像")
    private String headImg;
    @ApiModelProperty(value = "绑定状态（0：未绑定，1：绑定成功 -1：绑定失败）")
    private Integer bindStatus;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    @ApiModelProperty(value = "帐户可用余额")
    private BigDecimal amount;
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeAmount;
}
