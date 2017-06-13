package cn.superman.web.dao;

import java.util.List;

import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dao.base.MyBatisRepository;
import cn.superman.web.po.ProblemType;

@MyBatisRepository
public interface ProblemTypeDao extends BaseDao<ProblemType, ProblemType> {
    List<ProblemType> findWithSomeIds(List<Integer> ids);
}
