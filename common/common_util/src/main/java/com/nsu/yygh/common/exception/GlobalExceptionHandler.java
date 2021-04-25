package com.nsu.yygh.common.exception;

import com.nsu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 定义一个全局的异常返回类：如果后台出错，不显示500，返回一个统一的错误异常
 */

// 出现异常就跳转到这个Controller
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 页面出现错误Exception，就执行该方法
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 处理自定义异常
     * @param myException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MyException.class)
    public Result error(MyException myException) {
        return Result.fail(myException.getMessage());
    }
}
