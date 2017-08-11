package me.iszhenyu.ifiji.web.validator;

import me.iszhenyu.ifiji.core.exception.IllegalArgumentException;
import me.iszhenyu.ifiji.web.validator.annotation.Range;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author zhen.yu
 * @since 2017/7/10
 */
public class ArgumentInRangeResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Range.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameter = methodParameter.getParameterName();
        String parameterType = methodParameter.getParameterType().getName();
        if (!parameterType.equals("java.lang.Integer")) {
            throw new IllegalArgumentException("argument must be Integer");
        }

        String value = webRequest.getParameter(parameter);
        Range validation = methodParameter.getParameterAnnotation(Range.class);
        Integer realValue = Integer.valueOf(value);
        if(validation.min() != -1 && realValue < validation.min()){
            throw new IllegalArgumentException(validation.message());
        }
        if(validation.max() != -1 && realValue > validation.max()){
            throw new IllegalArgumentException(validation.message());
        }
        return value;
    }
}
