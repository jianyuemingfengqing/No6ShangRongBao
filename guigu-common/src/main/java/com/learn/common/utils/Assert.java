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

    public static void strNotNull(String string, ResponseEnum codeNullError) {
        if (string == null || string.trim().length() == 0) {
            throw new BusinessException(codeNullError);
        }
    }

    public static void strNotEq(String code, String redisCode, ResponseEnum codeError) {
        if (code == null || redisCode == null || !code.equals(redisCode)) {
            throw new BusinessException(codeError);
        }
    }

    public static void isTrue(boolean flag, ResponseEnum responseEnum) {
        if (flag) {
            throw new BusinessException(responseEnum);
        }
    }
}
