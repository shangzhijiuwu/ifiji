package me.iszhenyu.ifiji.core.orm.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhen.yu
 * @since 2017/8/11
 */
public class Pagination<T> extends SimplePage implements Serializable {
    private static final long serialVersionUID = 3744434893536458346L;
    private List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Pagination() {
    }

    public Pagination(int totalCount, int pageSize, int pageNo) {
        super(totalCount, pageSize, pageNo);
    }

    public Pagination(int totalCount, int pageSize, int pageNo, List<T> list) {
        super(totalCount, pageSize, pageNo);
        this.items = list;
    }
}
