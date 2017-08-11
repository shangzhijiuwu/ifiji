package me.iszhenyu.ifiji.dao;

import me.iszhenyu.ifiji.dao.mapper.BaseMapper;
import me.iszhenyu.ifiji.model.BaseDO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiaoz on 2017/5/11.
 */
public abstract class BaseDao<T extends BaseDO> {

	protected abstract BaseMapper<T> getMapper();

	public T get(Serializable id) {
		return getMapper().get(id);
	}

	public List<T> listAll() {
		return getMapper().listAll();
	}

	public List<T> listByIds(Collection<Serializable> ids) {
		return getMapper().listByIds(ids);
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

	public void remove(Serializable id) {
		getMapper().remove(id);
	}

	public void remove(T model) {
		getMapper().remove(model);
	}

	public void batchRemove(Collection<T> models) {
		getMapper().batchRemove(models);
	}

}
