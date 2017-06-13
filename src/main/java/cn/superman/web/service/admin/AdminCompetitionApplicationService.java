package cn.superman.web.service.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.superman.constant.ConstantParameter;
import cn.superman.util.BeanMapperUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.dao.CompetitionApplicationDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.CompetitionApplicationEmailDTO;
import cn.superman.web.dto.UpdateApplicationDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.Competition;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.po.CompetitionApplication;
import cn.superman.web.service.EmailService;
import cn.superman.web.service.EmailService.EmailRunnable;
import cn.superman.web.service.page.PageService;
import cn.superman.web.util.OfficeUtils;

@Service
public class AdminCompetitionApplicationService extends
		PageService<CompetitionApplication, CompetitionApplication> {

	@Autowired
	private CompetitionApplicationDao competitionApplicationDao;
	@Autowired
	private AdminCompetitionService adminCompetitionService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private AdminCompetitionAccountService adminCompetitionAccountService;

	public CompetitionApplication findById(Integer id) {
		return competitionApplicationDao.findById(id);
	}

	public List<Integer> getAllCompetitionIds() {
		return competitionApplicationDao.findAllCompetionId();
	}

	public Integer dispatchAccount(Integer competitionId, String applicationId) {

		// TODO 对于同一个比赛的请求，要上锁避免并发分配的问题,当然这里还存在一些问题，后续再优化
		synchronized (competitionId) {
			CompetitionAccount account = adminCompetitionAccountService
					.findOneCompetitionNotUseAccount(competitionId);
			if (account == null) {
				throw new ServiceLogicException("分配失败，没有账号可分配");
			}

			CompetitionApplication application = new CompetitionApplication();
			application.setCompetitionApplicationId(applicationId);
			application.setCompetitionAccountId(account
					.getCompetitionAccountId());
			competitionApplicationDao.update(application);

			adminCompetitionAccountService.useAccount(account
					.getCompetitionAccountId());
			return account.getCompetitionAccountId();
		}
	}

	public void cancelDispatchAccount(String applicationId, Integer accountId) {
		adminCompetitionAccountService.cancelDispatch(accountId);
		CompetitionApplication application = new CompetitionApplication();
		application.setCompetitionApplicationId(applicationId);
		application.setCompetitionAccountId(-1);
		competitionApplicationDao.update(application);
	}

	public List<CompetitionApplication> getAllApplicationByCompetitionId(
			Integer competitionId) {
		CompetitionApplication condition = new CompetitionApplication();
		condition.setCompetitionId(competitionId);
		return competitionApplicationDao.findWithCondition(condition);
	}

	public void update(UpdateApplicationDTO dto) {
		CompetitionApplication application = BeanMapperUtil.map(dto,
				CompetitionApplication.class);
		competitionApplicationDao.update(application);
	}

	public void sendEmail(CompetitionApplicationEmailDTO dto) {
		String emailSubject = ConstantParameter.SYSTEM_NAME + "比赛报名结果";
		String emailContent = dto.getNoticeContent();
		EmailRunnable runnable = new EmailRunnable(emailSubject, emailContent,
				dto.getEmail());
		// 发送邮件，通知用户
		emailService.sendEmail(runnable);

		CompetitionApplication application = new CompetitionApplication();
		application.setCompetitionApplicationId(dto
				.getCompetitionApplicationId());
		application.setIsHaveSendEmail(true);
		competitionApplicationDao.update(application);
	}

	public void uploadApplicationReport(MultipartFile file,
			Integer competitionId) {
		Competition competition = adminCompetitionService
				.findById(competitionId);
		try {
			String excelFilePath = competition.getCompetitionContentRootPath()
					+ competition.getCompetitionName()
					+ WebConstant.APPLICATION_REPORT_EXCEL_SAVE_NAME;
			file.transferTo(new File(excelFilePath));
			OfficeUtils.excel2Html(
					excelFilePath,
					competition.getCompetitionContentRootPath()
							+ competition.getCompetitionName()
							+ WebConstant.APPLICATION_REPORT_HTML_SAVE_NAME);
		} catch (Exception e) {
			Log4JUtil.logError(e);
		}
	}

	public File downApplicationReport(Integer competitionId) {
		Competition competition = adminCompetitionService
				.findById(competitionId);
		String excelFilePath = competition.getCompetitionContentRootPath()
				+ competition.getCompetitionName()
				+ WebConstant.APPLICATION_REPORT_EXCEL_SAVE_NAME;
		File excelFile = new File(excelFilePath);
		if (!excelFile.exists()) {
			return null;
		}

		return excelFile;
	}

	public String watchApplicationReport(Integer competitionId) {
		Competition competition = adminCompetitionService
				.findById(competitionId);
		String htmlFilePath = competition.getCompetitionContentRootPath()
				+ competition.getCompetitionName()
				+ WebConstant.APPLICATION_REPORT_HTML_SAVE_NAME;
		File htmlFile = new File(htmlFilePath);
		if (!htmlFile.exists()) {
			return null;
		}

		try {
			return FileUtils.readFileToString(htmlFile);
		} catch (IOException e) {
			Log4JUtil.logError(e);
			return null;
		}
	}

	@Override
	public BaseDao<CompetitionApplication, CompetitionApplication> getUseDao() {
		return competitionApplicationDao;
	}
}
