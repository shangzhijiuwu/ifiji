package me.iszhenyu.ifiji.web.validator;

import me.iszhenyu.ifiji.util.StringUtils;
import me.iszhenyu.ifiji.web.validator.annotation.NotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zhen.yu
 * @since 2017/7/7
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {
    @Override
    public void initialize(NotEmpty notEmpty) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isNotEmpty(s);
    }
}
