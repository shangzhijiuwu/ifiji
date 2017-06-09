package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.FijiException;
import me.iszhenyu.ifiji.exception.ValidationException;
import me.iszhenyu.ifiji.web.vo.ErrorVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;


/**
 * @author zhen.yu
 * @since 2017/6/8
 */
class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected void validateForm(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new ValidationException(errors.get(0).getDefaultMessage());
        }
    }

    @ExceptionHandler({FijiException.class})
    public ResponseEntity<ErrorVo> exception(FijiException e) {
        ErrorVo restError = new ErrorVo();
        restError.setStatus(HttpStatus.BAD_REQUEST.value());
        restError.setErrorMessage(e.getShowMessage());
        return new ResponseEntity<>(restError, e.getStatus());
    }
}
