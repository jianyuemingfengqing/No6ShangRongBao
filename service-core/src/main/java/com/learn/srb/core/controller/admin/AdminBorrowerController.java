package com.learn.srb.core.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.common.result.R;
import com.learn.srb.core.pojo.entity.Borrower;
import com.learn.srb.core.pojo.vo.BorrowerDetailVO;
import com.learn.srb.core.service.BorrowerService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/admin/core/borrower")
public class AdminBorrowerController {
    @Resource
    BorrowerService borrowerService;

    @ApiOperation("带条件分页查询借款人列表")
    @GetMapping("list/{pageNum}/{pageSize}")
    public R list(@PathVariable("pageNum") Integer pageNum,
                  @PathVariable("pageSize") Integer pageSize,
                  String keyword) {
        IPage<Borrower> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Borrower> wrapper = Wrappers.lambdaQuery(Borrower.class);
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.like(Borrower::getName, keyword) // 姓名
                    .or()
                    .like(Borrower::getMobile, keyword) // 手机
                    .or()
                    .like(Borrower::getIdCard, keyword); // 身份证
        }
        borrowerService.page(page, wrapper);
        return R.ok().data("page", page);
    }

    @ApiOperation("查询借款人详情")
    @GetMapping("getBorrowerDetail/{id}")
    public R getBorrowerDetail(@PathVariable("id")String id){
        BorrowerDetailVO vo = borrowerService.getBorrowerDetail(id);
        return R.ok().data("item",vo);
    }
/*    @ApiOperation("借款人审批")
    @PostMapping("approval")
    public R approval(@RequestBody BorrowerApprovalVO vo){
        borrowerService.approval(vo);
        return R.ok().message("审批完成");
    }
   */

}

