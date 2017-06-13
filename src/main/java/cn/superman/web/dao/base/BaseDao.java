package cn.superman.web.dao.base;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T, E> {
	void add(T t);

	void update(T t);

	void deleteById(Serializable id);

	void deleteWithCondition(E condition);

	List<T> find();

	T findById(Serializable id);

	List<T> findWithCondition(E condition);

	long queryTotalCount();

	long queryTotalCountWithCondition(E condition);
}
