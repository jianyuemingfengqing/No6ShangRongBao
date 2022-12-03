package com.learn.srb.core.controller.api;


import com.learn.common.result.R;
import com.learn.srb.core.pojo.vo.UserBindVO;
import com.learn.srb.core.service.UserBindService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/api/core/userBind")
public class ApiUserBindController {
    @Resource
    UserBindService userBindService;

    //处理用户账户绑定的请求
    /*
        前端提交了绑定用户的个人数据
            后端接口可以将数据提交给hfb开户，但是需要考虑编码问题
           后端接口也可以将数据封装到一个自动提交的表单中响应给浏览器，浏览器解析表单代码时会自动将
            表单数据提交给表单的aciton地址   无需考虑编码问题

            <form action="http://localhost:9999/userBind/BindAgreeUserV2">
                <input name="xx" value="xxx">
            </form>
            <script>
                document.getElementsByName('form')[0].submit()
            </script>
     */
    @ApiOperation("账户开通绑定")
    @PostMapping("auth/bindAccount")
    public R bindAccount(@RequestBody UserBindVO userBindVO,
                         @RequestHeader(value = "token", required = false) String token) {//获取前端提交的参数
        String form = userBindService.bindAccount(userBindVO, token);
        return R.ok().data("form", form);
    }


    ///api/core/userBind/notify
    @ApiOperation("异步回调:hfb在用户账户绑定后回调")
    @PostMapping("notify")
    public String notifyUrl(HttpServletRequest request) {
//        System.out.println("回调了");
//        return R.ok().toString();
        //代表用户账户绑定完毕：获取hfb通知的用户绑定的数据
        return userBindService.notifyUrl(request);

    }

}

