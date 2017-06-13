package cn.superman.web.service.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.superman.constant.ConstantParameter;
import cn.superman.util.BeanMapperUtil;
import cn.superman.util.DateUtil;
import cn.superman.util.JsonUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.web.bean.ExcelCreator;
import cn.superman.web.component.AutowireInjector;
import cn.superman.web.dao.CompetitionApplicationDao;
import cn.superman.web.dao.CompetitionDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.AddCompetitionDTO;
import cn.superman.web.dto.UpdateCompetitionDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.job.CloseCompetitionJob;
import cn.superman.web.job.JudgeCompetitionJob;
import cn.superman.web.po.Competition;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.po.CompetitionApplication;
import cn.superman.web.service.JobService;
import cn.superman.web.service.page.PageService;

@Service
public class AdminCompetitionService extends
		PageService<Competition, Competition> {
	@Autowired
	private CompetitionDao competitionDao;
	@Autowired
	private JobService jobService;
	@Autowired
	private AdminCompetitionAccountService adminCompetitionAccountService;
	@Autowired
	private CompetitionApplicationDao competitionApplicationDao;
	@Autowired
	private AutowireInjector autowireInjector;

	private ExecutorService reportExecutorService = Executors
			.newSingleThreadExecutor();

	@Transactional
	public void add(AddCompetitionDTO dto) {
		Competition competition = BeanMapperUtil.map(dto, Competition.class);

		if (dto.getIsCanDeclare()) {
			if (dto.getCompetitionApplyBeginTime() == null
					|| dto.getCompetitionApplyEndTime() == null) {
				throw new ServiceLogicException("允许申请报名的比赛，申请开始和结束时间都不能为空");
			}
		}

		// 判断格式是否正确
		if (dto.getCompetitionProblemIds() != null
				&& dto.getCompetitionProblemIds().matches("([0-9]*,)*")) {
			throw new ServiceLogicException("题目格式不对");
		}

		// 创建比赛内容文件夹
		File competitionContentDir = new File(
				ConstantParameter.COMPETITION_ROOT_PATH + File.separator
						+ competition.getCompetitionName() + "_"
						+ DateUtil.getYYYYMMddToday() + "_"
						+ RandomStringUtils.randomAlphanumeric(6));
		if (competitionContentDir.exists()) {
			throw new ServiceLogicException(
					"存在相同名字的比赛文件夹，你修改比赛名字，或者稍后再尝试创建（因为文件夹名称带有6位随机数）");
		}

		if (!competitionContentDir.mkdirs()) {
			throw new ServiceLogicException("比赛创建失败，无法创建比赛内容文件夹，请联系维护人员");
		}
		competition.setCompetitionContentRootPath(competitionContentDir
				.getAbsolutePath());

		competition.setIsClose(false);
		competition.setIsJudge(false);
		competitionDao.add(competition);
		resizeCompetitionAccount(competition,
				competition.getCompetitionPlayersCount());
	}

	/**
	 * 这里本没有删除硬盘上该比赛的内容
	 * 
	 * @param id
	 */
	public void deleteById(Integer id) {
		Competition competition = competitionDao.findById(id);
		if (competition.getIsCanDeclare() != null
				&& competition.getIsCanDeclare()) {
			// 是公开比赛，删除申请请求
			CompetitionApplication competitionApplicationCondition = new CompetitionApplication();
			competitionApplicationCondition.setCompetitionId(id);
			competitionApplicationDao
					.deleteWithCondition(competitionApplicationCondition);
		}

		competitionDao.deleteById(id);
		// 释放这个比赛占据的账号
		adminCompetitionAccountService.releaseAllCompetitionAccount(id);
	}

	public Competition findById(Integer id) {
		return competitionDao.findById(id);
	}

	@Transactional
	public void update(UpdateCompetitionDTO dto) {

		Competition competition = BeanMapperUtil.map(dto, Competition.class);

		// 判断格式是否正确
		if (dto.getCompetitionProblemIds() != null
				&& dto.getCompetitionProblemIds().matches("([0-9]*,)*")) {
			throw new ServiceLogicException("题目格式不对");
		}

		competitionDao.update(competition);

		if (competition.getCompetitionPlayersCount() != null) {
			// 获得当前属于该比赛的账号数量
			int accountCount = adminCompetitionAccountService
					.findCompetitionAccountCount(competition.getCompetitionId());
			// 判断是多了还是少了
			if (accountCount == competition.getCompetitionPlayersCount()) {
				return;
			} else if (accountCount < competition.getCompetitionPlayersCount()) {
				resizeCompetitionAccount(competition,
						competition.getCompetitionPlayersCount() - accountCount);
			} else {
				int useCount = adminCompetitionAccountService
						.findCompetitionUseAccountCount(competition
								.getCompetitionId());
				if (useCount > accountCount) {
					// 如果已经使用的数量，比新调整的数量还要多，证明有一些账号是无法释放的，因为已经被占用了
					throw new ServiceLogicException(
							"新的参赛人数过低，已经分配出去的账号数量，比新的参赛人数还要多");
				}
				// 减少
				adminCompetitionAccountService.releaseSomeCompetitionAccount(
						competition.getCompetitionId(), accountCount
								- competition.getCompetitionPlayersCount());
			}
		}
	}

	public void publish(Integer id) {
		Competition competition = new Competition();
		competition.setCompetitionId(id);
		competition.setIsPublish(true);
		competitionDao.update(competition);
	}

	public void closeCompetition(Integer id, boolean isRunNow) {
		if (!isRunNow) {
			String jobPath = ConstantParameter.CLOSE_COMPETITION_JOB_ROOT_PATH
					+ RandomStringUtils.randomAlphabetic(12);
			CloseCompetitionJob closeRunnable = new CloseCompetitionJob(id);
			jobService.executeDelay(closeRunnable, CloseCompetitionJob.class,
					jobPath);
			return;
		}

		// 所有的job一开始都不在spring的管理中，所以这里要为其注入组件
		CloseCompetitionJob closeCompetitionJob = new CloseCompetitionJob(id);
		autowireInjector.autowire(closeCompetitionJob);
		jobService.executeNow(closeCompetitionJob);
	}

	public void createReport(final Integer id) {
		reportExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				createCompetitionReport(adminCompetitionAccountService
						.findCompetitionAccount(id), competitionDao
						.findById(id));
			}
		});

	}

	private void createCompetitionReport(
			List<CompetitionAccount> competitionAccounts,
			Competition competition) {
		ExcelCreator.Builder builder = new ExcelCreator.Builder();
		List<List<String>> datas = new ArrayList<List<String>>();
		List<String> oneData = null;
		List<String> problemIdList = new ArrayList<String>();

		for (CompetitionAccount account : competitionAccounts) {
			oneData = new ArrayList<String>();
			oneData.add(account.getLoginAccount());
			oneData.addAll(handleScore(account.getAccountScore(), problemIdList));
			oneData.add(account.getAccountRemark());
			datas.add(oneData);
		}

		// 按照总成绩从高到低排序
		datas.sort(new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				// 当前第二个的位置是总成绩
				int score1 = Integer.parseInt(o1.get(1));
				int score2 = Integer.parseInt(o2.get(1));
				return score2 - score1;
			}
		});

		try {
			String[] headers = new String[3 + problemIdList.size()];
			headers[0] = "账号";
			headers[1] = "总分";
			short[] colWidths = new short[3 + problemIdList.size()];
			colWidths[0] = 50;
			colWidths[1] = 20;

			for (int i = 0; i < problemIdList.size(); i++) {
				headers[i + 2] = "题目编号：" + problemIdList.get(i) + "的情况";
				colWidths[i + 2] = 150;
			}
			headers[headers.length - 1] = "备注信息";
			colWidths[headers.length - 1] = 150;

			ExcelCreator creator = builder
					.fileName(competition.getCompetitionName() + "比赛报告")//
					.fileSavePath(competition.getCompetitionContentRootPath())//
					.isHorizontalCENTER(true)//
					.isVerticalCENTER(true)//
					.isWrapText(true)//
					.headers(headers)//
					.datas(datas)//
					.colWidths(colWidths)//
					.bulid();
			creator.create();
		} catch (Exception e) {
			Log4JUtil.logError(e);
		}
	}

	/**
	 * 返回一个包含总分，以及每一道题目的得分情况的集合（题目按照编号顺序排序）
	 * 
	 * @param accountScore
	 * @param problemIdList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> handleScore(String accountScore,
			List<String> problemIdList) {
		List<List<Map<String, Object>>> allDatas = JsonUtil.toBean(
				accountScore, List.class);

		if (allDatas == null) {
			return Arrays.asList(0 + "");
		}

		int totalScore = 0;
		String problemId = null;
		int index = 0;
		// 选取数量多的作为长度基数（某些情况下problemIdList会比allDatas长度要长，比如用户某些题目没有提交时）
		int base = problemIdList.size() > allDatas.size() ? problemIdList
				.size() : allDatas.size();
		String[] result = new String[base + 1];
		// 取出每一道题目的情况
		for (List<Map<String, Object>> datas : allDatas) {
			// 每一个测试结果
			for (Map<String, Object> map : datas) {
				problemId = (String) map.get("problemId");
				index = problemIdList.indexOf(problemId);

				if (index == -1) {
					problemIdList.add(problemId);
					index = problemIdList.size() - 1;
				}

				if ((Boolean) map.get("isRight")) {
					totalScore += 10;
				}
			}

			result[index + 1] = datas.toString();
		}

		result[0] = totalScore + "";

		return Arrays.asList(result);
	}

	public File getCompetitionReport(Integer id) {
		Competition competition = competitionDao.findById(id);
		File reportFile = new File(competition.getCompetitionContentRootPath()
				+ File.separator + competition.getCompetitionName()
				+ "比赛报告.xls");

		if (!reportFile.exists()) {
			throw new ServiceLogicException("报告不存在");
		}

		return reportFile;
	}

	public void judgeCompetitionAllCode(Integer competitionId, boolean isRunNow) {
		if (!isRunNow) {
			String jobPath = ConstantParameter.JUDGE_COMPETITION_JOB_ROOT_PATH
					+ RandomStringUtils.randomAlphabetic(12);
			JudgeCompetitionJob judgeRunnable = new JudgeCompetitionJob(
					competitionId);
			jobService.executeDelay(judgeRunnable, JudgeCompetitionJob.class,
					jobPath);
			return;
		}

		// 所有的job一开始都不在spring的管理中，所以这里要为其注入组件
		JudgeCompetitionJob judgeCompetitionJob = new JudgeCompetitionJob(
				competitionId);
		autowireInjector.autowire(judgeCompetitionJob);
		jobService.executeNow(judgeCompetitionJob);
	}

	// 虽然这样效率很低，但是因为只是属于后台的一个功能，而且并发的可能性不高，首要确保的是账号的数量的安全。所以先直接采用这种简单的写法
	private synchronized void resizeCompetitionAccount(Competition competition,
			int competitionIdNeedCount) {
		List<CompetitionAccount> noDispatchAccount = adminCompetitionAccountService
				.findNoDispatchAccount();
		int noDispatchAccountSize = noDispatchAccount.size();

		// 如果空闲的账号不足时，就要创建新的账号了
		if (noDispatchAccountSize > competitionIdNeedCount) {
			// 空闲的账号充足，直接更新这些未分配出去的账号信息即可
			int notNeedCount = noDispatchAccountSize - competitionIdNeedCount;

			for (int n = 0; n < notNeedCount; n++) {
				// 移除多余数目的账号
				noDispatchAccount.remove(n);
			}
			adminCompetitionAccountService.updateOldAccount(competition,
					noDispatchAccount);
		} else {
			// 先利用所以未分配出去的账号，再创建新的账号
			// 获取不足的数量
			int gap = competitionIdNeedCount - noDispatchAccountSize;
			if (noDispatchAccountSize > 0) {
				adminCompetitionAccountService.updateOldAccount(competition,
						noDispatchAccount);
			}
			adminCompetitionAccountService.makeNewAccount(gap,
					competition.getCompetitionId(),
					competition.getCompetitionContentRootPath());
		}
	}

	@Override
	public BaseDao<Competition, Competition> getUseDao() {
		return competitionDao;
	}

}
