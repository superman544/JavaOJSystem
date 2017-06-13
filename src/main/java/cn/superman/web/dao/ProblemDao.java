package cn.superman.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dao.base.MyBatisRepository;
import cn.superman.web.po.Problem;

@MyBatisRepository
public interface ProblemDao extends BaseDao<Problem, Problem> {
    public void userSloveProblem(@Param("userIdData") String userIdData, @Param("problemId") Integer problemId);

    public void increaseSubmitProblemCount(Integer problemId);

    public List<Problem> findPulishProblemByLikeName(String problemName);
}
