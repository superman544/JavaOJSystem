package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateCompetitionAccountVO {
	@NotNull(message = "账号编号不能为空")
	private Integer competitionAccountId;
	@NotBlank(message = "登陆账号不能为空")
	@Size(message = "最短为6位，最长为32位", min = 6, max = 32)
	private String loginAccount;
	@NotBlank(message = "登陆账号不能为空")
	@Size(message = "最短为6位，最长为32位", min = 6, max = 32)
	private String loginPassword;
	@Size(message = "备注最长是300个字符", max = 300)
	private String accountRemark;

	public Integer getCompetitionAccountId() {
		return competitionAccountId;
	}

	public void setCompetitionAccountId(Integer competitionAccountId) {
		this.competitionAccountId = competitionAccountId;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getAccountRemark() {
		return accountRemark;
	}

	public void setAccountRemark(String accountRemark) {
		this.accountRemark = accountRemark;
	}

}
