package me.iszhenyu.ifiji.exception;

import me.iszhenyu.ifiji.constant.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public class ValidationException extends FijiException {

    private static final long serialVersionUID = -8360053753340671580L;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public LogLevel getLogLevel() {
        return LogLevel.WARN;
    }
}
