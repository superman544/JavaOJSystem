package cn.superman.web.service.front;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import cn.superman.constant.ConstantParameter;
import cn.superman.system.service.JavaSandboxService;
import cn.superman.system.service.dto.JudgeProblemDTO;
import cn.superman.system.service.dto.ProblemJudgeResult;
import cn.superman.system.service.dto.ProblemJudgeResultItem;
import cn.superman.util.DateUtil;
import cn.superman.util.EncryptUtility;
import cn.superman.util.JsonUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.util.UUIDUtil;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.dao.ProblemDao;
import cn.superman.web.dao.SubmitRecordDao;
import cn.superman.web.dao.UserDao;
import cn.superman.web.dto.ProblemAnswerDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.Problem;
import cn.superman.web.po.SubmitRecord;
import cn.superman.web.po.User;

@Service
public class AnswerSubmitService {
	@Autowired
	private SubmitRecordDao submitRecordDao;
	@Autowired
	private ProblemDao problemDao;
	@Autowired
	private UserDao userDao;
	private JavaSandboxService javaSandboxService;
	private Pattern packagePattern = Pattern.compile("^[ ]*package.*;");
	private Pattern classNamePattern = Pattern
			.compile("public[ ]*class[ ]*Main[ ]*\\{");
	private Pattern mainMethodPattern = Pattern
			.compile("public[ ]*static[ ]*void[ ]*main");

	public AnswerSubmitService() {
		javaSandboxService = JavaSandboxService.getInstance();
	}

