package cn.superman.web.dto;

import java.util.Date;

public class UpdateCompetitionDTO {

    private Integer competitionId;
    private String competitionName;
    private String competitionDescription;
    private Date competitionBeginTime;
    private Date competitionEndTime;
    private Integer competitionPlayersCount;
    private String competitionProblemIds;
    private Boolean isPublish;
    private Date competitionApplyBeginTime;
    private Date competitionApplyEndTime;

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

    public String getCompetitionProblemIds() {
        return competitionProblemIds;
    }

    public void setCompetitionProblemIds(String competitionProblemIds) {
        this.competitionProblemIds = competitionProblemIds;
    }

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean isPublish) {
        this.isPublish = isPublish;
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

}
