package me.iszhenyu.ifiji.constant;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public enum FijiError {
    AUTH(401, "Unauthorized"),
    BUSINESS(400, "Business Error"),
    ILLEGAL(403, "Access Forbidden"),
    EMPTY(404, "Not Found"),
    INTERNAL(500, "Internal Server Error");

    private int code;
    private String type;

    FijiError(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
