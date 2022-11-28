package com.learn.srb.core.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.common.result.R;
import com.learn.common.utils.Assert;
import com.learn.srb.core.mapper.IntegralGradeMapper;
import com.learn.srb.core.pojo.entity.IntegralGrade;
import com.learn.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static sun.management.Agent.error;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@Slf4j
@Api(tags = "积分等级管理模块")
@CrossOrigin
//@RequestMapping("/integralGrade")
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    @Resource
    IntegralGradeService integralGradeService;
    @Resource
    IntegralGradeMapper integralGradeMapper;

    //查所有
    @GetMapping
    @ApiOperation(value = "积分等级列表")
    public R list() {
        List<IntegralGrade> integralGradeList = integralGradeService.list();

      /*  log.error("{}的级别, 当前时间{}", "debug", new Date());
        log.info("{}级别日志, 当前时间{}", "info", new Date());
        log.warn("{}级别日志, 当前时间{}", "warn", new Date());
        log.error("{}级别日志, 时间{}", "error", new Date());*/
/*        if (integralGradeList == null) {
            return R.error();
        }*/
        Assert.notNull(integralGradeList, -1, "未能查询到数据");
        return R.ok().data("items", integralGradeList);

    }

    //查一个
    @ApiOperation(value = "id查询积分等级")
    @GetMapping("{id}")
    public R getById(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        IntegralGrade integralGradeServiceById = integralGradeService.getById(id);

    /*    if (integralGradeServiceById == null) {
            return R.error();
        }*/
        Assert.notNull(integralGradeServiceById, -1, "没有该用户");
        return R.ok().data("item", integralGradeServiceById);
    }

    @ApiOperation(value = "id删除积分等级")
    @DeleteMapping("{id}")
    public R delById(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        boolean result = integralGradeService.removeById(id);
 /*       if (result) {
            return R.ok();
        }
        return R.error().message("删除失败");*/
        Assert.notTrue(result, -1, "删除失败");
        return R.ok().message("删除成功");
    }

    @ApiOperation(value = "批量删除积分等级")
    @DeleteMapping("batchDel")
    public R batchDel(@RequestParam List<String> id) {
        boolean result = integralGradeService.removeByIds(id);

        Assert.notTrue(result, -1, "删除失败");
        return R.ok().message("删除成功");
    }

    @ApiOperation(value = "更新积分等级", notes = "必须提供id")
    @PutMapping
    public R update(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.updateById(integralGrade);

/*        if (result) {
            return R.ok();
        }
        return R.error().message("更新失败");*/
        Assert.notTrue(result, -1, "更新失败");
        return R.ok();
    }

    @ApiOperation(value = "新增积分等级")
    @PostMapping
    public R save(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.save(integralGrade);

/*        if (result) {
            return R.ok();
        }
        return R.error().message("新增失败");*/
        Assert.notTrue(result, -1, "新增失败");
        return R.ok();
    }

    @ApiOperation(value = "恢复数据")
    @PutMapping("regain")
    public R update() {

        integralGradeMapper.regain();
//        Assert.notTrue(result, -1, "更新失败");
        return R.ok();
    }

    //    分页
    @ApiOperation(value = "分页查询积分等级")
    @GetMapping("/page")
    public R getIntegralGradeList(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
    ) {
        Page<IntegralGrade> integralGradePage = new Page<>(pageNum, pageSize);
        Page<IntegralGrade> integralGradeList = integralGradeMapper.selectPage(integralGradePage, null);
        return R.ok().data("items", integralGradeList);

    }
}

