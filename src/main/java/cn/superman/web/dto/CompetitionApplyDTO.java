package cn.superman.web.dto;

public class CompetitionApplyDTO {
	private String email;
	private String phone;
	private Integer applicationPeopleCount;
	private String applicationSummary;
	private String applicationContent;
	private Integer competitionId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getApplicationPeopleCount() {
		return applicationPeopleCount;
	}

	public void setApplicationPeopleCount(Integer applicationPeopleCount) {
		this.applicationPeopleCount = applicationPeopleCount;
	}

	public String getApplicationSummary() {
		return applicationSummary;
	}

	public void setApplicationSummary(String applicationSummary) {
		this.applicationSummary = applicationSummary;
	}

	public String getApplicationContent() {
		return applicationContent;
	}

	public void setApplicationContent(String applicationContent) {
		this.applicationContent = applicationContent;
	}

	public Integer getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}

	@Override
	public String toString() {
		return "AddCompetitionApplicationDTO [email=" + email + ", phone="
				+ phone + ", applicationPeopleCount=" + applicationPeopleCount
				+ ", applicationSummary=" + applicationSummary
				+ ", applicationContent=" + applicationContent
				+ ", competitionId=" + competitionId + "]";
	}

}
