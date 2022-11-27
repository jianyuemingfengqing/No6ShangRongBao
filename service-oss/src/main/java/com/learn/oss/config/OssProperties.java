package com.learn.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.apache.naming.SelectorContext.prefix;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties implements InitializingBean {
    String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;

    public static String ENDPOINT;
    public static String ACCESSKEYID;
    public static String ACCESSKEYSECRET;
    public static String BUCKETNAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = this.endpoint;
        ACCESSKEYID = this.accessKeyId;
        ACCESSKEYSECRET = this.accessKeySecret;
        BUCKETNAME = this.bucketName;
    }
}
