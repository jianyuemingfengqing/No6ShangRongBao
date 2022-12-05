package com.learn.srb.core.controller.api;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.learn.common.result.R;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.pojo.entity.Borrower;
import com.learn.srb.core.pojo.vo.BorrowerVO;
import com.learn.srb.core.service.BorrowerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author Atguigu
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/api/core/borrower")
public class ApiBorrowerController {
    @Autowired
    BorrowerService borrowerService;

    @ApiOperation("查询借款人审核状态")
    @GetMapping("/auth/getBorrowerStatus")
    public R getBorrowerStatus(@RequestHeader(value = "token", required = false) String token) {
        Long userId = JwtUtils.getUserId(token);
        //查询该用户的借款人审核状态
        Borrower borrower = borrowerService.getOne(Wrappers.lambdaQuery(Borrower.class)
                .eq(Borrower::getUserId, userId)
                .select(Borrower::getStatus));
        Integer status = 0;
        if (borrower != null) {
            status = borrower.getStatus();
        }
        return R.ok().data("status", status);
    }


    @ApiOperation("提交借款人基本信息")
    @PostMapping("/auth/commitBorrower")
    public R commitBorrower(
            @RequestBody BorrowerVO borrowerVO,
            @RequestHeader(value = "token", required = false) String token
    ) {
        borrowerService.saveBorrower(borrowerVO, token);
        return R.ok();
    }
}

