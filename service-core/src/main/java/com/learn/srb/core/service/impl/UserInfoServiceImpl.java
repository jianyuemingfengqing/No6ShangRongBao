package com.learn.srb.core.service.impl;

import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.Assert;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.mapper.UserInfoMapper;
import com.learn.srb.core.pojo.vo.UserRegisterVO;
import com.learn.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public void register(UserRegisterVO userRegisterVO) {
        //1、获取参数
        String code = userRegisterVO.getCode();
        String mobile = userRegisterVO.getMobile();
        String password = userRegisterVO.getPassword();
        Integer userType = userRegisterVO.getUserType();
        // 校验非空
        Assert.strNotNull(code , ResponseEnum.CODE_NULL_ERROR);
        Assert.strNotNull(mobile , ResponseEnum.MOBILE_NULL_ERROR);
        Assert.strNotNull(password , ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notNull(userType ,ResponseEnum.USER_TYPE_NULL_ERROR );
    }
}
