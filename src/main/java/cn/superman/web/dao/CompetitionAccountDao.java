package cn.superman.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dao.base.MyBatisRepository;
import cn.superman.web.po.CompetitionAccount;

@MyBatisRepository
public interface CompetitionAccountDao extends BaseDao<CompetitionAccount, CompetitionAccount> {

    public CompetitionAccount findOneWithCondition(CompetitionAccount condition);

    public List<Integer> findAllCompetionId();

    public void createAccountBatch(List<CompetitionAccount> accounts);

    public void releaseAllAccountByCompetitionId(Integer competitionId);

    public void releaseSomeAccount(@Param("competitionId") Integer competitionId, @Param("releaseCount") Integer releaseCount);

    public void setAccountScoreByCodeRootPath(@Param("accountScore") String accountScore, @Param("codeRootPath") String codeRootPath);
}
