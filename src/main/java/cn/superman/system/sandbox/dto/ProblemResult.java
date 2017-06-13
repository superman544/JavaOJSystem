package cn.superman.system.sandbox.dto;

import java.util.ArrayList;
import java.util.List;

public class ProblemResult {
	private String runId;
	private List<ProblemResultItem> resultItems = new ArrayList<ProblemResultItem>();

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public List<ProblemResultItem> getResultItems() {
		return resultItems;
	}

	public void setResultItems(List<ProblemResultItem> resultItems) {
		this.resultItems = resultItems;
	}

}
