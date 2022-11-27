package com.learn.srb.sms;

import com.learn.srb.sms.config.SmsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PropertiesTest {
    @Test
    public void testpropertis(){
        System.out.println(SmsProperties.APPCODE);
        System.out.println(SmsProperties.PATH);
        System.out.println(SmsProperties.TEMPLATE_ID);
    }
}
