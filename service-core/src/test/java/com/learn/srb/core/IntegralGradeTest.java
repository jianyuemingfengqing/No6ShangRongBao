package com.learn.srb.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.srb.core.pojo.entity.IntegralGrade;
import com.learn.srb.core.service.IntegralGradeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@SpringBootTest
public class IntegralGradeTest {

    @Resource
    IntegralGradeService integralGradeService;

    @Test
    public void testIn() {
//        integralGradeService.list();
        Page<IntegralGrade> page = integralGradeService.page(new Page<IntegralGrade>(2, 2));

        System.out.println(page.getRecords());
    }

    @Test
    public void testAs(){
        String str = "";
        Assert.notNull(str,"不能为空");
    }
}
