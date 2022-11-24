package com.learn.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.srb.core.listener.DictExcelDataListener;
import com.learn.srb.core.pojo.dto.ExcelDictDTO;
import com.learn.srb.core.pojo.entity.Dict;
import com.learn.srb.core.mapper.DictMapper;
import com.learn.srb.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public void importDicts(MultipartFile file) {
        try {
            InputStream fileInputStream = file.getInputStream(); //将excel中的数据转为流
            EasyExcel.read(fileInputStream)
                    .sheet(0)
                    .head(ExcelDictDTO.class)
                    .registerReadListener(new DictExcelDataListener(baseMapper))
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Dict> getDictByPid(String id) {
        List<Dict> list = this.list(
                new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, id)
        );
        list.forEach(dict -> {
            int count = this.count(
                    new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, dict.getId())
            );
            dict.setHasChildren(count > 0);
        });
        return list;
    }
}
