package com.learn.srb.sms.server.impl;

import com.alibaba.fastjson.JSON;
import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.HttpUtils;
import com.learn.common.utils.RandomUtils;
import com.learn.common.utils.RegexValidateUtils;
import com.learn.srb.sms.server.SmsService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendMsg(String mobile, Integer type) {
        // 验证手机号
        boolean b = RegexValidateUtils.checkCellphone(mobile);
        if (!b) {
            throw new BusinessException(ResponseEnum.MOBILE_ERROR);
        }

        // 发送短信

        try {
            String host = "https://dfsns.market.alicloudapi.com";
            String path = "/data/send_sms";
            String method = "POST";
            String appcode = "421a1ed5766f44739bc4b2b88096d41e";
            Map<String, String> headers = new HashMap<String, String>();

            headers.put("Authorization", "APPCODE " + appcode);

            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();

            Map<String, String> bodys = new HashMap<String, String>();

            String fourBitRandom = RandomUtils.getFourBitRandom();
            bodys.put("content", "code:" + fourBitRandom);
            bodys.put("phone_number", mobile);
            bodys.put("template_id", "TPL_0000");


            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            // 接受响应的数据
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map map = JSON.parseObject(result, Map.class);
            if ("OK".equals(map.get("status"))) {
                // 短信发送成功

            } else {
                throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR);
            }

        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR,e);
        }
    }
}
