package cn.superman.web.dto;

public class AddProblemTypeDTO {
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
