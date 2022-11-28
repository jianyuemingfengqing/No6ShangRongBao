package com.learn.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.learn.common.exception.BusinessException;
import com.learn.common.result.R;
import com.learn.common.result.ResponseEnum;
import com.learn.common.utils.Assert;
import com.learn.srb.core.pojo.dto.ExcelDictDTO;
import com.learn.srb.core.pojo.entity.Dict;
import com.learn.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
public class AdminDictController {
    @Resource
    DictService dictService;

    @ApiOperation(value = "导入数据")
    @PostMapping("import")
    public R importDict(MultipartFile file) {
        dictService.importDicts(file);
        return R.ok();
    }

    @ApiOperation(value = "导出数据")
    @GetMapping("export")
    public void exportDict(HttpServletResponse response) {
        try {
        List<Dict> dicts = dictService.list(); // 获取数据库中的数据

//        1.准备一个集合接收数据
       /* List<ExcelDictDTO> excelDictDTOS = new ArrayList<>();
        dicts.forEach(dict -> {
            ExcelDictDTO excelDict = new ExcelDictDTO(); //创建一个对象 接收这一行的数据
            excelDict.setId(dict.getId());
            excelDict.setParentId(dict.getParentId());
            excelDict.setName(dict.getName());
            excelDict.setDictCode(dict.getDictCode());
            excelDict.setValue(dict.getValue());

            excelDictDTOS.add(excelDict);  // 往表里加 一行
        });*/
        // 使用流
        List<ExcelDictDTO> excelDictDTOList = dicts.stream().map(dict -> {
            ExcelDictDTO excelDict = new ExcelDictDTO(); //创建一个对象 接收这一行的数据
            excelDict.setId(dict.getId());
            excelDict.setParentId(dict.getParentId());
            excelDict.setName(dict.getName());
            excelDict.setDictCode(dict.getDictCode());
            excelDict.setValue(dict.getValue());
            return excelDict;
        }).collect(Collectors.toList());

//        将数据写到响应体中
        response.setHeader("Content-disposition"
                , "attachment;filename=dicts"
                        + new DateTime().toString("_yyyy_MM_dd_HH_mm")
                        + ExcelTypeEnum.XLSX.getValue()
        );
/*        try {*/
            EasyExcel.write(response.getOutputStream())
                    .head(ExcelDictDTO.class)
                    .sheet("数据字典")
                    .doWrite(excelDictDTOList);
        } catch (Exception e) {
         /*   throw new RuntimeException(e);*/
            throw  new BusinessException(ResponseEnum.EXPORT_DATA_ERROR,e);

        }

    }

    @ApiOperation(value = "根据父id查询")
    @GetMapping("getDictByPid/{id}")
    public R getDictByPid(@ApiParam(value = "id值", required = true) @PathVariable("id") String id) {
        List<Dict> dictByPid = dictService.getDictByPid(id);

        Assert.notNull(dictByPid, -1, "没有子节点");
        return R.ok().data("items", dictByPid);
    }

    //AdminDictController
    @ApiOperation(value = "更新数据字典")
    @PutMapping("updateDict")
    public R updateDict(@RequestBody Dict dict){
        dictService.updateDict(dict);
        return R.ok();
    }
}

