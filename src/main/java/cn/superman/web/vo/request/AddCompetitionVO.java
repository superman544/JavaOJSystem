package cn.superman.web.vo.request;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AddCompetitionVO {
	@NotBlank(message = "比赛名称不能为空")
	@Size(max = 30, message = "长度不可以超过30个字符")
	private String competitionName;
	private String competitionDescription;
	@NotNull(message = "比赛开始时间不能为空")
	private Date competitionBeginTime;
	@NotNull(message = "比赛结束时间不能为空")
	private Date competitionEndTime;
	private Date competitionApplyBeginTime;
	private Date competitionApplyEndTime;
	@NotNull(message = "比赛人数不能为空")
	private Integer competitionPlayersCount;
	private Boolean isCanDeclare = false;
	private Boolean isPublish = false;
	private String competitionProblemIds;

	public String getCompetitionName() {
		return competitionName;
	}

	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;
	}

	public String getCompetitionDescription() {
		return competitionDescription;
	}

	public void setCompetitionDescription(String competitionDescription) {
		this.competitionDescription = competitionDescription;
	}

	public Date getCompetitionBeginTime() {
		return competitionBeginTime;
	}

	public void setCompetitionBeginTime(Date competitionBeginTime) {
		this.competitionBeginTime = competitionBeginTime;
	}

	public Date getCompetitionEndTime() {
		return competitionEndTime;
	}

	public void setCompetitionEndTime(Date competitionEndTime) {
		this.competitionEndTime = competitionEndTime;
	}

	public Date getCompetitionApplyBeginTime() {
		return competitionApplyBeginTime;
	}

	public void setCompetitionApplyBeginTime(Date competitionApplyBeginTime) {
		this.competitionApplyBeginTime = competitionApplyBeginTime;
	}

	public Date getCompetitionApplyEndTime() {
		return competitionApplyEndTime;
	}

	public void setCompetitionApplyEndTime(Date competitionApplyEndTime) {
		this.competitionApplyEndTime = competitionApplyEndTime;
	}

	public Integer getCompetitionPlayersCount() {
		return competitionPlayersCount;
	}

	public void setCompetitionPlayersCount(Integer competitionPlayersCount) {
		this.competitionPlayersCount = competitionPlayersCount;
	}

	public Boolean getIsCanDeclare() {
		return isCanDeclare;
	}

	public void setIsCanDeclare(Boolean isCanDeclare) {
		this.isCanDeclare = isCanDeclare;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	public String getCompetitionProblemIds() {
		return competitionProblemIds;
	}

	public void setCompetitionProblemIds(String competitionProblemIds) {
		this.competitionProblemIds = competitionProblemIds;
	}

}
