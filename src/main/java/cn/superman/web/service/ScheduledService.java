package cn.superman.web.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.superman.constant.ConstantParameter;
import cn.superman.util.Log4JUtil;
import cn.superman.web.component.AutowireInjector;
import cn.superman.web.job.CloseCompetitionJob;
import cn.superman.web.service.admin.AdminUserService;
import cn.superman.web.service.front.UserService;
import cn.superman.web.util.SerializingUtil;

@Service
public class ScheduledService {

	@Autowired
	private JobService jobService;
	@Autowired
	private AutowireInjector autowireInjector;
	@Autowired
	private UserService userService;
	@Autowired
	private AdminUserService adminUserService;

	// 每天从凌晨4点开始执行，一直执行到凌晨的6点
	@Scheduled(cron = "0 0 4-6 * * ?")
	public void doCloseCompetitionJob() {
		// 获取所有的任务
		File jobDir = new File(
				ConstantParameter.CLOSE_COMPETITION_JOB_ROOT_PATH);
		if (!jobDir.exists()) {
			Log4JUtil.logError(new RuntimeException("保存关闭比赛的深夜任务的文件夹不见了"));
			return;
		}

		File[] jobFiles = jobDir.listFiles();
		if (jobFiles.length < 1) {
			return;
		}

		for (File jobFile : jobFiles) {
			try {
				Runnable job = SerializingUtil.readBeanFromFile(jobFile,
						CloseCompetitionJob.class);
				// 先为其装配必要的组件
				autowireInjector.autowire(job);
				jobService.executeNow(job);
			} catch (Exception e) {
				e.printStackTrace();
				Log4JUtil.logError(e);
			}
			jobFile.delete();
		}
	}

	// 每天从凌晨1点开始执行，一直执行到凌晨的4点
	@Scheduled(cron = "0 0 1-4 * * ?")
	public void doJudgeCompetitionJob() {
		// 获取所有的任务
		File jobDir = new File(
				ConstantParameter.JUDGE_COMPETITION_JOB_ROOT_PATH);
		if (!jobDir.exists()) {
			Log4JUtil.logError(new RuntimeException("保存比赛判题的深夜任务的文件夹不见了"));
			return;
		}

		File[] jobFiles = jobDir.listFiles();
		if (jobFiles.length < 1) {
			return;
		}

		for (File jobFile : jobFiles) {
			try {
				Runnable job = SerializingUtil.readBeanFromFile(jobFile,
						CloseCompetitionJob.class);
				// 先为其装配必要的组件
				autowireInjector.autowire(job);
				jobService.executeNow(job);
			} catch (Exception e) {
				e.printStackTrace();
				Log4JUtil.logError(e);
			}
			jobFile.delete();
		}
	}

	// 每天凌晨6点执行
	@Scheduled(cron = "0 0 6 * * ? ")
	public void updateUserLeaderboardCache() {
		userService.updateUserLeaderboardCache();
	}

	// 每天凌晨一点执行
	@Scheduled(cron = "0 0 1 * * ? ")
	public void countUserData() {
		adminUserService.countUserData();
	}

	/**
	 * 每兩个小时定时清理一下，用于放置class文件的文件夹内过期的class文件
	 */
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void clearClassFiles() {
		File dir = new File(ConstantParameter.CLASS_FILE_ROOT_PATH);
		if (!dir.exists()) {
			try {
				dir.createNewFile();
			} catch (IOException e) {
				Log4JUtil.logError(e);
			}
			return;
		}

		File[] listFiles = dir.listFiles();

		for (File file : listFiles) {
			try {
				file.delete();
			} catch (Exception e) {
				Log4JUtil.logError(e);
			}
		}
	}
}
