package cn.superman.web.po;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class SubmitRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3525247058399149957L;
	private BigInteger submitId;
	private Date submitTime;
	private BigInteger submitProblemId;
	private Integer submitUserId;
	private Double score;
	private Boolean isAccepted;
	private String details;
	private String codeLanguage;
	private String codeFilePath;
	private String submitRecordTableName;

	public BigInteger getSubmitId() {
		return submitId;
	}

	public void setSubmitId(BigInteger submitId) {
		this.submitId = submitId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public BigInteger getSubmitProblemId() {
		return submitProblemId;
	}

	public void setSubmitProblemId(BigInteger submitProblemId) {
		this.submitProblemId = submitProblemId;
	}

	public Integer getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(Integer submitUserId) {
		this.submitUserId = submitUserId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCodeLanguage() {
		return codeLanguage;
	}

	public void setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
	}

	public String getCodeFilePath() {
		return codeFilePath;
	}

	public void setCodeFilePath(String codeFilePath) {
		this.codeFilePath = codeFilePath;
	}

	public String getSubmitRecordTableName() {
		return submitRecordTableName;
	}

	public void setSubmitRecordTableName(String submitRecordTableName) {
		this.submitRecordTableName = submitRecordTableName;
	}

}
