package com.learn.srb.core.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;

@Configuration
@EnableCaching //启用缓存管理
public class RedisConfig {
    @Resource
    RedisTemplate redisTemplate;

    //RedisConfig对象初始化后 立即为redisTemplate设置键和值的序列化器
    @PostConstruct //该注解标注的方法会在当前类的构造器调用后立即执行
    public void init() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    //缓存管理器
    //方法被容器调用时会自动从容器中获取commons-pool2 配置好的redis连接池对象
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        //RedisCacheManager.create()直接创建RedisCacheManager对象返回
        // RedisCacheManager.builder()返回RedisCacheManager的构建器(RedisCacheManagerBuilder) 可以继续配置RedisCacheManager

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))//设置自动缓存到redis中数据的过期时间
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer())) //设置键的序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer())) //设置值的序列化器
//                .disableCachingNullValues()//禁用缓存空值  默认缓存空值
                ;
        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(configuration)
                .build();
        return cacheManager;
    }
}