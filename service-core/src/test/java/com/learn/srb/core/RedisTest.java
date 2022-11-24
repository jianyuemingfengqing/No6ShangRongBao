package com.learn.srb.core;

import com.learn.srb.core.pojo.entity.Dict;
import com.learn.srb.core.service.DictService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {
    @Autowired
    DictService dictService;
    //springboot自动配置原理：
    //装配redis的模板类对象
    //RedisTemplate键和值泛型都是Object
    @Autowired
    RedisTemplate redisTemplate;
    //StringRedisTemplate继承了RedisTemplate键和值泛型都是String
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //存数据到redis中
    @Test
    public void testWrite(){
        List<Dict> dicts = dictService.getDictByPid("1");
/*        数据类型：
         string: 序列化的方式来保存字符串  最大支持512m
                  用于库存、分布式锁、验证码、对象对象集合转为json字符串
                         incr/decr 原子性进行计算   setnx: 分布式锁   setex:设置过期时间
         list： 底层是双向链表  两端操作效率高
                  用于  队列(lpush/rpop)   按照添加顺序有序  可以重复
         set: 底层为 value值设置null的hash结构 ，set存数据的特点和hash一样，无序不重复    抽奖号码/登录用户统计
         zset： 底层为value值存分数的hash结构，可以按照分数区间排名查询  topN
         hash： 一个hash结构可以存多对k-v的数据  用来存复杂的数据(对象)
         */

   /*     redisTemplate：使用序列化的方式存对象 所以存入的类必须实现序列化接口
                      对象类型存取时不需要手动转换*/
        redisTemplate.opsForValue().set("dicts" , dicts , 20 , TimeUnit.MINUTES);
/*        stringRedisTemplate:对象类型需要我们转为json字符串才可以存入
                           存入的数据不需要实现序列化接口*/
//        stringRedisTemplate.opsForValue().set("dicts" , JSON.toJSONString(dicts) , 2 , TimeUnit.HOURS);
    }
    //从redis中读取数据
    @Test
    public void testRead(){
        Object dicts = redisTemplate.opsForValue().get("dicts");
        System.out.println(dicts.getClass().getName());
        System.out.println(dicts);
//        String dictsStr = stringRedisTemplate.opsForValue().get("dicts");
//        List<Dict> dicts2 = JSON.parseArray(dictsStr, Dict.class);
//        System.out.println(dicts2);
    }

    //改造RedisTemplate
    //1、存数据时 自动将数据转为json字符串存入到redis(redis客户端中查询的数据可读、字符串已经实现了序列化接口)
    //2、读数据时 自动将json转为它对应类型的对象/对象集合
    //所有的单元测试执行前 为Redistemplate配置键和值的序列化器
//    @BeforeAll  在单元测试中的 静态方法上添加 类被加载时 该方法会执行
    @BeforeEach  //在单元测试成员方法上添加，每个单元测试执行前 该方法会被调用
    public void initRedisTemplate(){
        //给string类型的操作 设置的键 提供键的序列化器(转为字符串存入)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //给string类型的操作 设置的值 提供值的序列化器(将对象转为json同时将对象的class设置到json中)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

//        redisTemplate.setHashKeySerializer();
//        redisTemplate.setHashValueSerializer();
    }
    /*
    存对象数据到redis中：使用hash结构来存储
        用户对象： username=fangfang   age=23   gender=女   id=1
            key     属性名1  属性值1
                    属性名2  属性值2
                    属性名3  属性值3
     */

    @Test
    void testHash(){
//        redisTemplate.boundHashOps("key");  根据redis中的key获取hash结构   就可以使用类似map的方式操作它里面的数据
        BoundHashOperations hashOps = redisTemplate.boundHashOps("user:1");
//        Set keys = hashOps.keys();
//        hashOps.values();
//        hashOps.entries();
//        hashOps.put(p,val); 存入一个属性和值
//        hashOps.get(p) 获取属性值
//        hashOps.hasKey(p) 判断是否包含某个属性
//        hashOps.size() entry的个数
        if(hashOps.size()==0){
            System.out.println("user:1的数据还未初始化");
            hashOps.put("username","fangfang");
            hashOps.put("age","23");
            hashOps.put("gender","女");
            hashOps.put("id","1");
        }
        Set keys = hashOps.keys();
        System.out.println(keys);
        System.out.println(hashOps.values());
        hashOps.entries().forEach((k,v)->{
            System.out.println(k+" = "+ v );
        });

    }

}