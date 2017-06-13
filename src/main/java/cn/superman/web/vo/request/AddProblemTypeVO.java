package cn.superman.web.vo.request;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AddProblemTypeVO {
	@NotBlank(message = "题目分类的名称不能 为空")
	@Size(max = 20)
	private String problemTypeName;
	private String problemTypeDescription;

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

}
