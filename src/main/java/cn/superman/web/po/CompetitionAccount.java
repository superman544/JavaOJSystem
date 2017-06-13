package cn.superman.web.po;

import java.io.Serializable;

public class CompetitionAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9078232326171890497L;
	private Integer competitionAccountId;
	private String loginAccount;
	private String loginPassword;
	private Integer competitionId;
	private String accountRemark;
	private String accountScore;
	private String accountCodeRootPath;
	private Boolean isUse;

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

	public Integer getCompetitionId() {
		return competitionId;
	}

	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}

	public String getAccountRemark() {
		return accountRemark;
	}

	public void setAccountRemark(String accountRemark) {
		this.accountRemark = accountRemark;
	}

	public String getAccountScore() {
		return accountScore;
	}

	public void setAccountScore(String accountScore) {
		this.accountScore = accountScore;
	}

	public String getAccountCodeRootPath() {
		return accountCodeRootPath;
	}

	public void setAccountCodeRootPath(String accountCodeRootPath) {
		this.accountCodeRootPath = accountCodeRootPath;
	}

	public Boolean getIsUse() {
		return isUse;
	}

	public void setIsUse(Boolean isUse) {
		this.isUse = isUse;
	}

	@Override
	public String toString() {
		return "CompetitionAccount [competitionAccountId="
				+ competitionAccountId + ", loginAccount=" + loginAccount
				+ ", loginPassword=" + loginPassword + ", competitionId="
				+ competitionId + ", accountRemark=" + accountRemark
				+ ", accountScore=" + accountScore + ", accountCodeRootPath="
				+ accountCodeRootPath + ", isUse=" + isUse + "]";
	}

}
