package com.learn.srb.core.controller.api;

import com.learn.common.exception.BusinessException;
import com.learn.common.result.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/core/test")
public class ApiTestController {
    @Value("${server.port}")
    Integer port;
    @GetMapping("test/{key}")
    public R test(@PathVariable("key")String key){
        System.out.println("端口号为："+port +"的服务获取到的参数: " +key);
        return R.ok();
    }
    @GetMapping("test1/{key1}")
    public R test1(@PathVariable("key1")String key1){
        System.out.println("端口号为："+port +"的服务获取到的参数: " +key1);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok();
    }
    @GetMapping("test2")
    public R test2(String key1,String key2){
        System.out.println("端口号为："+port +"的服务获取到的参数: " +key1+" , "+key2);
        return R.ok();
    }
    @GetMapping("test3")
    public R test3(BusinessException e){//pojo入参
        System.out.println("端口号为："+port +"的服务获取到的参数: " + e);
        return R.ok();
    }
    @PostMapping("test4")
    public R test4(@RequestBody R r){
        System.out.println("端口号为："+port +"的服务获取到的参数: " +r);
        return R.ok();
    }
}
