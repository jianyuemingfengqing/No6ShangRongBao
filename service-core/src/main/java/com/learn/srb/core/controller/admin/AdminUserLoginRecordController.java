package com.learn.srb.core.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.common.result.R;
import com.learn.srb.core.pojo.entity.UserLoginRecord;
import com.learn.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/admin/core/userLoginRecord")
public class AdminUserLoginRecordController {
    @Resource
    UserLoginRecordService userLoginRecordService;

    @ApiOperation("查询会员最近10条登录日志")
    @GetMapping("getLoginRecords/{userInfoId}")
    public R getLoginRecords(@PathVariable("userInfoId") String userInfoId) {
        List<UserLoginRecord> userLoginRecords = userLoginRecordService.list(new LambdaQueryWrapper<UserLoginRecord>()
                .eq(UserLoginRecord::getUserId, userInfoId)
                .orderByDesc(UserLoginRecord::getCreateTime)
                .last("limit 10"));
        return R.ok().data("items", userLoginRecords);
    }
}

