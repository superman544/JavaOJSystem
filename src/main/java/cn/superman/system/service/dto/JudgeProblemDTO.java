package cn.superman.system.service.dto;

import java.util.List;

import cn.superman.system.service.JavaSandboxService.JudgeResultListener;

public class JudgeProblemDTO {
	private String runId;
	private String javaFilePath;
	private long timeLimit;
	private long memoryLimit;
	private List<String> problemInputPathList;
	private List<String> problemOutputPathList;
	private JudgeResultListener judgeResultListener;

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getJavaFilePath() {
		return javaFilePath;
	}

	public void setJavaFilePath(String javaFilePath) {
		this.javaFilePath = javaFilePath;
	}

	public List<String> getProblemInputPathList() {
		return problemInputPathList;
	}

	public void setProblemInputPathList(List<String> problemInputPathList) {
		this.problemInputPathList = problemInputPathList;
	}

	public List<String> getProblemOutputPathList() {
		return problemOutputPathList;
	}

	public void setProblemOutputPathList(List<String> problemOutputPathList) {
		this.problemOutputPathList = problemOutputPathList;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public long getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(long memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public JudgeResultListener getJudgeResultListener() {
		return judgeResultListener;
	}

	public void setJudgeResultListener(JudgeResultListener judgeResultListener) {
		this.judgeResultListener = judgeResultListener;
	}

}
