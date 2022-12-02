package com.learn.srb.core.controller.admin;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.common.result.R;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.vo.UserInfoSearchVO;
import com.learn.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/admin/core/userInfo")

@Api(tags = "后台管理  用户信息模块")
public class AdminUserInfoController {
    @Resource
    UserInfoService userInfoService;

    @ApiOperation("带条件分页查询用户信息")
    @GetMapping("list/{pageNum}/{pageSize}")
    public R listUserInfos(UserInfoSearchVO userInfoSearchVO,  //get方式可以通过pojo入参接收请求参数
                           @PathVariable(value = "pageNum") Integer pageNum,
                           @PathVariable(value = "pageSize") Integer pageSize) {
        Page<UserInfo> page = new Page<>(pageNum, pageSize);
        userInfoService.listUserInfos(page, userInfoSearchVO);
        return R.ok().data("page", page);
    }

    @ApiOperation("账号锁定解锁")
    @PutMapping("updateStatus/{id}/{status}")
    public R updateStatus(@PathVariable("id") String id,
                          @PathVariable("status") Integer status) {
        userInfoService.update(new LambdaUpdateWrapper<UserInfo>()
                .eq(UserInfo::getId, id)
                .set(UserInfo::getStatus, status));
        return R.ok();
    }

}

