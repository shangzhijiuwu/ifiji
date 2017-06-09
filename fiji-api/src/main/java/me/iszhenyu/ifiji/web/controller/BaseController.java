package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

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

}
