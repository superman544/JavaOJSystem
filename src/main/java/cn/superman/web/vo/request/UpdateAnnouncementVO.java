package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateAnnouncementVO {
	@NotNull(message = "文章的编号不能为空")
	private Integer announcementId;
	@NotBlank(message = "文章的标题不能为空")
	private String announcementTitle;
	@NotBlank(message = "文章的简介不能为空")
	private String announcementIntroduction;
	private String announcementContent;
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

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

}
