package com.learn.srb.sms.feign;


import com.learn.common.exception.BusinessException;
import com.learn.common.result.R;
import com.learn.srb.sms.feign.fallback.CoreClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

//当前接口发起请求访问的服务器地址为service-core从注册中心获取的地址：192.168.137.1:8112
@FeignClient(value = "service-core",fallback = CoreClientFallback.class)
public interface CoreClient {

    @GetMapping("/api/core/userInfo/isRegist/{mobile}")
    public R isRegist(@PathVariable("mobile") String mobile);



    @GetMapping("/api/core/test/test/{key}")//GetMapping请求方式
    public R test(@PathVariable("key")String key);//R代表返回值类型
    //192.168.137.1:8112+ 接口路径
    //192.168.137.1:8112/api/core/test/test1/{key1}
    @GetMapping("/api/core/test/test1/{key1}")//GetMapping请求方式
    public R test1(@PathVariable("key1")String key1);//R代表返回值类型
    //feign客户端创建时 方法的参数如果没有加注解 默认当做请求体参数传参
    //但是 请求体只有一个
    @GetMapping("/api/core/test/test2")
    public R test2(@RequestParam("key1") String key1,@RequestParam("key2") String key2);
    //SpringQueryMap注解可以将一个对象的属性和值转为请求参数列表提交
    //必须使用简单类型的对象
    @GetMapping("/api/core/test/test3")
    public R test3(@SpringQueryMap BusinessException e);

    @PostMapping("/api/core/test/test4")
    public R test4(@RequestBody R r);
}
