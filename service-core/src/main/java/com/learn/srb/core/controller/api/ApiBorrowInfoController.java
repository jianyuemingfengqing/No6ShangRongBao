package com.learn.srb.core.controller.api;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.learn.common.result.R;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.pojo.entity.BorrowInfo;
import com.learn.srb.core.service.BorrowInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/api/core/borrowInfo")
public class ApiBorrowInfoController {
    @Resource
    BorrowInfoService borrowInfoService;
    @ApiOperation("获取借款状态")
    @GetMapping("auth/status")
    public R borrowStatus(@RequestHeader(value = "token",required = false) String token){
        Long userId = JwtUtils.getUserId(token);
        BorrowInfo borrowInfo = borrowInfoService.getOne(Wrappers.lambdaQuery(BorrowInfo.class)
                .eq(BorrowInfo::getUserId, userId)
                .select(BorrowInfo::getStatus));
        return R.ok().data("status" , borrowInfo==null?0:borrowInfo.getStatus());
    }
    @ApiOperation("获取用户借款额度")
    @GetMapping("auth/borrowAmount")
    public R borrowAmount(@RequestHeader(value = "token",required = false) String token){
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(token);
        return R.ok().data("borrowAmount",borrowAmount);
    }
    /*
    "borrowYearRate": "10",
    "amount": "5000",
    "period": 12,
    "returnMethod": 2,
    "moneyUse": 1
     */
    @ApiOperation("提交借款申请")
    @PostMapping("auth/commitBorrow")
    public R commitBorrow(@RequestBody BorrowInfo borrowInfo,
                          @RequestHeader(value = "token",required = false) String token){
        borrowInfoService.commitBorrow(borrowInfo,token);
        return R.ok().message("提交借款申请成功");
    }
}



