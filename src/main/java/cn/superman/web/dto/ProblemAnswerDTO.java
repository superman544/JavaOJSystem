package cn.superman.web.dto;

import java.math.BigInteger;

import cn.superman.web.po.User;

public class ProblemAnswerDTO {
	private String codeLanguage;
	private BigInteger submitProblemId;
	private User user;
	private String code;

	public String getCodeLanguage() {
		return codeLanguage;
	}

	public void setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
	}

	public BigInteger getSubmitProblemId() {
		return submitProblemId;
	}

	public void setSubmitProblemId(BigInteger submitProblemId) {
		this.submitProblemId = submitProblemId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
