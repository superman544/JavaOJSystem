package cn.superman.web.dto;

public class UpdateProblemTypeDTO {
	private Integer problemTypeId;
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
