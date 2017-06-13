package cn.superman.web.vo.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import cn.superman.system.service.dto.ProblemJudgeResultItem;

public class SubmitRecordVO {
    private BigInteger submitId;
    private Date submitTime;
    private BigInteger submitProblemId;
    private Integer submitUserId;
    private double score;
    private boolean isAccepted;
    private String codeLanguage;
    private String code;
    private List<ProblemJudgeResultItem> items;

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ProblemJudgeResultItem> getItems() {
        return items;
    }

    public void setItems(List<ProblemJudgeResultItem> items) {
        this.items = items;
    }

}
