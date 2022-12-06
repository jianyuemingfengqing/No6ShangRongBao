package com.learn.srb.core.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.srb.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.srb.core.pojo.vo.BorrowInfoVO;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {

    List<BorrowInfoVO> selectBorrowInfoVOs(Page<BorrowInfo> borrowInfoPage);

    BorrowInfoVO selectBorrowInfoVOById(String id);
}
