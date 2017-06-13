package cn.superman.web.vo.request;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AddAnnouncementVO {
	@NotBlank(message = "文章的标题请不要为空哦")
	@Size(max = 30)
	private String announcementTitle;
	@NotBlank(message = "文章的简介请不要为空哦")
	@Size(max = 250)
	private String announcementIntroduction;
	private String announcementContent;
	private Boolean isPublish = false;

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
