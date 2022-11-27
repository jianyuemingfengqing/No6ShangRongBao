package com.learn.srb.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {
    String host;
    String path;
    String method;
    String appcode;
    String templateId;

    public static String HOST;
    public static String PATH;
    public static String METHOD;
    public static String APPCODE;
    public static String TEMPLATE_ID;

    @PostConstruct
    public void init() {
        HOST = this.host;
        PATH = this.path;
        METHOD = this.method;
        APPCODE = this.appcode;
        TEMPLATE_ID = this.templateId;
    }
}
