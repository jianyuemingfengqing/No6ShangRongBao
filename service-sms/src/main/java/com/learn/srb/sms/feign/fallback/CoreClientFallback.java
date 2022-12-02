package com.learn.srb.sms.feign.fallback;


import com.learn.common.exception.BusinessException;
import com.learn.common.result.R;
import com.learn.srb.sms.feign.CoreClient;
import org.springframework.stereotype.Component;

@Component //组件类注解之间区别并不大
public class CoreClientFallback implements CoreClient {
    @Override
    public R isRegist(String mobile) {
        System.out.println("CoreClientFallback test1降级处理");
        return R.error();
    }

    @Override
    public R test(String key) {
        System.out.println("CoreClientFallback test降级处理");
        return R.error();
    }

    @Override
    public R test1(String key1) {
        System.out.println("CoreClientFallback test1降级处理");
        return R.error();
    }

    @Override
    public R test2(String key1, String key2) {
        System.out.println("CoreClientFallback test2降级处理");
        return R.error();
    }

    @Override
    public R test3(BusinessException e) {
        System.out.println("CoreClientFallback test3降级处理");
        return R.error();
    }

    @Override
    public R test4(R r) {
        System.out.println("CoreClientFallback test4降级处理");
        return R.error();
    }
}
