package cn.superman.web.vo.request;

import org.hibernate.validator.constraints.NotBlank;

public class CompetitionApplicationEmailVO {
	@NotBlank(message = "申请单的编号不能为空")
	private String competitionApplicationId;
	@NotBlank(message = "接收邮件的邮箱不能为空")
	private String email;
	@NotBlank(message = "邮件内容不能为空")
	private String noticeContent;

	public String getCompetitionApplicationId() {
		return competitionApplicationId;
	}

	public void setCompetitionApplicationId(String competitionApplicationId) {
		this.competitionApplicationId = competitionApplicationId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

}
