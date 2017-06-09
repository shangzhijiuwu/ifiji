package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.FijiException;
import me.iszhenyu.ifiji.web.vo.ErrorVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author zhen.yu
 * @since 2017/6/9
 */
@ControllerAdvice
public class FijiExceptionHandler {

    @ExceptionHandler({FijiException.class})
    public ResponseEntity<ErrorVO> handleFijiException(FijiException e) {
        ErrorVO restError = new ErrorVO();
        restError.setStatus(e.getStatus().value());
        restError.setErrorMessage(e.getShowMessage());
        return new ResponseEntity<>(restError, e.getStatus());
    }
}
