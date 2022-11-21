package com.learn.common.utils;

import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;

public class Assert {
    // 自定义断言
//非空
    public static void notNull(Object object, ResponseEnum responseEnum) {
        if (object == null) {
            throw new BusinessException(responseEnum);
        }
    }

    // 非真
    public static void notTrue(boolean flag, ResponseEnum responseEnum) {
        if (!flag) {
            throw new BusinessException(responseEnum);
        }
    }

    //非空
    public static void notNull(Object object, Integer code, String message) {
        if (object == null) {
            throw new BusinessException(code, message);
        }
    }

    // 非真
    public static void notTrue(boolean flag, Integer code, String message) {
        if (!flag) {
            throw new BusinessException(code, message);
        }
    }
}
