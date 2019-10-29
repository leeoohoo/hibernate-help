package com.learn.hibernate.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 全局异常拦截
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/9/27.
 */
@Slf4j
@ControllerAdvice
public class GloableExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Mono gloabExceptionHandler(Exception e){
        log.error("-----------------------------------------<<<<<<<<<<<<<<<<<<<<<异常分割线<<<<<<<<<<<<<<<<<<<<<<<<<------------------------------------");
        log.error("-------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>系统发生异常",e);
        log.error("-----------------------------------------<<<<<<<<<<<<<<<<<<<<<异常分割线<<<<<<<<<<<<<<<<<<<<<<<<<------------------------------------");

        //除数不能为零
        if(e instanceof ArithmeticException){
            return Mono.error(new ArithmeticException("除数不能为零"));
        }
        //参数验证错误
        if(e instanceof WebExchangeBindException){
            var e1 = (WebExchangeBindException)e;
            List<FieldError> fieldErrors = e1.getFieldErrors();
            StringBuilder sb = new StringBuilder();
            for(FieldError f : fieldErrors) {
                sb.append(f.getDefaultMessage()).append(";");
            }
            sb.deleteCharAt(sb.lastIndexOf(";"));
            return Mono.error(new RuntimeException(sb.toString()));
        }

        if(e instanceof ServerWebInputException) {
            return Mono.error(new RuntimeException("请求类型错误"));
        }



        if(e.getMessage().isBlank()) {
            return Mono.error(new RuntimeException("系统繁忙，请稍后在试"));
        }else {
            return Mono.error(e);
        }
    }

}
