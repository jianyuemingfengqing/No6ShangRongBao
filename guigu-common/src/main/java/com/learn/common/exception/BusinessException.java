package com.learn.common.exception;

import com.learn.common.result.ResponseEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private Integer code;
    private String message;

    public BusinessException(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    public BusinessException(Integer code,String message,Exception e){
        super(e);
        this.code = code;
        this.message = message;
    }
    public BusinessException(ResponseEnum codeEnum){
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }
    public BusinessException(ResponseEnum codeEnum,Exception e){
        super(e);
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }
}
