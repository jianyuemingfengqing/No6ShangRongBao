package com.learn.srb.core.pojo.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("借款审批列表数据模型")
public class BorrowInfoVO {
    @ApiModelProperty(value = "编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "借款用户id")
    private Long userId;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "借款金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "借款期限")
    private String period;

    @ApiModelProperty(value = "年化利率")
    private String borrowYearRate;

    @ApiModelProperty(value = "还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本")
    private String returnMethod;

    @ApiModelProperty(value = "资金用途")
    private String moneyUse;

    @ApiModelProperty(value = "状态（0：未提交，1：审核中， 2：审核通过， -1：审核不通过）")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
