package com.learn.srb.core.controller.admin;


import com.learn.common.result.R;
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
//@RequestMapping("/integralGrade")
@RequestMapping("/admin/integralGrade")
public class IntegralGradeController {
    @Resource
    IntegralGradeService integralGradeService;

    //查所有
    @GetMapping
    @ApiOperation(value = "积分等级列表")
    public R list() {
        List<IntegralGrade> integralGradeList = integralGradeService.list();
        log.debug("{}级别日志" , "debug");
        log.info("{}级别日志" , "info");
        log.warn("{}级别日志" , "warn");
        log.error("{}级别日志" , "error");
        if (integralGradeList == null) {
            return R.error();
        }
        return R.ok().data("items",integralGradeList);

    }

    //查一个
    @ApiOperation(value = "id查询积分等级")
    @GetMapping("{id}")
    public R getById(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        IntegralGrade integralGradeServiceById = integralGradeService.getById(id);
        if (integralGradeServiceById == null) {
            return R.error();
        }
        return R.ok().data("item",integralGradeServiceById);
    }

    @ApiOperation(value = "id删除积分等级")
    @DeleteMapping("{id}")
    public R delById(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        boolean result = integralGradeService.removeById(id);
        if (result) {
            return R.ok();
        }
        return R.error().message("删除失败");
    }

    @ApiOperation(value = "更新积分等级", notes = "必须提供id")
    @PutMapping
    public R update(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.updateById(integralGrade);

        if (result) {
            return R.ok();
        }
        return R.error().message("更新失败");
    }

    @ApiOperation(value = "新增积分等级")
    @PostMapping
    public R save(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.save(integralGrade);

        if (result) {
            return R.ok();
        }
        return R.error().message("新增失败");
    }
}

