package com.learn.srb.core.mapper;

import com.learn.srb.core.pojo.entity.IntegralGrade;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 积分等级表 Mapper 接口
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface IntegralGradeMapper extends BaseMapper<IntegralGrade> {
    @Update("UPDATE integral_grade SET is_deleted = 0 WHERE is_deleted = 1")
    void regain();
}
