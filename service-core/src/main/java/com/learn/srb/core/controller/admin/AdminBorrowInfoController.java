package com.learn.srb.core.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.common.result.R;
import com.learn.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.learn.srb.core.pojo.vo.BorrowInfoVO;
import com.learn.srb.core.service.BorrowInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/admin/core/borrowInfo")
public class AdminBorrowInfoController {
    @Resource
    BorrowInfoService borrowInfoService;

    @ApiOperation("借款审批")
    @PostMapping("approval")
    public R borrowInfoApproval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO){
        borrowInfoService.borrowInfoApproval(borrowInfoApprovalVO);
        return R.ok().message("审批完成");
    }

    @ApiOperation("查询借款详情")
    @GetMapping("{id}")
    public R borrowInfo(@PathVariable("id")String id){
        //BorrowInfoVO    BorrowerDetailVO
        Map<String,Object> map = borrowInfoService.getBorrowInfo(id);
        //将k-v数据存在 r对象的map中：r.data.put(key,val);
//        return R.ok().data("map",map);//r.data.map.borrowInfoVO
        //使用map的引用赋值给r对象的data属性：r.data = map;
        return R.ok().data(map);//r.data.borrowInfoVO
    }
    @ApiOperation("借款审批列表")
    @GetMapping("list/{pageNum}/{pageSize}")
    public R list(@PathVariable("pageNum")Integer pageNum,
                  @PathVariable("pageSize")Integer pageSize){
        Page<BorrowInfoVO> page = new Page<>(pageNum,pageSize);
        borrowInfoService.listBorrowInfoVOs(page);
        return  R.ok().data("page",page);
    }

}




