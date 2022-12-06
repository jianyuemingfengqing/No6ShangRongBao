package com.learn.srb.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.srb.base.config.utils.JwtUtils;
import com.learn.srb.core.mapper.BorrowerMapper;
import com.learn.srb.core.pojo.entity.Borrower;
import com.learn.srb.core.pojo.entity.BorrowerAttach;
import com.learn.srb.core.pojo.entity.UserInfo;
import com.learn.srb.core.pojo.entity.UserIntegral;
import com.learn.srb.core.pojo.vo.BorrowerApprovalVO;
import com.learn.srb.core.pojo.vo.BorrowerDetailVO;
import com.learn.srb.core.pojo.vo.BorrowerVO;
import com.learn.srb.core.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    UserInfoService userInfoService;
    @Resource
    BorrowerAttachService borrowerAttachService;
    @Resource
    UserIntegralService userIntegralService;
    @Resource
    DictService dictService;

    @Override
    public void saveBorrower(BorrowerVO borrowerVO, String token) {
        Long userId = JwtUtils.getUserId(token);
        UserInfo userInfo = userInfoService.getById(userId);

        Borrower borrower = new Borrower();//借款人数据部分数据是登录用户实名认证的数据, 需要token
        BeanUtils.copyProperties(borrowerVO, borrower);       // 存储borrower中有的
        // 给borrower没有的字段中添加数据
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setUserId(userId);
        borrower.setMobile(userInfo.getMobile());
        borrower.setName(userInfo.getName());
        borrower.setStatus(1);//提交借款人数据后 等待 后台管理员认证
        this.save(borrower);
        //保存借款人附件数据
        List<BorrowerAttach> attaches = borrowerVO.getAttachs();

        if (!CollectionUtils.isEmpty(attaches)) {
            //为所有的附件对象设置借款人id
            attaches.forEach(attach -> {
                attach.setBorrowerId(borrower.getId());
            });
            borrowerAttachService.saveBatch(attaches);
        }
    }

    @Override
    public BorrowerDetailVO getBorrowerDetail(String id) {
        BorrowerDetailVO vo = new BorrowerDetailVO();
        //1、查询借款人数据
        Borrower borrower = this.getById(id);
        BeanUtils.copyProperties(borrower, vo);
        //2、完善借款人vo的 需要设置字符串的属性值
        vo.setSex(borrower.getSex() == 1 ? "男" : "女");
        vo.setMarry(borrower.getMarry() ? "是" : "否");
        switch (borrower.getStatus()) {
            case 1:
                vo.setStatus("认证中");
                break;
            case 2:
                vo.setStatus("认证成功");
                break;
            case -1:
                vo.setStatus("认证失败");
                break;
            default:
                vo.setStatus("未认证");
                break;
        }
        //borrower中存储的是学历的value值： 代表选中的学历的value值
        //使用学历的 dictCode值+ 选中的二级数据字典的 value值 查询 数据字典的name

        String education = dictService.getDictNameByDictCodeAndValue("education", borrower.getEducation());
        vo.setEducation(education);

        String income = dictService.getDictNameByDictCodeAndValue("income", borrower.getIncome());
        vo.setIncome(income);

        String industry = dictService.getDictNameByDictCodeAndValue("industry", borrower.getIndustry());
        vo.setIndustry(industry);

        String returnSource = dictService.getDictNameByDictCodeAndValue("returnSource", borrower.getReturnSource());
        vo.setReturnSource(returnSource);

        String relation = dictService.getDictNameByDictCodeAndValue("relation", borrower.getContactsRelation());
        vo.setContactsRelation(relation);
        //3、查询借款人的附件数据
        List<BorrowerAttach> borrowerAttaches = borrowerAttachService.list(
                Wrappers.lambdaQuery(BorrowerAttach.class)
                        .eq(BorrowerAttach::getBorrowerId, id));
        //遍历附件集合  修改每个附件的 imageLabel值方便前端遍历展示
        //图片类型imageType  （idCard1：身份证正面，idCard2：身份证反面，house：房产证，car：车）
        borrowerAttaches.forEach(borrowerAttach -> {
            switch (borrowerAttach.getImageType()) {
                case "idCard1":
                    borrowerAttach.setImageLabel("身份证正面");
                    break;
                case "idCard2":
                    borrowerAttach.setImageLabel("身份证反面");
                    break;
                case "house":
                    borrowerAttach.setImageLabel("房产证");
                    break;
                case "car":
                    borrowerAttach.setImageLabel("车");
                    break;
            }
        });

        vo.setAttaches(borrowerAttaches);
        return vo;
    }


    @Override
    public void approval(BorrowerApprovalVO vo) {
        //审批结果对应的表：
        //borrower+user_info: 借款人审批状态
        Borrower borrower = this.getById(vo.getBorrowerId());
        UserInfo userInfo = userInfoService.getById(borrower.getUserId());
        Integer status = vo.getStatus();
        //更新借款人表中的状态
        this.update(Wrappers.lambdaUpdate(Borrower.class)
                .eq(Borrower::getId, vo.getBorrowerId()) // 根据id查找借款人
                .set(Borrower::getStatus, status)); // 设置状态
        //更新用户表中的借款人认证状态
        userInfo.setBorrowAuthStatus(status);
        if (status == -1) { // 认证失败, 不需要执行下面操作
            userInfoService.updateById(userInfo);
            return;
        }
        //总积分
        Integer totalIntegral = 0;
        //获取user_id
        //user_integral: 保存审批的每一个积分的来源  方便以后细化控制用户的积分(使用、过期)
        Integer infoIntegral = vo.getInfoIntegral();
        //基本积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setIntegral(infoIntegral);
        userIntegral.setContent("基本信息积分");
        userIntegral.setUserId(borrower.getUserId());
        userIntegralService.save(userIntegral);
        totalIntegral += infoIntegral;

        Boolean isIdCardOk = vo.getIsIdCardOk();
        //身份认证积分: 30
        if (isIdCardOk) {
            userIntegral = new UserIntegral();
            userIntegral.setIntegral(30);
            userIntegral.setContent("身份信息积分");
            userIntegral.setUserId(borrower.getUserId());
            userIntegralService.save(userIntegral);
            totalIntegral += 30;
        }
        Boolean isCarOk = vo.getIsCarOk();
        //车辆认证积分: 60
        if (isCarOk) {
            userIntegral = new UserIntegral();
            userIntegral.setIntegral(60);
            userIntegral.setContent("车辆信息积分");
            userIntegral.setUserId(borrower.getUserId());
            userIntegralService.save(userIntegral);
            totalIntegral += 60;
        }
        Boolean isHouseOk = vo.getIsHouseOk();
        //房产认证积分: 100
        if (isHouseOk) {
            userIntegral = new UserIntegral();
            userIntegral.setIntegral(100);
            userIntegral.setContent("v信息积分");
            userIntegral.setUserId(borrower.getUserId());
            userIntegralService.save(userIntegral);
            totalIntegral += 100;
        }
        //user_info:  保存用户可用的总积分

        //userInfoService.update(Wrappers.lambdaUpdate(UserInfo))

        //修改总积分
        userInfo.setIntegral(userInfo.getIntegral() + totalIntegral);
        userInfoService.updateById(userInfo);
    }
}
