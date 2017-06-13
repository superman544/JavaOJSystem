package cn.superman.web.vo.response;

import java.util.Date;

public class CompetitionResponse {
    private Integer competitionId;
    private String competitionName;
    private String competitionDescription;
    private Date competitionBeginTime;
    private Date competitionEndTime;
    private Date competitionApplyBeginTime;
    private Date competitionApplyEndTime;
    private Boolean isClose;

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

    public Boolean getIsClose() {
        return isClose;
    }

    public void setIsClose(Boolean isClose) {
        this.isClose = isClose;
    }

}
