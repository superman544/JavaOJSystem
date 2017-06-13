package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateProblemTypeVO {
	@NotNull(message = "题目分类的编号不能 为空")
	private Integer problemTypeId;
	@NotBlank(message = "题目分类的名称不能 为空")
	@Size(max = 20)
	private String problemTypeName;
	private String problemTypeDescription;

	public Integer getProblemTypeId() {
		return problemTypeId;
	}

	public void setProblemTypeId(Integer problemTypeId) {
		this.problemTypeId = problemTypeId;
	}

	public String getProblemTypeName() {
		return problemTypeName;
	}

	public void setProblemTypeName(String problemTypeName) {
		this.problemTypeName = problemTypeName;
	}

	public String getProblemTypeDescription() {
		return problemTypeDescription;
	}

	public void setProblemTypeDescription(String problemTypeDescription) {
		this.problemTypeDescription = problemTypeDescription;
	}

	@Override
	public String toString() {
		return "problemType [problemTypeId=" + problemTypeId
				+ ", problemTypeName=" + problemTypeName
				+ ", problemTypeDescription=" + problemTypeDescription + "]";
	}
}
