package cn.superman.system.sandbox.dto;

import java.util.ArrayList;
import java.util.List;

public class Problem {
	private long timeLimit;
	private long memoryLimit;
	private String classFileName;
	private String runId;
	private List<String> inputDataFilePathList = new ArrayList<String>();

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

	public String getClassFileName() {
		return classFileName;
	}

	public void setClassFileName(String classFileName) {
		this.classFileName = classFileName;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public List<String> getInputDataFilePathList() {
		return inputDataFilePathList;
	}

	public void setInputDataFilePathList(List<String> inputDataFilePathList) {
		this.inputDataFilePathList = inputDataFilePathList;
	}

}
