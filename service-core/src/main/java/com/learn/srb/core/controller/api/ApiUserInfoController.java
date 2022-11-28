package com.learn.srb.core.controller.api;


import com.learn.common.result.R;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.vo.UserRegisterVO;
import com.learn.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("api/core/userInfo")
@Api(tags = "用户信息模块")
public class ApiUserInfoController {
    @Resource
    UserInfoService userInfoService;

    @ApiOperation(value = "注册")
    @PostMapping("register")
    public R register(@RequestBody UserRegisterVO userRegisterVO) {

        userInfoService.register(userRegisterVO);
        return R.ok().message("注册成功");
    }
}
