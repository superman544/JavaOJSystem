package cn.superman.web.dao;

import java.util.List;

import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dao.base.MyBatisRepository;
import cn.superman.web.po.CompetitionApplication;

@MyBatisRepository
public interface CompetitionApplicationDao extends
		BaseDao<CompetitionApplication, CompetitionApplication> {
	List<Integer> findAllCompetionId();
}
