package com.learn.srb.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {
    String host;
    String path;
    String method;
    String appcode;
    String templateId;
}
