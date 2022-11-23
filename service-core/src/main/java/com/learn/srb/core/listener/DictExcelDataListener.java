package com.learn.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.srb.core.mapper.DictMapper;
import com.learn.srb.core.pojo.dto.ExcelDictDTO;
import com.learn.srb.core.pojo.entity.Dict;

import javax.annotation.Resource;

public class DictExcelDataListener extends AnalysisEventListener<ExcelDictDTO> {

//    @Resource
    DictMapper dictMapper;

    public DictExcelDataListener(DictMapper baseMapper) {
        this.dictMapper = baseMapper;
    }

    @Override
    public void invoke(ExcelDictDTO excelDictDTO, AnalysisContext analysisContext) {
        Integer count = dictMapper.selectCount(
                // 一行一行读取
                // 比较数据库中的id 与 excel这一行中的id值 相不相同
                new LambdaQueryWrapper<Dict>().eq(Dict::getId, excelDictDTO.getId())
        );
        if (count == 0) {
            // 如果id值不同, 说明在数据中没有, 需要保存到数据库
            Dict dict = new Dict();
            // 字段名可能不一样吗, 保险起见一个一个赋值
            dict.setId(excelDictDTO.getId());
            dict.setName(excelDictDTO.getName());
            dict.setDictCode(excelDictDTO.getDictCode());
            dict.setParentId(excelDictDTO.getParentId());
            if (excelDictDTO.getValue() != null) {
                // excel表格 有空值的情况
                dict.setValue(excelDictDTO.getValue());
            }
            dictMapper.insert(dict);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