	public void submitAnswer(ProblemAnswerDTO dto) {
		checkCodeStandard(dto.getCode());

		User user = dto.getUser();
		// 组拼java文件名，并修改里面的主类名
		// u用户ID:时间毫秒值
		String javaFileName = "u" + dto.getUser().getUserId() + "_"
				+ System.currentTimeMillis() + "Main";
		// 替换主类名为文件名
		String code = dto.getCode().replace("Main", javaFileName);

		// 创建当天的代码提交文件夹
		String today = DateUtil.getYYYYMMddToday();
		File dir = new File(user.getSourceFileRootPath() + File.separator
				+ today);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log4JUtil.logError(new RuntimeException("创建文件夹失败，无法保存用户代码"));
			}
		}

		FileOutputStream outputStream = null;
		String javaFilePath = dir.getAbsolutePath() + File.separator
				+ javaFileName + WebConstant.DEFAULT_CODE_FILE_SUFFIX;
		try {
			outputStream = new FileOutputStream(javaFilePath);
			outputStream.write(code.getBytes());

			SubmitRecord record = new SubmitRecord();
			record.setIsAccepted(false);
			record.setCodeLanguage(dto.getCodeLanguage());
			record.setCodeFilePath(javaFilePath);
			record.setDetails("编译运行中");
			record.setScore(new Double(0));
			record.setSubmitProblemId(dto.getSubmitProblemId());
			record.setSubmitTime(new Date());
			record.setSubmitUserId(user.getUserId());
			record.setSubmitRecordTableName(user.getSubmitRecordTableName());
			submitRecordDao.add(record);

			// 发布判题请求
			sendAnswerToJudge(dto, javaFilePath, record);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("创建文件失败，无法保存用户代码");
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {

			}
		}
	}

	/**
	 * 
	 * @param code
	 * @return true表示代码没有问题，false表示代码有问题
	 */
	private void checkCodeStandard(String code) {
		Matcher matcher = packagePattern.matcher(code);
		if (matcher.find()) {
			throw new ServiceLogicException("不能拥有package语句");
		}

		matcher = classNamePattern.matcher(code);
		if (!matcher.find()) {
			throw new ServiceLogicException("主类名必须是Main");
		}

		matcher = mainMethodPattern.matcher(code);
		if (!matcher.find()) {
			throw new ServiceLogicException("没有静态的main方法入口");
		}
	}

	private void sendAnswerToJudge(ProblemAnswerDTO dto, String javaFilePath,
			final SubmitRecord record) {
		// 先查询出，题目相关要求
		Problem problem = problemDao.findById(dto.getSubmitProblemId());
		JudgeProblemDTO judgeProblemDTO = new JudgeProblemDTO();
		judgeProblemDTO.setJavaFilePath(javaFilePath);
		judgeProblemDTO.setMemoryLimit(problem.getMemoryLimit());
		judgeProblemDTO.setTimeLimit(problem.getTimeLimit());
		judgeProblemDTO.setRunId(UUIDUtil.getUUID());

		List<String> inputPaths = getFileList(problem.getInputFileRootPath());
		List<String> outputPaths = getFileList(problem.getOutputFileRootPath());
		judgeProblemDTO.setProblemInputPathList(inputPaths);
		judgeProblemDTO.setProblemOutputPathList(outputPaths);
		judgeProblemDTO.setJudgeResultListener(new JudgeResultListener(record
				.getSubmitRecordTableName(), record.getSubmitId(), dto
				.getUser().getUserId(), problem));

		javaSandboxService.judgeProblem(judgeProblemDTO,
				new JavaSandboxService.ErrorListener() {
					@Override
					public void onError(Exception exception) {
						record.setDetails(exception.getMessage());
						submitRecordDao.update(record);
					}
				});
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

	private class JudgeResultListener implements
			JavaSandboxService.JudgeResultListener {
		private String submitRecordTableName;
		private BigInteger submitRecordId;
		private Integer userId;
		private Problem problem;

		public JudgeResultListener(String submitRecordTableName,
				BigInteger submitRecordId, Integer userId, Problem problem) {
			super();
			this.submitRecordTableName = submitRecordTableName;
			this.submitRecordId = submitRecordId;
			this.userId = userId;
			this.problem = problem;
		}

		// TODO 以后也给加上一个事务吧？
		@Override
		public void judgeResult(ProblemJudgeResult problemJudgeResult) {
			SubmitRecord record = new SubmitRecord();
			record.setSubmitRecordTableName(submitRecordTableName);
			record.setSubmitId(submitRecordId);

			User user = new User();
			user.setUserId(userId);
			user.setLastSubmitTime(new Date());
			userDao.update(user);

			if (problemJudgeResult.getCorrectRate() >= 1.0) {
				record.setIsAccepted(true);
				// 更新这道题目的答对者总数，提交者总数，以及答对者ID编号集合
				// TODO 感觉现在这样做还是很消耗数据库资源的，以后在想想怎么优化吧
				problemDao.userSloveProblem(userId
						+ WebConstant.PROBLEM_RIGHT_USER_ID_GAP,
						problem.getProblemId());
			} else {
				record.setIsAccepted(false);
				// 提交者总数
				problemDao.increaseSubmitProblemCount(problem.getProblemId());
			}

			record.setScore(new Double(
					problemJudgeResult.getCorrectRate() * 100));

			List<ProblemJudgeResultItem> problemJudgeResultItems = problemJudgeResult
					.getProblemJudgeResultItems();
			ProblemJudgeResultItem tempResultItem = null;
			// 先加密里面的输入输出文件路径
			for (int i = 0; i < problemJudgeResultItems.size(); i++) {
				tempResultItem = problemJudgeResultItems.get(i);
				try {
					tempResultItem
							.setInputFilePath(Base64Utils
									.encodeToUrlSafeString(EncryptUtility
											.AESEncoding(
													ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
													tempResultItem
															.getInputFilePath())
											.getBytes()));

					tempResultItem
							.setOutputFilePath(Base64Utils
									.encodeToUrlSafeString(EncryptUtility
											.AESEncoding(
													ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
													tempResultItem
															.getOutputFilePath())
											.getBytes()));
				} catch (Exception e) {
					Log4JUtil.logError(e);
				}
			}
			String details = JsonUtil.toJson(problemJudgeResultItems);
			record.setDetails(details);

			submitRecordDao.update(record);
		}
	};
}
