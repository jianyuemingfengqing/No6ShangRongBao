package com.learn.srb.core.controller.api;


import com.learn.common.result.R;
import com.learn.srb.core.pojo.entity.Dict;
import com.learn.srb.core.service.DictService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@RestController
@RequestMapping("/api/core/dict")
public class ApiDictController {
    @Resource
    DictService dictService;
    @ApiOperation("根据dictCode查询下一级数据字典")
    @GetMapping("getDictsByDictCode/{dictCode}")
    public R getDictsByDictCode(@PathVariable("dictCode") String dictCode){
        List<Dict> dicts = dictService.getDictsByDictCode(dictCode);
        return R.ok().data("items",dicts);
    }
}

