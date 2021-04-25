package com.nsu.yygh.common.exception;

import com.nsu.yygh.common.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义异常类
 */

@Data
@ApiModel("自定义异常类")
public class MyException extends RuntimeException {
    @ApiModelProperty("异常状态码")
    private Integer code;

    public MyException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public MyException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
