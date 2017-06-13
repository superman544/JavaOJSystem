package cn.superman.web.dto;

public class UpdateAnnouncementDTO {
	private Integer announcementId;
	private String announcementTitle;
	private String announcementIntroduction;
	private String announcementContent;
	private Integer announcementCreateManagerId;
	private Boolean isPublish;

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

	public Integer getAnnouncementCreateManagerId() {
		return announcementCreateManagerId;
	}

	public void setAnnouncementCreateManagerId(
			Integer announcementCreateManagerId) {
		this.announcementCreateManagerId = announcementCreateManagerId;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

}
