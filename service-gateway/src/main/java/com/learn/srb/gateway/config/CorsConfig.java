package com.learn.srb.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        //UrlBasedCorsConfigurationSource 可以对包含通配符的路径 配置允许跨域
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); //允许哪些服务器来跨域访问   * 通配所有
        config.addAllowedMethod("*"); //允许哪些请求方式跨域访问
        config.addAllowedHeader("*");//允许携带哪些请求头跨域访问
        config.setAllowCredentials(true);//允许携带cookie跨域访问
        configSource.registerCorsConfiguration("/**" , config);
        CorsWebFilter filter = new CorsWebFilter(configSource);
        return  filter;
    }
}
