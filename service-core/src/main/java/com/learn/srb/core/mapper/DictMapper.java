package com.learn.srb.core.mapper;

import com.learn.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface DictMapper extends BaseMapper<Dict> {

    List<Dict> selectDictsByDictCode(String dictCode);
}
