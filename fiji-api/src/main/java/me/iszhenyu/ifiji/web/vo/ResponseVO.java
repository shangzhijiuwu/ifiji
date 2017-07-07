package me.iszhenyu.ifiji.web.vo;

/**
 * @author zhen.yu
 * @since 2017/7/7
 */
public class ResponseVO {
    private static final String OK = "ok";
    private static final String ERROR = "error";

    private Meta meta;
    private Object data;

    public static ResponseVO success() {
        return new ResponseVO().fillSuccess();
    }

    public static ResponseVO success(Object data) {
        return new ResponseVO().fillSuccess(data);
    }

    public static ResponseVO fail() {
        return new ResponseVO().fillFailure();
    }

    public static ResponseVO fail(String message) {
        return new ResponseVO().fillFailure(message);
    }

    public ResponseVO fillSuccess() {
        this.meta = new Meta(true, OK);
        return this;
    }

    public ResponseVO fillSuccess(Object data) {
        this.meta = new Meta(true, OK);
        this.data = data;
        return this;
    }

    public ResponseVO fillFailure() {
        this.meta = new Meta(false, ERROR);
        return this;
    }

    public ResponseVO fillFailure(String message) {
        this.meta = new Meta(false, message);
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getData() {
        return data;
    }

    public class Meta {

        private boolean success;
        private String message;

        public Meta(boolean success) {
            this.success = success;
        }

        public Meta(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
