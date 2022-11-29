package com.learn.srb.sms.server.impl;

import com.alibaba.fastjson.JSON;
import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.HttpUtils;
import com.learn.common.utils.RandomUtils;
import com.learn.common.utils.RegexValidateUtils;
import com.learn.srb.base.config.constants.SrbConsts;
import com.learn.srb.sms.config.SmsProperties;
import com.learn.srb.sms.server.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SmsProperties smsProperties;

    @Override
    public void sendMsg(String mobile, Integer type) {
        // 验证手机号
        boolean b = RegexValidateUtils.checkCellphone(mobile);
        if (!b) {
            throw new BusinessException(ResponseEnum.MOBILE_ERROR);
        }

        // 发送短信
        //时间
        String timesKey =  SrbConsts.SMS_TIMES_PREFIX + mobile + ":" + type;
        //次数
        String countKey = SrbConsts.SMS_COUNT_PREFIX  + mobile + ":" + type;
        //验证码的key
        String codeKey = SrbConsts.SMS_CODE_PREFIX + mobile + ":" + type;

        //2.1 2分钟内不能重复获取
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(timesKey))) {
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);
        }
        //2.2 一天内只能获取3条
        String countStr = stringRedisTemplate.opsForValue().get(countKey);
        if (StringUtils.isNotEmpty(countStr) && Integer.parseInt(countStr) >= 3) {
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_COUNTS_CONTROL_ERROR);
        }

        try {
            String host = smsProperties.HOST;
            String path = smsProperties.PATH;
            String method = smsProperties.METHOD;
            String appcode = smsProperties.APPCODE;
            Map<String, String> headers = new HashMap<String, String>();

            headers.put("Authorization", "APPCODE " + appcode);
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            Map<String, String> bodys = new HashMap<String, String>();

            String fourBitRandom = RandomUtils.getFourBitRandom();
            querys.put("content", "code:" + fourBitRandom);
            querys.put("phone_number", mobile);
            querys.put("template_id", smsProperties.TEMPLATE_ID);


//            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            // 接受响应的数据
//            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
//            List<Map> list = JSON.parseArray(result, Map.class);
//            Map map = JSON.parseObject(result, Map.class);
//            Map map = list.get(0);
//            if ("OK".equals(map.get("status"))) {
                // 短信发送成功
                // 存储验证码
                stringRedisTemplate.opsForValue().set(countKey, fourBitRandom, 20, TimeUnit.MINUTES);
                //更新验证码发送次数
                //2分钟内不能重复获取验证码
                stringRedisTemplate.opsForValue().set(timesKey, "1", 2, TimeUnit.MINUTES);

                if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(countKey))) {// 判断是否是重复获取
                    //如果是第N次获取 在之前的次数上+1
                    stringRedisTemplate.opsForValue().increment(countKey);
                } else {
                    //如果第一次获取验证码 , 之前的过期了
                    stringRedisTemplate.opsForValue().set(countKey,"1",1,
                            TimeUnit.DAYS);
                }

//            } else {
//                throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR);
//            }

        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR, e);
        }
    }
}
