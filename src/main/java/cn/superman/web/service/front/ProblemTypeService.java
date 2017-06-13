package cn.superman.web.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.web.dao.ProblemTypeDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.po.ProblemType;
import cn.superman.web.service.page.PageService;

@Service
public class ProblemTypeService extends PageService<ProblemType, ProblemType> {
	@Autowired
	private ProblemTypeDao problemTypeDao;

	public ProblemType getProblemTypeById(Integer problemTypeId) {
		return problemTypeDao.findById(problemTypeId);
	}

	public List<ProblemType> findAll() {
		return problemTypeDao.find();
	}

	@Override
	public BaseDao<ProblemType, ProblemType> getUseDao() {
		return problemTypeDao;
	}
}
