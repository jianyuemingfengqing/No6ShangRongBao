package com.learn.srb.core.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

public class GlobalMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //判断如果bean有需要自动填充属性的set方法  进行自动填充
        if(metaObject.hasSetter("createTime")){
            metaObject.setValue("createTime",new Date());
        }
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime",new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime",new Date());
        }
    }
}
