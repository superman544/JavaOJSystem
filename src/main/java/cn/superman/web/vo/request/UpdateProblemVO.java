package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateProblemVO {
	@NotNull(message = "题目编号不能为空")
	private Integer problemId;
	@NotBlank(message = "题目名称不能为空")
	@Size(max = 50)
	private String problemName;
	private Integer problemTypeId;
	private String problemContent;
	private String problemLabel;
	@NotNull(message = "时间限制不能为空")
	private Long timeLimit;
	@NotNull(message = "内存限制不能为空")
	private Long memoryLimit;
	private Integer problemDifficulty;
	private Integer problemValue;
	private Integer problemVersion;
	private Boolean isPublish;

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public Integer getProblemTypeId() {
		return problemTypeId;
	}

	public void setProblemTypeId(Integer problemTypeId) {
		this.problemTypeId = problemTypeId;
	}

	public String getProblemContent() {
		return problemContent;
	}

	public void setProblemContent(String problemContent) {
		this.problemContent = problemContent;
	}

	public String getProblemLabel() {
		return problemLabel;
	}

	public void setProblemLabel(String problemLabel) {
		this.problemLabel = problemLabel;
	}

	public Long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Long getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(Long memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public Integer getProblemDifficulty() {
		return problemDifficulty;
	}

	public void setProblemDifficulty(Integer problemDifficulty) {
		this.problemDifficulty = problemDifficulty;
	}

	public Integer getProblemValue() {
		return problemValue;
	}

	public void setProblemValue(Integer problemValue) {
		this.problemValue = problemValue;
	}

	public Integer getProblemVersion() {
		return problemVersion;
	}

	public void setProblemVersion(Integer problemVersion) {
		this.problemVersion = problemVersion;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

}
