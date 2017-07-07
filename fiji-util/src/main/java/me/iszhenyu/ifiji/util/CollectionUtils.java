package me.iszhenyu.ifiji.util;

import java.util.Collection;

/**
 * @author zhen.yu
 * @since 2017/7/7
 */
public class CollectionUtils {
    public static  <E> boolean isEmpty(Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <E> boolean isNotEmpty(Collection<E> collection) {
        return !isEmpty(collection);
    }
}
