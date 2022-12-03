package com.learn.srb.core.service;

import com.learn.srb.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.srb.core.pojo.vo.UserBindVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface UserBindService extends IService<UserBind> {

    String notifyUrl(HttpServletRequest request);

    String bindAccount(UserBindVO userBindVO, String token);
}
