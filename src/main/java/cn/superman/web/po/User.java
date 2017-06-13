package cn.superman.web.po;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 253560234577457440L;
    private Integer userId;
    private String account;
    private String password;
    private String nickname;
    private String email;
    private String sourceFileRootPath;
    private Integer rightProblemCount;
    private Integer haveDoneProblem;
    private String submitRecordTableName;
    // 是否已经被封禁，被封了的话就无法登陆了这个账号
    private Boolean isBan;
    // 改用户一共解决的问题总价值
    private Integer totalSolveValue;
    private Date lastSubmitTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSourceFileRootPath() {
        return sourceFileRootPath;
    }

    public void setSourceFileRootPath(String sourceFileRootPath) {
        this.sourceFileRootPath = sourceFileRootPath;
    }

    public Integer getRightProblemCount() {
        return rightProblemCount;
    }

    public void setRightProblemCount(Integer rightProblemCount) {
        this.rightProblemCount = rightProblemCount;
    }

    public Integer getHaveDoneProblem() {
        return haveDoneProblem;
    }

    public void setHaveDoneProblem(Integer haveDoneProblem) {
        this.haveDoneProblem = haveDoneProblem;
    }

    public String getSubmitRecordTableName() {
        return submitRecordTableName;
    }

    public void setSubmitRecordTableName(String submitRecordTableName) {
        this.submitRecordTableName = submitRecordTableName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getTotalSolveValue() {
        return totalSolveValue;
    }

    public void setTotalSolveValue(Integer totalSolveValue) {
        this.totalSolveValue = totalSolveValue;
    }

    public Boolean getIsBan() {
        return isBan;
    }

    public void setIsBan(Boolean isBan) {
        this.isBan = isBan;
    }

    public Date getLastSubmitTime() {
        return lastSubmitTime;
    }

    public void setLastSubmitTime(Date lastSubmitTime) {
        this.lastSubmitTime = lastSubmitTime;
    }

}
