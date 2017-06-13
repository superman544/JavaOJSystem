package cn.superman.web.service.front;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.Log4JUtil;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.dao.CompetitionAccountDao;
import cn.superman.web.dao.CompetitionDao;
import cn.superman.web.dao.ProblemDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.CompetitionProblemAnswerDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.Competition;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.po.Problem;
import cn.superman.web.service.page.PageService;

@Service
public class CompetitionService extends PageService<Competition, Competition> {

	@Autowired
	private CompetitionDao CompetitionDao;
	@Autowired
	private CompetitionAccountDao competitionAccountDao;
	@Autowired
	private ProblemDao problemDao;

	private static Competition defaultCondition = null;
	static {
		defaultCondition = new Competition();
		defaultCondition.setIsPublish(true);
	}

	@Override
	public BaseDao<Competition, Competition> getUseDao() {
		return CompetitionDao;
	}

	@Override
	public Competition getDefaultCondition() {
		return defaultCondition;
	}

	public Competition getCompetitionById(Integer competitionId) {
		return CompetitionDao.findById(competitionId);
	}

	public CompetitionAccount loginCompetition(String account, String password,
			Integer competitionId) {
		CompetitionAccount condition = new CompetitionAccount();
		condition.setLoginAccount(account);
		condition.setLoginPassword(password);
		condition.setCompetitionId(competitionId);
		List<CompetitionAccount> list = competitionAccountDao
				.findWithCondition(condition);

		if (list == null || list.size() < 1) {
			throw new ServiceLogicException("没有该比赛账号");
		}

		return list.get(0);
	}

	public List<Problem> getCompetitionProblems(String problemIds) {
		String[] ids = problemIds.split(WebConstant.COMPETITION_PROBLEM_ID_GAP);
		List<Problem> problems = new ArrayList<Problem>();
		Problem problem;
		for (String id : ids) {
			problem = problemDao.findById(Integer.parseInt(id));
			if (problem != null) {
				problems.add(problem);
			}
		}
		return problems;
	}

	public void submitCompetitionProblemAnswer(CompetitionProblemAnswerDTO dto) {
		CompetitionAccount competitionAccount = dto.getCompetitionAccount();
		String accountCodeRootPath = competitionAccount
				.getAccountCodeRootPath();
		File codeDir = new File(accountCodeRootPath + File.separator
				+ dto.getProblemId());
		if (!codeDir.exists()) {
			if (!codeDir.mkdirs()) {
				throw new ServiceLogicException("系统无法创建保存代码的文件夹，请联系管理员");
			}
		}

		String javaFileName = "p" + dto.getProblemId() + "_"
				+ System.currentTimeMillis() + "Main";
		// 替换主类名为文件名
		String code = dto.getCode().replace("Main", javaFileName);
		try {
			// 先判断原本文件夹下，是否已经存在代码文件，有的话将旧的代码文件删除，即始终保持文件夹下只有一个文件并且是代码文件
			File dir = new File(codeDir.getAbsolutePath());
			if (dir.list().length > 0) {
				File[] listFiles = dir.listFiles();
				for (int i = 0; i < listFiles.length; i++) {
					FileUtils.deleteQuietly(listFiles[i]);
				}
			}

			// 暂时默认代码都是JAVA
			File codeFile = new File(codeDir.getAbsolutePath() + File.separator
					+ javaFileName + WebConstant.DEFAULT_CODE_FILE_SUFFIX);
			FileUtils.write(codeFile, code);
		} catch (IOException e) {
			Log4JUtil.logError(e);
		}
	}
}
