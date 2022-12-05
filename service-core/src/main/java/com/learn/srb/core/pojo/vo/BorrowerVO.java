package com.learn.srb.core.pojo.vo;

import com.learn.srb.core.pojo.entity.Borrower;
import com.learn.srb.core.pojo.entity.BorrowerAttach;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
@ApiModel("提交的借款人模型")
@Data
public class BorrowerVO extends Borrower {
    private List<BorrowerAttach> attachs;
}
