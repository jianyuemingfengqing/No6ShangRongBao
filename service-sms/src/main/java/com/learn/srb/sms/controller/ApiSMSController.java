package com.learn.srb.sms.controller;

import com.learn.common.result.R;
import com.learn.srb.sms.feign.CoreClient;
import com.learn.srb.sms.server.SmsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController

@RequestMapping("/api/sms")
public class ApiSMSController {
    @Resource
    SmsService smsService;

    @Resource
    CoreClient coreClient;

    @ApiOperation("发送短信")
    @GetMapping("sendMsg/{mobile}/{type}")
    public R sendMsg(
            @PathVariable("mobile") String mobile,
            @ApiParam(value = "短信类型: 0注册 1:登录 2:充值")
            @PathVariable("type") Integer type
    ) {
        smsService.sendMsg(mobile, type);
        return R.ok();
    }

    @ApiOperation("测试远程访问")
    @GetMapping("test")
    public R testMsg() {
        coreClient.test("hello word");
        return R.ok();
    }

}
