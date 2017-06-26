package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.FijiException;
import me.iszhenyu.ifiji.web.vo.ErrorVO;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorVO> handleRuntimeException(RuntimeException e) {
        ErrorVO restError = new ErrorVO();
        restError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        restError.setErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(restError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
