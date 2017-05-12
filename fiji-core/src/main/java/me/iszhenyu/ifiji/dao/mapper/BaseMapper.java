package me.iszhenyu.ifiji.dao.mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiaoz on 2017/5/11.
 */
public interface BaseMapper<T> {

	T get(Serializable id);

	List<T> find();

	List<T> findByIds(Collection<Serializable> ids);

	void insert(T model);

	void batchInsert(Collection<T> models);

	int update(T model);

	void delete(Serializable id);

	void delete(T model);

	void batchDelete(Collection<T> models);

}
