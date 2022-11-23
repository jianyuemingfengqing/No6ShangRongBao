package com.learn.srb.core.controller.admin;


import com.learn.common.result.R;
import com.learn.common.utils.Assert;
import com.learn.srb.core.pojo.entity.IntegralGrade;
import com.learn.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Api(tags = "数据字典后台管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/core/dict")
public class DictController {
    @Resource
    DictService dictService;
    @ApiOperation(value = "导入数据")
    @PostMapping("import")
    public R importDict(MultipartFile file) {
        dictService.importDicts(file);
        return R.ok();
    }

}

