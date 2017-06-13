package cn.superman.web.dto;

public class UpdateCompetitionAccountDTO {
	private Integer competitionAccountId;
	private String loginAccount;
	private String loginPassword;
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
