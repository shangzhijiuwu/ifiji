package me.iszhenyu.ifiji.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiaoz on 2017/5/11.
 */
public interface BaseMapper<T> {

	T get(Serializable id);

	List<T> listAll();

	List<T> listByIds(Collection<Serializable> ids);

	void insert(T model);

	void batchInsert(Collection<T> models);

	int update(T model);

	void remove(Serializable id);

	void remove(T model);

	void batchRemove(Collection<T> models);

}
