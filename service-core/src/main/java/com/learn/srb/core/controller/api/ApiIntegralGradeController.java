package com.learn.srb.core.controller.api;


import com.learn.srb.core.pojo.entity.IntegralGrade;
import com.learn.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@Api(tags = "积分等级管理模块")
//@RequestMapping("/integralGrade")
@RequestMapping("/api/integralGrade")
public class ApiIntegralGradeController {
    @Resource
    IntegralGradeService integralGradeService;

    //查所有
    @GetMapping
    @ApiOperation(value = "积分等级列表")
    public List<IntegralGrade> list() {
        List<IntegralGrade> integralGradeList = integralGradeService.list();
        return integralGradeList;
    }

    //查一个
    @ApiOperation(value = "id查询积分等级")
    @GetMapping("{id}")
    public IntegralGrade getById(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        IntegralGrade integralGradeServiceById = integralGradeService.getById(id);
        return integralGradeServiceById;
    }

    @ApiOperation(value = "id删除积分等级")
    @DeleteMapping("{id}")
    public Boolean delById(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        boolean result = integralGradeService.removeById(id);
        return result;
    }

    @ApiOperation(value = "更新积分等级", notes = "必须提供id")
    @PutMapping
    public Boolean update(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.updateById(integralGrade);
        return result;
    }

    @ApiOperation(value = "新增积分等级")
    @PostMapping
    public Boolean save(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.save(integralGrade);
        return result;
    }
}

