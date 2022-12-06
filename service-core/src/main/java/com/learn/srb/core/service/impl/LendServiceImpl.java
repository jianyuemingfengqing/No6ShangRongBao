package com.learn.srb.core.service.impl;

import com.learn.srb.core.pojo.entity.Lend;
import com.learn.srb.core.mapper.LendMapper;
import com.learn.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.learn.srb.core.service.LendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Service
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, String borrowId) {

    }
}
