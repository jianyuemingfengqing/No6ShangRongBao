package com.learn.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.Assert;
import com.learn.common.utils.MD5;
import com.learn.srb.base.config.constants.SrbConsts;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.mapper.UserInfoMapper;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.entity.UserLoginRecord;
import com.learn.srb.core.pojo.vo.UserInfoSearchVO;
import com.learn.srb.core.pojo.vo.UserRegisterVO;
import com.learn.srb.core.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        //1、验证参数
        String code = userRegisterVO.getCode();
        String mobile = userRegisterVO.getMobile();
        String password = userRegisterVO.getPassword();
        Integer userType = userRegisterVO.getUserType();
        Assert.strNotNull(code, ResponseEnum.CODE_NULL_ERROR);
        Assert.strNotNull(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.strNotNull(password, ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notNull(userType, ResponseEnum.USER_TYPE_NULL_ERROR);
        //2、验证验证码
        String codeKey = SrbConsts.SMS_CODE_PREFIX + mobile + ":" + 0;
        stringRedisTemplate.opsForValue().set(codeKey, code);
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

    @Override
    public String login(UserInfo userInfo, HttpServletRequest request) {
        //获取请求参数
        String mobile = userInfo.getMobile();
        String password = userInfo.getPassword();
        Integer userType = userInfo.getUserType();
        Assert.strNotNull(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.strNotNull(password, ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notNull(userType, ResponseEnum.USER_TYPE_NULL_ERROR);

        //处理登录业务
        //1、判断账号是否存在
        UserInfo user = this.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, mobile)
                .eq(UserInfo::getUserType, userType));
        Assert.notNull(user, ResponseEnum.LOGIN_MOBILE_ERROR);

        //2、如果账号存在 使用账号的盐对登录的密码进行加密 和账号的密文密码进行比较  判断密码
        String encodedPwd = user.getPassword();//数据库中加密的密码
        //获取盐
        String salt = user.getSalt();
        //对登录密码加密
        String encrypt = MD5.encrypt(MD5.encrypt(password) + salt);
        Assert.strNotEq(encrypt, encodedPwd, ResponseEnum.LOGIN_PASSWORD_ERROR);

        //优化：
        //判断用户的状态是否正常
//        if(user.getStatus()!=1){
//
//        }
        Assert.isTrue(user.getStatus() != 1, ResponseEnum.LOGIN_LOCKED_ERROR);
        //保存登录成功的日志：登录时的ip+时间
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(user.getId());
//        System.out.println(request.getRemoteHost());
//        System.out.println(request.getRequestURI().toString());
        String ip = request.getHeader("remote-host");
        userLoginRecord.setIp(ip);

        //3、登录成功：构建jwt字符串返回
        String token = JwtUtils.createToken(user.getId(), user.getNickName());
        return token;
    }

    @Override
    public void listUserInfos(Page<UserInfo> page, UserInfoSearchVO userInfoSearchVO) {
        // 使用包装类是因为 基本返回类型默认值为0 不好处理, 而包装类基本返回类型为空
        // 获取参数
        Integer userType = userInfoSearchVO.getUserType();
        String mobile = userInfoSearchVO.getMobile();
        Integer status = userInfoSearchVO.getStatus();

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(mobile)) {
            queryWrapper.like(UserInfo::getMobile, mobile);
        }
        if (status != null) {
            queryWrapper.eq(UserInfo::getStatus, status);
        }
        if (userType != null) {
            queryWrapper.eq(UserInfo::getUserType, userType);
        }
        this.page(page, queryWrapper);
    }
}
