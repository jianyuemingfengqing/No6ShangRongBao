package com.learn.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.srb.core.listener.DictExcelDataListener;
import com.learn.srb.core.pojo.dto.ExcelDictDTO;
import com.learn.srb.core.pojo.entity.Dict;
import com.learn.srb.core.mapper.DictMapper;
import com.learn.srb.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
    @Resource
    RedisTemplate redisTemplate;

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
    @Cacheable(value = "dicts" , key = "'cache:'+{#id}") //拼接后的key为 dicts::cache:parentId的值
    public List<Dict> getDictByPid(String id) {
        // 怎么存到 就怎么查
/*        Object o = redisTemplate.opsForValue().get("dicts:" + id);
        if (o != null){
            // 如果缓存有数据, 就用缓存的
            return (List<Dict>)o;
        }*/

        List<Dict> list = this.list(
                new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, id)
        );
        list.forEach(dict -> {
            int count = this.count(
                    new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, dict.getId())
            );
            dict.setHasChildren(count > 0);
        });
//        redisTemplate.opsForValue().set("dicts:" + id, list);
        return list;
    }

    //DictServiceImpl
    @CacheEvict(value = "dicts" , key = "'cache:'+{#dict.parentId}")
    @Override
    public void updateDict(Dict dict) {
        this.updateById(dict);
    }
}
