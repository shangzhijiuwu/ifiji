package me.iszhenyu.ifiji.dao;

import me.iszhenyu.ifiji.dao.mapper.BaseMapper;
import me.iszhenyu.ifiji.model.BaseModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiaoz on 2017/5/11.
 */
public abstract class BaseDao<T extends BaseModel> {

	protected abstract BaseMapper<T> getMapper();

	public T get(Serializable id) {
		return getMapper().get(id);
	}

	public List<T> find() {
		return getMapper().find();
	}

	public List<T> findByIds(Collection<Serializable> ids) {
		return getMapper().findByIds(ids);
	}

	public void insert(T model) {
		getMapper().insert(model);
	}

	public void batchInsert(Collection<T> models) {
		getMapper().batchInsert(models);
	}

	//更新乐观锁
	public void update(T model) {
		int updatedCount = getMapper().update(model);
		if (updatedCount == 0) {
			throw new RuntimeException("更新失败");
		}
	}

	public void batchUpdate(Collection<T> models) {
		for (T model : models) {
			update(model);
		}
	}

	public void delete(Serializable id) {
		getMapper().delete(id);
	}

	public void delete(T model) {
		getMapper().delete(model);
	}

	public void batchDelete(Collection<T> models) {
		getMapper().batchDelete(models);
	}

}
