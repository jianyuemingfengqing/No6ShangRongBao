package com.learn.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.Assert;
import com.learn.common.utils.MD5;
import com.learn.srb.base.config.constants.SrbCons;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.mapper.UserInfoMapper;
import com.learn.srb.core.pojo.vo.UserRegisterVO;
import com.learn.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    StringRedisTemplate stringRedisTemplate;//使用存验证码的redis模板类对象来获取

    @Override
    public void register(UserRegisterVO userRegisterVO) {
        //1、获取参数
        String code = userRegisterVO.getCode();
        String mobile = userRegisterVO.getMobile();
        String password = userRegisterVO.getPassword();
        Integer userType = userRegisterVO.getUserType();
        // 校验非空
        Assert.strNotNull(code, ResponseEnum.CODE_NULL_ERROR);
        Assert.strNotNull(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.strNotNull(password, ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notNull(userType, ResponseEnum.USER_TYPE_NULL_ERROR);

        //2、验证验证码
        String codeKey = SrbCons.SMS_CODE_PREFIX + mobile + ":" + 0;
        String redisCode = stringRedisTemplate.opsForValue().get(codeKey);
        Assert.strNotEq(code, redisCode, ResponseEnum.CODE_ERROR);

        //3、重复注册验证
        int count = this.count(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, mobile)
                .eq(UserInfo::getUserType, userType));
        if (count > 0) {
            //手机号码已被注册
            throw new BusinessException(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        //4、密码加密: 加盐加密可以保证密码不会被暴力破解
        //真实开发  一个用户一个盐
        //生成盐
        String salt = UUID.randomUUID().toString().substring(0, 6);
        String encrypt = MD5.encrypt(MD5.encrypt(password) + salt);

        //5、存到数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(encrypt);//使用加密的密码设置
        userInfo.setMobile(mobile);
        userInfo.setUserType(userType);
        userInfo.setSalt(salt);
        userInfo.setHeadImg("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        userInfo.setNickName("谷粉" + UUID.randomUUID().toString().substring(0, 6));
        this.save(userInfo);
        //删除redis中缓存的验证码
        stringRedisTemplate.delete(codeKey);
    }
}
