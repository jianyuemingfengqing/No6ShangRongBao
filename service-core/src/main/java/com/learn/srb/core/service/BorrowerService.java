package com.learn.srb.core.service;

import com.learn.srb.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.srb.core.pojo.vo.BorrowerDetailVO;
import com.learn.srb.core.pojo.vo.BorrowerVO;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrower(BorrowerVO borrowerVO, String token);

    BorrowerDetailVO getBorrowerDetail(String id);
}
