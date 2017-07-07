package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.FijiException;
import me.iszhenyu.ifiji.web.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author zhen.yu
 * @since 2017/6/9
 */
@ControllerAdvice
public class FijiExceptionHandler {

    @ExceptionHandler({FijiException.class})
    public ResponseEntity<ResponseVO> handleFijiException(FijiException e) {
        ResponseVO vo = ResponseVO.fail(e.getShowMessage());
        return new ResponseEntity<>(vo, e.getStatus());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseVO handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseVO.fail("could_not_read_json");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseVO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseVO.fail("request_method_not_supported");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseVO handleHttpMediaTypeNotSupportedException(Exception e) {
        return ResponseVO.fail("content_type_not_supported");
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class})
    public ResponseVO handleRuntimeException(RuntimeException e) {
        return ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
