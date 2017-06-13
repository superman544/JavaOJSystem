package cn.superman.system.service.dto;

import java.util.List;

public class ProblemJudgeResult {
	private String runId;
	private float correctRate;
	private List<ProblemJudgeResultItem> problemJudgeResultItems;

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public float getCorrectRate() {
		return correctRate;
	}

	public void setCorrectRate(float correctRate) {
		this.correctRate = correctRate;
	}

	public List<ProblemJudgeResultItem> getProblemJudgeResultItems() {
		return problemJudgeResultItems;
	}

	public void setProblemJudgeResultItems(
			List<ProblemJudgeResultItem> problemJudgeResultItems) {
		this.problemJudgeResultItems = problemJudgeResultItems;
	}

	@Override
	public String toString() {
		return "ProblemJudgeResult [runId=" + runId + ", correctRate="
				+ correctRate + ", problemJudgeResultItems="
				+ problemJudgeResultItems + "]";
	}

}
