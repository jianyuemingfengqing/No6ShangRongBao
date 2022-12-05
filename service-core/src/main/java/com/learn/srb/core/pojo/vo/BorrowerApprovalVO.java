package com.learn.srb.core.pojo.vo;

import lombok.Data;

@Data
public class BorrowerApprovalVO {
    private String borrowerId;
    private Integer status;
    private Integer infoIntegral;
    private Boolean isIdCardOk;
    private Boolean isCarOk;
    private Boolean isHouseOk;
}
