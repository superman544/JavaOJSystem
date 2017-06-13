package cn.superman.web.dao;

import java.util.List;

import cn.superman.web.dao.base.MyBatisRepository;
import cn.superman.web.po.SubmitRecord;

@MyBatisRepository
public interface SubmitRecordDao {
	void add(SubmitRecord submitRecord);

	void update(SubmitRecord submitRecord);

	void deleteWithCondition(SubmitRecord condition);

	List<SubmitRecord> findWithCondition(SubmitRecord condition);

	long querySubmitRecordotalCountWithCondition(SubmitRecord condition);
}
