package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class NewProblemVO {
	@NotBlank(message = "题目名字不能为空")
	@Size(max = 50)
	private String problemName;
	@NotBlank(message = "题目内容不能为空")
	private String problemContent;
	@NotNull(message = "题目必须属于某种类型")
	private Integer problemTypeId;
	@NotNull(message = "请输入时间限制")
	private Long timeLimit;
	@NotNull(message = "请输入内存限制")
	private Long memoryLimit;
	private int problemDifficulty;
	private int problemValue;
	private Boolean isPublish = false;
	private String problemLabel;

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public String getProblemContent() {
		return problemContent;
	}

	public void setProblemContent(String problemContent) {
		this.problemContent = problemContent;
	}

	public Integer getProblemTypeId() {
		return problemTypeId;
	}

	public void setProblemTypeId(Integer problemTypeId) {
		this.problemTypeId = problemTypeId;
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

	public int getProblemDifficulty() {
		return problemDifficulty;
	}

	public void setProblemDifficulty(int problemDifficulty) {
		this.problemDifficulty = problemDifficulty;
	}

	public int getProblemValue() {
		return problemValue;
	}

	public void setProblemValue(int problemValue) {
		this.problemValue = problemValue;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	public String getProblemLabel() {
		return problemLabel;
	}

	public void setProblemLabel(String problemLabel) {
		this.problemLabel = problemLabel;
	}

}
