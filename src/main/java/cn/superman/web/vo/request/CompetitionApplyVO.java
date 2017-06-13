package cn.superman.web.vo.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class CompetitionApplyVO {
	@NotNull(message = "邮箱不能为空")
	@Email(message = "邮箱格式不对")
	private String email;
	@NotBlank(message = "电话不能为空")
	private String phone;
	@NotNull(message = "参赛人数至少为1")
	@Min(value = 1, message = "参赛人数至少为1")
	@Max(value = 10, message = "参赛人数至多为10人")
	private Integer applicationPeopleCount;
	@NotBlank(message = "申请概要不能不填")
	private String applicationSummary;
	private String applicationContent;
	@NotNull(message = "比赛选择失败")
	private Integer competitionId;
	@NotBlank(message = "缺乏令牌，请确认该比赛是可以申请报名的")
	private String token;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "AddCompetitionApplicationVO [email=" + email + ", phone="
				+ phone + ", applicationPeopleCount=" + applicationPeopleCount
				+ ", applicationSummary=" + applicationSummary
				+ ", applicationContent=" + applicationContent
				+ ", competitionId=" + competitionId + ", token=" + token + "]";
	}

}
