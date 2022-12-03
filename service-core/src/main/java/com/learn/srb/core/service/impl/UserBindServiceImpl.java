package com.learn.srb.core.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.hfb.FormHelper;
import com.learn.srb.core.hfb.HfbConst;
import com.learn.srb.core.hfb.RequestHelper;
import com.learn.srb.core.mapper.UserBindMapper;
import com.learn.srb.core.pojo.entity.UserBind;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.vo.UserBindVO;
import com.learn.srb.core.service.UserBindService;
import com.learn.srb.core.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Service
@Slf4j
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {
    @Resource
    UserInfoService userInfoService;

    @Override
    public String bindAccount(UserBindVO userBindVO, String token) {
        Long userId = JwtUtils.getUserId(token); // 验证用户id, 解析失败直接报异常

        //拼接到自动提交表单的表单项中的k-v参数值列表
        Map<String, Object> map = new HashMap<>();
/*
      作用      可以将一个对象或map转为另一个对象或map
      前提条件   map的属性名和bean的属性名一样才可以
        ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.convertValue(from ,to)
*/
        //表单自动提交访问的用户绑定的接口 需要的参数 接口文档中有描述的
        map.put("agentId", HfbConst.AGENT_ID); // 汇付宝给商家分配的固定值
        map.put("agentUserId", userId); // 从token中获取
        map.put("idCard", userBindVO.getIdCard());
        map.put("personalName", userBindVO.getName());
        map.put("bankType", userBindVO.getBankType());
        map.put("bankNo", userBindVO.getBankNo());
        map.put("mobile", userBindVO.getMobile());
        map.put("returnUrl", HfbConst.USERBIND_RETURN_URL);//浏览器提交绑定请求给hfb后同步跳转返回的页面地址(绑定成功的页面的地址)
        map.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL); //浏览器提交绑定请求给hfb后  hfb处理完毕要访问的我们的服务中的接口(可以获取用户绑定的结果)
        map.put("timestamp", System.currentTimeMillis());
        //基于上面的参数 通过MD5+秘钥生成的签名 防止数据被篡改   写在参数设置完毕后
        String sign = RequestHelper.getSign(map);
        map.put("sign", sign);
        String form = FormHelper.buildForm(HfbConst.USERBIND_URL, map);


        //保存绑定数据到数据库中
        UserBind userBind = new UserBind();
        // 0 : 未绑定  1：已绑定  -1 ：绑定失败
        BeanUtils.copyProperties(userBindVO, userBind); // 将属性全部拷贝一份到userBind
        userBind.setUserId(userId);
        userBind.setStatus(0);
        this.save(userBind);

        return form;
    }

    @Override
    public String notifyUrl(HttpServletRequest request) {
        //1、获取通知结果   使用request而不用实体类, 是因为request有isSignEquals可以验签, 更简单
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = RequestHelper.switchMap(parameterMap);
        //2、验签
        boolean b = RequestHelper.isSignEquals(map);
        if (!b) {
            log.error("账户绑定失败：签名验证错误- {}", JSON.toJSONString(map));
            return "fail";
        }
        //3、判断hfb业务处理是否成功resultCode   0001
        String resultCode = map.get("resultCode").toString();
        if (!"0001".equals(resultCode)) {
            log.error("账户绑定失败：状态码错误- {}", JSON.toJSONString(map));
            return "fail";
        }
        //4、保存绑定数据+更新userInfo表中的绑定字段
        //hfb生成的账户的编号
        String bindCode = map.get("bindCode").toString();

        //判断如果bindcode已存在 后续业务不执行(bindcode唯一)
        int count = this.count(new LambdaQueryWrapper<UserBind>()
                .eq(UserBind::getBindCode, bindCode));
        if (count > 0) {
            //防止幂等性问题
            return "success";
        }
        //绑定hfb的会员的id
        String agentUserId = map.get("agentUserId").toString();

        //更新绑定数据状态为成功
        //根据userid查询绑定对象+用户对象
        UserBind userBind = this.getOne(new LambdaQueryWrapper<UserBind>()
                .eq(UserBind::getUserId, agentUserId));
//        userBind.setStatus(1);
//        userBind.setBindCode(bindCode);
//        this.updateById(userBind);
        this.update(new LambdaUpdateWrapper<UserBind>()
                .eq(UserBind::getUserId, agentUserId)
                .set(UserBind::getStatus, 1)
                .set(UserBind::getBindCode, bindCode));

        userInfoService.update(new LambdaUpdateWrapper<UserInfo>()
                .eq(UserInfo::getId, agentUserId)
                .set(UserInfo::getBindCode, bindCode)
                .set(UserInfo::getBindStatus, 1)
                .set(UserInfo::getIdCard, userBind.getIdCard())
                .set(UserInfo::getName, userBind.getName()));

        log.info("账户绑定成功： {}", JSON.toJSONString(map));

        return "success";
    }

}
