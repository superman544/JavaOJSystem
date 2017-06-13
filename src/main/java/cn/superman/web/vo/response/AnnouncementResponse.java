package cn.superman.web.vo.response;

import java.util.Date;

public class AnnouncementResponse {
    private Integer announcementId;
    private String announcementTitle;
    private String announcementIntroduction;
    private String announcementContent;
    private Date announcementCreateTime;
    private Date announcementPublishTime;

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementIntroduction() {
        return announcementIntroduction;
    }

    public void setAnnouncementIntroduction(String announcementIntroduction) {
        this.announcementIntroduction = announcementIntroduction;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
    }

    public Date getAnnouncementCreateTime() {
        return announcementCreateTime;
    }

    public void setAnnouncementCreateTime(Date announcementCreateTime) {
        this.announcementCreateTime = announcementCreateTime;
    }

    public Date getAnnouncementPublishTime() {
        return announcementPublishTime;
    }

    public void setAnnouncementPublishTime(Date announcementPublishTime) {
        this.announcementPublishTime = announcementPublishTime;
    }

}
