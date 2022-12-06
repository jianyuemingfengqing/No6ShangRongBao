package com.learn.srb.core.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
@ApiModel("借款审批数据模型")
public class BorrowInfoApprovalVO {

    @ApiModelProperty("借款信息id")
    private String borrowId;
    @ApiModelProperty("是否通过:2通过 -1不通过")
    private Integer status;
    @ApiModelProperty("标的名称")
    private String title;
    @ApiModelProperty("起息日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lendStartDate;
    @ApiModelProperty("年化收益率")
    //年化率=月化率*12
    private BigDecimal lendYearRate;
    @ApiModelProperty("服务费率")
    private BigDecimal serviceRate;
    @ApiModelProperty("标的描述")
    private String lendInfo;

}
