package com.learn.srb.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.srb.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.srb.core.pojo.vo.BorrowInfoVO;

import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    void commitBorrow(BorrowInfo borrowInfo, String token);

    BigDecimal getBorrowAmount(String token);

    void listBorrowInfoVOs(Page<BorrowInfoVO> page);
}
