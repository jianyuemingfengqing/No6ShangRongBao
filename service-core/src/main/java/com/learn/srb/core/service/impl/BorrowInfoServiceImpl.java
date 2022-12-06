package com.learn.srb.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.Assert;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.mapper.BorrowInfoMapper;
import com.learn.srb.core.pojo.entity.BorrowInfo;
import com.learn.srb.core.pojo.entity.Borrower;
import com.learn.srb.core.pojo.entity.IntegralGrade;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.learn.srb.core.pojo.vo.BorrowInfoVO;
import com.learn.srb.core.pojo.vo.BorrowerDetailVO;
import com.learn.srb.core.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    UserInfoService userInfoService;
    @Resource
    IntegralGradeService integralGradeService;
    @Resource
    DictService dictService;
    @Resource
    BorrowerService borrowerService;
    @Resource
    LendService lendService;

    @Override
    public BigDecimal getBorrowAmount(String token) {
        Long userId = JwtUtils.getUserId(token); // 解析token 获取用户id  并验证登录
        //查询用户的积分
        UserInfo userInfo = userInfoService.getById(userId);
        Integer integral = userInfo.getIntegral();
        //根据积分查询它所在的区间的借款额度
        // 用户积分>=start   <=end
        //SELECT * FROM integral_grade WHERE integral_start<=250  AND integral_end>=250;
        IntegralGrade integralGrade = integralGradeService.getOne(Wrappers.lambdaQuery(IntegralGrade.class)
                .le(IntegralGrade::getIntegralStart, integral)
                .ge(IntegralGrade::getIntegralEnd, integral));

        return integralGrade == null ? new BigDecimal("0") : integralGrade.getBorrowAmount();
    }

    @Override
    public void commitBorrow(BorrowInfo borrowInfo, String token) {
        Long userId = JwtUtils.getUserId(token);

//            判断用户账户是否正常：是否被锁定  是否已绑定汇付宝
        UserInfo userInfo = userInfoService.getById(userId);
        Assert.isTrue(userInfo.getStatus() != 1, ResponseEnum.LOGIN_LOCKED_ERROR);
        Assert.isTrue(userInfo.getBindStatus() != 1, ResponseEnum.USER_NO_BIND_ERROR);
        //借款人信息是否已经审核通过
        Assert.isTrue(userInfo.getBorrowAuthStatus() != 2, ResponseEnum.USER_NO_AMOUNT_ERROR);

//            判断用户 借款金额是否超过自己的额度
        IntegralGrade integralGrade = integralGradeService.getOne(
                Wrappers.lambdaQuery(IntegralGrade.class)
                        .le(IntegralGrade::getIntegralStart, userInfo.getIntegral())
                        .ge(IntegralGrade::getIntegralEnd, userInfo.getIntegral())
        );
        //当前用户允许的最大借款额度
        BigDecimal allowBorrowAmount = integralGrade.getBorrowAmount();
        Assert.isTrue(borrowInfo.getAmount().compareTo(allowBorrowAmount) == 1, ResponseEnum.USER_AMOUNT_LESS_ERROR);

        borrowInfo.setUserId(userId);
        //将年利率转为小数存储(以后获取时有可能需要*100)
        borrowInfo.setBorrowYearRate(borrowInfo.getBorrowYearRate().divide(new BigDecimal("100")));
        borrowInfo.setStatus(1);
        this.save(borrowInfo);
    }

    @Override
    public void listBorrowInfoVOs(Page<BorrowInfoVO> page) {
        /*获取数据
          查询borrowInfo的分页数据
          查询每个borrowInfo的borrower数据*/

      /*   mybatisplus 自定义mapper方法 如果传入了Page对象 它会自动使用page对象的分页条件查询数据
           1、根据泛型查询映射的表的记录条数
           2、page会自动计算 总页码  等数据
           3、分页条件会通过分页拦截器 自动追加到sql后面*/
        //自定义sql实现分页查询
        Page<BorrowInfo> borrowInfoPage = new Page<>(page.getCurrent(), page.getSize());
        //返回的结果是查询到的BorrowInfoVO集合
        List<BorrowInfoVO> borrowInfoVOS = baseMapper.selectBorrowInfoVOs(borrowInfoPage);
        //遍历修改每一个借款信息的  还款方式+用途
        borrowInfoVOS.forEach(borrowInfoVO -> {
            borrowInfoVO.setMoneyUse(
                    dictService.getDictNameByDictCodeAndValue(
                            "moneyUse", Integer.parseInt(borrowInfoVO.getMoneyUse())
                    )
            );
            borrowInfoVO.setReturnMethod(
                    dictService.getDictNameByDictCodeAndValue(
                            "returnMethod", Integer.parseInt(borrowInfoVO.getReturnMethod())
                    )
            );
        });

        page.setRecords(borrowInfoVOS);
        page.setTotal(borrowInfoPage.getTotal());
        page.setPages(borrowInfoPage.getPages());
    }

    @Override
    public Map<String, Object> getBorrowInfo(String id) {
        //根据borrowInfo借款id获取借款信息
        BorrowInfoVO borrowInfoVO = baseMapper.selectBorrowInfoVOById(id);
        borrowInfoVO.setMoneyUse(
                dictService.getDictNameByDictCodeAndValue(
                        "moneyUse", Integer.parseInt(borrowInfoVO.getMoneyUse())
                )
        );
        borrowInfoVO.setReturnMethod(
                dictService.getDictNameByDictCodeAndValue(
                        "returnMethod", Integer.parseInt(borrowInfoVO.getReturnMethod())
                )
        );
        //获取borrower借款人用户id
        Long userId = borrowInfoVO.getUserId();
        //根据用户id获取借款人id
        Borrower borrower = borrowerService.getOne(
                Wrappers.lambdaQuery(Borrower.class)
                        .eq(Borrower::getUserId, userId)
                        .select(Borrower::getId)
        );
        //获取借款人详情
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetail(borrower.getId().toString());

        Map<String, Object> map = new HashMap<>();
        map.put("borrowInfoVO", borrowInfoVO);
        map.put("borrowerDetailVO", borrowerDetailVO);
        return map;
    }

    @Override
    public void borrowInfoApproval(BorrowInfoApprovalVO borrowInfoApprovalVO) {
        //更新借款状态
        Integer status = borrowInfoApprovalVO.getStatus();
        String borrowId = borrowInfoApprovalVO.getBorrowId();
        this.update(Wrappers.lambdaUpdate(BorrowInfo.class)
                .eq(BorrowInfo::getId , borrowId)
                .set(BorrowInfo::getStatus , status));
        //判断如果审批通过  将审批信息转为标的保存
        if(status==2){//审批通过
            lendService.createLend(borrowInfoApprovalVO, borrowId);
        }
    }

}
