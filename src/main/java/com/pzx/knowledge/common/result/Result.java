package com.pzx.knowledge.common.result;

import lombok.Data;

@Data
public class Result <T>{
    private int code;
    private String message;
    private T data;

    private Result(){}
    // 成功（无数据）
    public static <T>  Result<T> ok(){
        Result<T> r =new Result<>();
        r.code=ResultCode.SUCCESS.getCode();
        r.message=ResultCode.SUCCESS.getMessage();
        return r;
    }


    // 成功（带数据）
    public static<T> Result<T> ok(T data){
        Result<T> r =new Result<>();
        r.code=ResultCode.SUCCESS.getCode();
        r.message=ResultCode.SUCCESS.getMessage();
        r.data=data;
        return r;
    }


    // 失败（自定义 message）
    public static<T> Result<T> fail(ResultCode code){
         Result<T> r= new Result<>();
         r.code =code.getCode();
         r.message = code.getMessage();
        return r;
    }

    // 失败（自定义 message）
    public static <T> Result<T> fail(ResultCode code, String message) {
        Result<T> r = new Result<>();
        r.code = code.getCode();
        r.message = message;
        return r;
    }



}
