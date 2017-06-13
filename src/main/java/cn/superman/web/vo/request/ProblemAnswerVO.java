package cn.superman.web.vo.request;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

public class ProblemAnswerVO {
	@NotNull(message = "代码语言不能为空")
	private String codeLanguage;
	@NotNull(message = "题目编号不能为空")
	private BigInteger submitProblemId;
	@NotNull(message = "不能提交空代码")
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
