package cn.superman.web.dto;

public class CompetitionApplicationEmailDTO {
	private String competitionApplicationId;
	private String email;
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
