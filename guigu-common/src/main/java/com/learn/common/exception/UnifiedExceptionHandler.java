package com.learn.common.exception;

import com.learn.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
@Slf4j
public class UnifiedExceptionHandler {
    @ExceptionHandler(value = Exception.class) //当controller中抛出Exception，则捕获
    public R handleException(Exception e) {
//        log.error(e.getMessage(), e);
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error();
    }

    @ExceptionHandler(value = BusinessException.class)
    public R exception(BusinessException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error().code(e.getCode()).message(e.getMessage());
    }
}
