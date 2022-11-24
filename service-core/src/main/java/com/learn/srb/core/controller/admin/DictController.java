package com.learn.srb.core.controller.admin;


import com.learn.common.result.R;
import com.learn.srb.core.pojo.dto.ExcelDictDTO;
import com.learn.srb.core.pojo.entity.Dict;
import com.learn.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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

    @ApiOperation(value = "导出数据")
    @PostMapping("export")
    public void exportDict(HttpServletResponse response) {
        List<Dict> dicts = dictService.list(); // 获取数据库中的数据

//        1.准备一个集合接收数据
        List<ExcelDictDTO> excelDictDTOS = new ArrayList<>();
        dicts.forEach(dict -> {
            ExcelDictDTO excelDict = new ExcelDictDTO(); //创建一个对象 接收这一行的数据
            excelDict.setId(dict.getId());
            excelDict.setParentId(dict.getParentId());
            excelDict.setName(dict.getName());
            excelDict.setDictCode(dict.getDictCode());
            excelDict.setValue(dict.getValue());

            excelDictDTOS.add(excelDict);  // 往表里加 一行
        });

    }

}

