package me.iszhenyu.ifiji.core.exception;

import me.iszhenyu.ifiji.constant.LogLevel;
import me.iszhenyu.ifiji.util.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public abstract class FijiException extends RuntimeException {

    private static final long serialVersionUID = -735473505163301070L;

    private HttpStatus status;
    private LogLevel logLevel;

    private String methodAndParams;
    private String showMessage;

    public FijiException() {
    }

    public FijiException(String message) {
        super(message);
    }

    public FijiException(String message, Throwable cause) {
        super(message, cause);
    }

    public FijiException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getStatus();

    public abstract LogLevel getLogLevel();

    /**
     * 给客户端看的message
     */
    public String getShowMessage() {
        if (StringUtils.isEmpty(showMessage)) {
            return getMessage();
        }
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public String getMethodAndParams() {
        return methodAndParams;
    }

    public void setMethodAndParams(String methodAndParams) {
        this.methodAndParams = methodAndParams;
    }

}
