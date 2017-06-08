package me.iszhenyu.ifiji.web.controller;

import org.quartz.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;


/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected void validateForm(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new ValidationException(errors.get(0).getDefaultMessage());
        }
    }
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<RestError> exception(RuntimeException e) {
        logger.error("", e);
        RestError restError = new RestError();
        restError.setStatus(HttpStatus.BAD_REQUEST.value());
        restError.setMessage(e.getMessage());
        ResponseEntity<RestError> response = new ResponseEntity<RestError>(restError, HttpStatus.BAD_REQUEST);
        return response;
    }
}
