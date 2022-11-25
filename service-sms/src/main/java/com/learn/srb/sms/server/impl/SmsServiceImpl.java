package com.learn.srb.sms.server.impl;

import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.HttpUtils;
import com.learn.common.utils.RegexValidateUtils;
import com.learn.srb.sms.server.SmsService;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendMsg(String mobile, Integer type) {
        // 验证手机号
        boolean b = RegexValidateUtils.checkCellphone(mobile);
        if (!b){
            throw new BusinessException(ResponseEnum.MOBILE_ERROR);
        }

        // 发送短信
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "421a1ed5766f44739bc4b2b88096d41e";
        Map<String, String> headers = new HashMap<String, String>();

        headers.put("Authorization", "APPCODE " + appcode);

        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:1234");
        bodys.put("phone_number", "13575432126");
        bodys.put("template_id", "TPL_0000");


        try {

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());

        } catch (Exception e) {
        }
    }
}
