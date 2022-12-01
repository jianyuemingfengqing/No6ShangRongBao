package com.learn.srb.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.srb.core.pojo.vo.UserInfoSearchVO;
import com.learn.srb.core.pojo.vo.UserRegisterVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(UserRegisterVO userRegisterVO);

    String login(UserInfo userInfo, HttpServletRequest request);

    void listUserInfos(Page<UserInfo> page, UserInfoSearchVO userInfoSearchVO);
}
