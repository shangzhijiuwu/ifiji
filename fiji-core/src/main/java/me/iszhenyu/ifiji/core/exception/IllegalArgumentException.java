package me.iszhenyu.ifiji.core.exception;

import me.iszhenyu.ifiji.constant.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * @author zhen.yu
 * @since 2017/7/10
 */
public class IllegalArgumentException extends FijiException {
    private static final long serialVersionUID = -8037864982038440596L;

    public IllegalArgumentException() {
    }

    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public LogLevel getLogLevel() {
        return LogLevel.INFO;
    }
}
