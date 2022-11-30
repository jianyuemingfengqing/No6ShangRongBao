package com.learn.srb.core.controller.api;


import com.learn.common.result.R;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.vo.UserInfoVo;
import com.learn.srb.core.pojo.vo.UserRegisterVO;
import com.learn.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/api/core/userInfo")
@CrossOrigin
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

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R login(@RequestBody UserInfo userInfo) {

        String token = userInfoService.login(userInfo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "登录后回显")
    @GetMapping("auth/getUserInfo")//auth表示当前接口必须登录后才可以访问
    public R getUserInfo(@RequestHeader(value = "token", required = false) String token) {//获取token参数
        //请求头传token：  token     token字符串
        Long userId = JwtUtils.getUserId(token);
        //userId nickname  头像
        UserInfo userInfo = userInfoService.getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return R.ok().data("item", userInfoVo);
    }
}

