package me.iszhenyu.ifiji.orm;

import java.io.Serializable;

/**
 * @author zhen.yu
 * @since 2017/7/28
 */
public interface Pageable extends Serializable {

    int getTotalCount();
    int getTotalPage();
    int getPageSize();
    int getPageNo();
    boolean isFirstPage();
    boolean isLastPage();
    int getNextPage();
    int getPrePage();

}
