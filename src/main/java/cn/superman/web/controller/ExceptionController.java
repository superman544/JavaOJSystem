package cn.superman.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.superman.util.Log4JUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.exception.UnAuthorizedException;

@ControllerAdvice
public class ExceptionController {
    // 那些在bean里面修饰了NotNull等校验注解的的会进到这里
    @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMap beanParamInvalidHandle(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();

        ResponseMap fieldErrorDataMap = new ResponseMap().buildNotSucessResponse();
        for (FieldError error : fieldErrors) {
            fieldErrorDataMap.put(error.getField() + "Error", error.getDefaultMessage());
        }

        return fieldErrorDataMap;
    }

    @ExceptionHandler(ServiceLogicException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMap serviceLogicExceptionHandle(ServiceLogicException exception) {
        ResponseMap errorDataMap = new ResponseMap().buildNotSucessResponse();
        errorDataMap.append("message", exception.getMessage());
        return errorDataMap;
    }

    // @RequestParam注解的没值时，都会进这里
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMap missingRequestParameterHandle(MissingServletRequestParameterException exception) {
        ResponseMap errorDataMap = new ResponseMap().buildNotSucessResponse();
        errorDataMap.append(exception.getParameterName(), "不能为空");
        return errorDataMap;
    }

    @ExceptionHandler(UnAuthorizedException.class)
    // 401：未经授权
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseMap processUnauthenticatedException(UnAuthorizedException exception) {
        ResponseMap errorDataMap = new ResponseMap().buildNotSucessResponse();
        errorDataMap.append("message", "权限不足，请重新登录或者找管理员授予权限");
        return errorDataMap;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseMap uncatchExceptionHandle(Throwable throwable) {
        ResponseMap errorDataMap = new ResponseMap().buildNotSucessResponse();
        errorDataMap.append("message", "系统遇到错误，操作失败");
        throwable.printStackTrace();
        Log4JUtil.logError(throwable);
        return errorDataMap;
    }
}
