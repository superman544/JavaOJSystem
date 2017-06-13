package cn.superman.web.po;

import java.io.Serializable;
import java.util.Date;

public class Competition implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = -2046076939739273575L;
	private Integer competitionId;
	private String competitionName;
	private String competitionDescription;
	private Date competitionBeginTime;
	private Date competitionEndTime;
	private Date competitionApplyBeginTime;
	private Date competitionApplyEndTime;
	private Integer competitionPlayersCount;
	private Boolean isClose;
	private Boolean isPublish;
	private Boolean isCanDeclare;
	private Boolean isJudge;
	private String competitionProblemIds;
	private String competitionContentRootPath;

	public Integer getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}

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

	public Integer getCompetitionPlayersCount() {
		return competitionPlayersCount;
	}

	public void setCompetitionPlayersCount(Integer competitionPlayersCount) {
		this.competitionPlayersCount = competitionPlayersCount;
	}

	public Boolean getIsClose() {
		return isClose;
	}

	public void setIsClose(Boolean isClose) {
		this.isClose = isClose;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	public Boolean getIsCanDeclare() {
		return isCanDeclare;
	}

	public void setIsCanDeclare(Boolean isCanDeclare) {
		this.isCanDeclare = isCanDeclare;
	}

	public String getCompetitionProblemIds() {
		return competitionProblemIds;
	}

	public void setCompetitionProblemIds(String competitionProblemIds) {
		this.competitionProblemIds = competitionProblemIds;
	}

	public String getCompetitionContentRootPath() {
		return competitionContentRootPath;
	}

	public void setCompetitionContentRootPath(String competitionContentRootPath) {
		this.competitionContentRootPath = competitionContentRootPath;
	}

	public Boolean getIsJudge() {
		return isJudge;
	}

	public void setIsJudge(Boolean isJudge) {
		this.isJudge = isJudge;
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

	@Override
	public String toString() {
		return "Competition [competitionId=" + competitionId
				+ ", competitionName=" + competitionName
				+ ", competitionDescription=" + competitionDescription
				+ ", competitionBeginTime=" + competitionBeginTime
				+ ", competitionEndTime=" + competitionEndTime
				+ ", competitionPlayersCount=" + competitionPlayersCount
				+ ", isClose=" + isClose + ", isPublish=" + isPublish
				+ ", isCanDeclare=" + isCanDeclare + ", isJudge=" + isJudge
				+ ", competitionProblemIds=" + competitionProblemIds
				+ ", competitionContentRootPath=" + competitionContentRootPath
				+ "]";
	}

}
