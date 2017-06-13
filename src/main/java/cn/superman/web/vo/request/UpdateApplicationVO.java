package cn.superman.web.vo.request;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateApplicationVO {
	@NotBlank(message = "申请编号不能为空")
	private String competitionApplicationId;
	@NotBlank(message = "处理级别不能不填写")
	private Integer level;
	@NotBlank(message = "请选择是否已经处理")
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
