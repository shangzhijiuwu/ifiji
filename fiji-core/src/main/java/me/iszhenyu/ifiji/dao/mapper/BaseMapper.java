package me.iszhenyu.ifiji.dao.mapper;

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

	void save(T model);

	void batchSave(Collection<T> models);

	int update(T model);

	void remove(Serializable id);

	void remove(T model);

	void batchRemove(Collection<T> models);

}
