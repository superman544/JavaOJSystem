package cn.superman.web.service.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.BeanMapperUtil;
import cn.superman.util.UUIDUtil;
import cn.superman.web.dao.CompetitionAccountDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.UpdateCompetitionAccountDTO;
import cn.superman.web.po.Competition;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.service.page.PageService;

@Service
public class AdminCompetitionAccountService extends
		PageService<CompetitionAccount, CompetitionAccount> {

	@Autowired
	private CompetitionAccountDao competitionAccountDao;

	public List<Integer> getAllCompetitionIds() {
		return competitionAccountDao.findAllCompetionId();
	}

	public void update(UpdateCompetitionAccountDTO dto) {
		CompetitionAccount account = BeanMapperUtil.map(dto,
				CompetitionAccount.class);
		competitionAccountDao.update(account);
	}

	public void cancelDispatch(Integer accountId) {
		CompetitionAccount account = new CompetitionAccount();
		account.setCompetitionAccountId(accountId);
		account.setIsUse(false);
		competitionAccountDao.update(account);
	}

	public void makeNewAccount(long accountCount, Integer competitionId,
			String competitionContentRootPath) {
		CompetitionAccount tempAccount = null;
		List<CompetitionAccount> newAccounts = new ArrayList<CompetitionAccount>();
		String temp = null;

		for (int i = 0; i < accountCount; i++) {
			tempAccount = new CompetitionAccount();
			temp = UUIDUtil.get32UUID();
			tempAccount.setLoginAccount(temp);
			tempAccount.setLoginPassword(RandomStringUtils
					.randomAlphanumeric(10));
			// 分配给某个比赛了，但是还未使用
			tempAccount.setIsUse(false);
			tempAccount.setAccountCodeRootPath(competitionContentRootPath
					+ File.separator + temp);
			tempAccount.setCompetitionId(competitionId);
			newAccounts.add(tempAccount);
		}

		competitionAccountDao.createAccountBatch(newAccounts);
	}

	public void updateOldAccount(Competition competition,
			List<CompetitionAccount> noUseAccounts) {
		CompetitionAccount tempAccount = null;
		String temp = null;
		for (int i = 0; i < noUseAccounts.size(); i++) {
			tempAccount = noUseAccounts.get(i);
			temp = UUIDUtil.get32UUID();
			tempAccount.setLoginAccount(temp);
			tempAccount.setLoginPassword(RandomStringUtils
					.randomAlphanumeric(10));
			tempAccount.setIsUse(false);
			tempAccount.setAccountCodeRootPath(competition
					.getCompetitionContentRootPath() + File.separator + temp);
			tempAccount.setCompetitionId(competition.getCompetitionId());
			// 更新现有账号信息
			competitionAccountDao.update(tempAccount);
		}
	}

	public void useAccount(Integer accountId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionAccountId(accountId);
		condition.setIsUse(true);
		competitionAccountDao.update(condition);
	}

	public List<CompetitionAccount> findCompetitionUseAccount(
			Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		condition.setIsUse(true);
		return competitionAccountDao.findWithCondition(condition);
	}

	public int findCompetitionUseAccountCount(Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		condition.setIsUse(true);
		return (int) competitionAccountDao
				.queryTotalCountWithCondition(condition);
	}

	public int findCompetitionAccountCount(Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		return (int) competitionAccountDao
				.queryTotalCountWithCondition(condition);
	}

	public List<CompetitionAccount> findCompetitionAccount(Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		return competitionAccountDao.findWithCondition(condition);
	}

	/**
	 * 找出已经分配给某个比赛，但是该比赛还没有使用的账号（即没有分配给某位用户）
	 * 
	 * @return
	 */
	public List<CompetitionAccount> findCompetitionNotUseAccount(
			Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		condition.setIsUse(false);
		return competitionAccountDao.findWithCondition(condition);
	}

	public CompetitionAccount findOneCompetitionNotUseAccount(
			Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		condition.setIsUse(false);
		return competitionAccountDao.findOneWithCondition(condition);
	}

	public int findCompetitionNotUseAccountCount(Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(competitionId);
		condition.setIsUse(false);
		return (int) competitionAccountDao
				.queryTotalCountWithCondition(condition);
	}

	/**
	 * 找出现在没有分配给某个比赛的账号
	 * 
	 * @return
	 */
	public List<CompetitionAccount> findNoDispatchAccount() {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setCompetitionId(-1);
		return competitionAccountDao.findWithCondition(condition);
	}

	public void releaseSomeCompetitionAccount(Integer competitionId,
			int releaseCount) {
		competitionAccountDao.releaseSomeAccount(competitionId, releaseCount);
	}

	public void releaseAllCompetitionAccount(Integer competitionId) {
		competitionAccountDao.releaseAllAccountByCompetitionId(competitionId);
	}

	@Override
	public BaseDao<CompetitionAccount, CompetitionAccount> getUseDao() {
		return competitionAccountDao;
	}
}
