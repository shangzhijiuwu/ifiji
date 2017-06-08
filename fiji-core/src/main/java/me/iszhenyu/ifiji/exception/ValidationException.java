package me.iszhenyu.ifiji.exception;

import me.iszhenyu.ifiji.constant.FijiError;
import me.iszhenyu.ifiji.constant.LogLevel;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public class ValidationException extends FijiException {

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
    public int getCode() {
        return FijiError.ILLEGAL.getCode();
    }

    @Override
    public String getType() {
        return FijiError.ILLEGAL.getType();
    }

    @Override
    public LogLevel getLogLevel() {
        return LogLevel.WARN;
    }
}
