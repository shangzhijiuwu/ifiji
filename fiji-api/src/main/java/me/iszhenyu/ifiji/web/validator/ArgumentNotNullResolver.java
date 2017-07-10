package me.iszhenyu.ifiji.web.validator;

import me.iszhenyu.ifiji.exception.IllegalArgumentException;
import me.iszhenyu.ifiji.web.validator.annotation.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author zhen.yu
 * @since 2017/7/10
 */
public class ArgumentNotNullResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(NotNull.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameter = methodParameter.getParameterName();
        String value = webRequest.getParameter(parameter);
        if (value == null) {
            NotNull validation = methodParameter.getParameterAnnotation(NotNull.class);
            throw new IllegalArgumentException(validation.message());
        }
        return value;
    }
}
