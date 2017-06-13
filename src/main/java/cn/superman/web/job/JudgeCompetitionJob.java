package cn.superman.web.job;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.superman.system.service.JavaSandboxService;
import cn.superman.system.service.dto.JudgeProblemDTO;
import cn.superman.system.service.dto.ProblemJudgeResult;
import cn.superman.system.service.dto.ProblemJudgeResultItem;
import cn.superman.util.JsonUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.util.UUIDUtil;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.dao.CompetitionAccountDao;
import cn.superman.web.dao.CompetitionDao;
import cn.superman.web.dao.ProblemDao;
import cn.superman.web.po.Competition;
import cn.superman.web.po.Problem;

public class JudgeCompetitionJob implements Runnable, Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 4921306545010830767L;
	// 用于在runid中嵌套题目编号的分隔符
	private static final String PROBLEM_ID_GAP = ":";
	@Autowired
	private transient CompetitionDao competitionDao;
	@Autowired
	private transient CompetitionAccountDao competitionAccountDao;
	@Autowired
	private transient ProblemDao problemDao;
	private transient Map<String, Problem> allCompetitionProblem = null;
	private Integer competitionId;

	public JudgeCompetitionJob() {

	}

	public JudgeCompetitionJob(Integer competitionId) {
		this.competitionId = competitionId;
	}

	@Override
	public void run() {
		judgeCompetitionAllCode(competitionId);
	}

	private void judgeCompetitionAllCode(Integer competitionId) {
		Competition competition = competitionDao.findById(competitionId);

		File competitionDir = new File(
				competition.getCompetitionContentRootPath());
		// 获取里面所有的比赛用户信息文件夹
		File[] competitionFiles = competitionDir.listFiles();

		if (competitionFiles == null || competitionFiles.length < 1) {
			return;
		}

		// 获取所有的题目信息
		String problemIds = competition.getCompetitionProblemIds();
		String[] ids = problemIds.split(WebConstant.COMPETITION_PROBLEM_ID_GAP);
		allCompetitionProblem = new HashMap<String, Problem>();
		for (String id : ids) {
			Problem problem = problemDao.findById(id);
			allCompetitionProblem.put(problem.getProblemId() + "", problem);
		}

		for (File file : competitionFiles) {
			// 非文件夹的，都不是用户信息文件夹
			if (file.isDirectory()) {
				// 该用户所有的题目文件夹
				File[] allCodeDirs = file.listFiles();
				String judgeCodeResult = getJudgeCodeResult(allCodeDirs);
				competitionAccountDao.setAccountScoreByCodeRootPath(
						judgeCodeResult, file.getAbsolutePath());
			}
		}

		Competition condition = new Competition();
		condition.setCompetitionId(competitionId);
		condition.setIsJudge(true);
		competitionDao.update(condition);
	}

	private String getJudgeCodeResult(File[] allCodeDirs) {
		JudgeProblemDTO judgeProblemDTO = null;
		Problem problem = null;
		// 文件夹的数量，代表该用户一共做了多少道题目
		JobJudgeResultListener listener = new JobJudgeResultListener(
				allCodeDirs.length);

		for (File codeDir : allCodeDirs) {
			// 文件夹名字就是题目编号
			String problemId = codeDir.getName();
			problem = allCompetitionProblem.get(problemId);
			// 如果题目已经不存在了，直接跳过
			if (problem == null) {
				// 告诉监听器，缺少了一个题目
				listener.missingOne();
				continue;
			}
			File[] codeFiles = codeDir.listFiles();

			// 理论上，只会有一个文件或者没有文件
			if (codeFiles.length == 1) {
				judgeProblemDTO = new JudgeProblemDTO();

				judgeProblemDTO.setJavaFilePath(codeFiles[0].getAbsolutePath());
				judgeProblemDTO.setMemoryLimit(problem.getMemoryLimit());
				judgeProblemDTO.setTimeLimit(problem.getTimeLimit());
				judgeProblemDTO.setProblemInputPathList(getFileList(problem
						.getInputFileRootPath()));
				judgeProblemDTO.setProblemOutputPathList(getFileList(problem
						.getOutputFileRootPath()));
				judgeProblemDTO.setRunId(problemId + PROBLEM_ID_GAP
						+ UUIDUtil.getUUID());
				judgeProblemDTO.setJudgeResultListener(listener);

				JavaSandboxService.getInstance().judgeProblem(judgeProblemDTO,
						null);
			}
		}

		return listener.getJsonSorce();
	}

	private List<String> getFileList(String rootPath) {
		File inputFileDir = new File(rootPath);
		String[] fileNames = inputFileDir.list();
		List<String> inputPaths = new ArrayList<String>(fileNames.length);

		for (int i = 0; i < fileNames.length; i++) {
			inputPaths.add(rootPath + File.separator + fileNames[i]);
		}
		return inputPaths;
	}

	private class JobJudgeResultListener implements
			JavaSandboxService.JudgeResultListener {
		// 表示一共要等待多少道题目的判题结果出来了，才能组织完这个人一共的做题信息
		private int waitCodeResultCount = 0;
		private int sloveCodeResultCount = 0;
		// 所有题目（包括每一道题目里的每一道小题）的成绩
		private List<List<Sorce>> allSorces = null;

		public JobJudgeResultListener(int waitCodeResultCount) {
			this.waitCodeResultCount = waitCodeResultCount;
			allSorces = Collections
					.synchronizedList(new ArrayList<List<Sorce>>(
							waitCodeResultCount));
		}

		@Override
		public void judgeResult(ProblemJudgeResult problemJudgeResult) {
			// 从item的runId中取出problemId
			String problemId = null;
			String runId = problemJudgeResult.getRunId();
			if (StringUtils.isNotBlank(runId) && runId.contains(PROBLEM_ID_GAP)) {
				problemId = problemJudgeResult.getRunId().split(PROBLEM_ID_GAP)[0];
			}

			List<ProblemJudgeResultItem> problemJudgeResultItems = problemJudgeResult
					.getProblemJudgeResultItems();
			if (problemJudgeResultItems != null
					&& problemJudgeResultItems.size() > 0) {
				Sorce sorce = null;
				List<Sorce> sorces = new ArrayList<Sorce>(
						problemJudgeResultItems.size());

				for (ProblemJudgeResultItem item : problemJudgeResultItems) {
					sorce = new Sorce();
					sorce.problemId = problemId;
					sorce.message = item.getMessage();
					sorce.useMemory = item.getUseMemory();
					sorce.useTime = item.getUseTime();
					sorce.isRight = item.isRight();
					sorces.add(sorce);
				}

				allSorces.add(sorces);
			}

			synchronized (this) {
				notifyAll();
			}
		}

		public void missingOne() {
			notifyAll();
		}

		/**
		 * 如果还没有结果，就会阻塞到有结果为止才返回
		 * 
		 * @return
		 */
		public String getJsonSorce() {
			synchronized (this) {
				while (sloveCodeResultCount < waitCodeResultCount) {
					try {
						wait();
						// 每得到一次判题结果，就进行加一
						sloveCodeResultCount++;
					} catch (InterruptedException e) {
						Log4JUtil.logError(e);
					}
				}
			}
			return JsonUtil.toJson(allSorces);
		}
	}

	@SuppressWarnings("unused")
	private static class Sorce {
		long useTime;
		long useMemory;
		String message;
		String problemId;
		boolean isRight;
	}
}
