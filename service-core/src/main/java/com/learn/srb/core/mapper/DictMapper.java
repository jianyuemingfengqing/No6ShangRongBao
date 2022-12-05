package com.learn.srb.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.srb.core.pojo.entity.Dict;
import org.apache.ibatis.annotations.Param;

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

    String selectDictNameByDictCodeAndValue(@Param("dictCode") String dictCode, @Param("value")Integer value);
}
