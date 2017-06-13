package cn.superman.web.dto;

public class UpdateApplicationDTO {
	private String competitionApplicationId;
	private Integer level;
	private Boolean isHaveHandle;

	public String getCompetitionApplicationId() {
		return competitionApplicationId;
	}

	public void setCompetitionApplicationId(String competitionApplicationId) {
		this.competitionApplicationId = competitionApplicationId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean getIsHaveHandle() {
		return isHaveHandle;
	}

	public void setIsHaveHandle(Boolean isHaveHandle) {
		this.isHaveHandle = isHaveHandle;
	}

}
