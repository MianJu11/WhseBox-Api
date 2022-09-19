package com.WhseApi.config;

import com.WhseApi.common.ResultAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.ValidationException;

/**
 * @author lty
 * @desc 全局异常捕获
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 缺少必要参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ResultAll.getResultJson(0, "请输入正确的参数", null);
    }

    /**
     * 请求头异常（缺少token）
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public String handleException(MissingRequestHeaderException e) {
        log.error(e.getMessage(), e);
        return ResultAll.errorToken();
    }

    /**
     * 参数校验未通过异常
     */
    @ExceptionHandler(ValidationException.class)
    public String handleException(ValidationException e) {
        log.error(e.getMessage(), e);
        return ResultAll.errorValidate(e.getMessage());
    }

    /**
     * 参数校验未通过异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ResultAll.errorValidate(e.getMessage());
    }


}
