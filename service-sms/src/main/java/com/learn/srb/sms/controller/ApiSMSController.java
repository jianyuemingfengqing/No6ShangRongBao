package com.learn.srb.sms.controller;

import com.learn.common.result.R;
import com.learn.srb.sms.server.SmsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/sms")
public class ApiSMSController {
    @Resource
    SmsService smsservice;

    @ApiOperation("发送短信")
    @GetMapping("sendMsg/{mobile}/{type}")
    public R sendMsg(
            @PathVariable("mobile") String mobile,
            @ApiParam(value = "短信类型: 0注册 1:登录 2:充值")
            @PathVariable("type") Integer type
    ) {
        smsservice.sendMsg(mobile,type);
        return R.ok();
    }
}
