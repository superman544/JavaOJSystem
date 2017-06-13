package cn.superman.web.dto;

import java.util.Date;

public class AddCompetitionDTO {
    private String competitionName;
    private String competitionDescription;
    private Date competitionBeginTime;
    private Date competitionEndTime;
    private Date competitionApplyBeginTime;
    private Date competitionApplyEndTime;
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

    public Integer getCompetitionPlayersCount() {
        return competitionPlayersCount;
    }

    public void setCompetitionPlayersCount(Integer competitionPlayersCount) {
        this.competitionPlayersCount = competitionPlayersCount;
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

    @Override
    public String toString() {
        return "AddCompetitionDTO [competitionName=" + competitionName + ", competitionDescription=" + competitionDescription + ", competitionBeginTime="
                + competitionBeginTime + ", competitionEndTime=" + competitionEndTime + ", competitionApplyBeginTime=" + competitionApplyBeginTime
                + ", competitionApplyEndTime=" + competitionApplyEndTime + ", competitionPlayersCount=" + competitionPlayersCount + ", isCanDeclare="
                + isCanDeclare + ", isPublish=" + isPublish + ", competitionProblemIds=" + competitionProblemIds + "]";
    }

}
