package me.iszhenyu.ifiji.web.validator;

import me.iszhenyu.ifiji.exception.IllegalArgumentException;
import me.iszhenyu.ifiji.web.validator.annotation.Max;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author zhen.yu
 * @since 2017/7/10
 */
public class ArgumentMaxResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Max.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameter = methodParameter.getParameterName();
        String parameterType = methodParameter.getParameterType().getName();
        if (!parameterType.equals("java.lang.Integer")) {
            throw new IllegalArgumentException("argument must be Integer");
        }

        String value = webRequest.getParameter(parameter);
        Max validation = methodParameter.getParameterAnnotation(Max.class);
        Integer realValue = Integer.valueOf(value);
        if(validation.value() != Integer.MAX_VALUE && realValue > validation.value()){
            throw new IllegalArgumentException(validation.message());
        }
        return value;
    }
}
