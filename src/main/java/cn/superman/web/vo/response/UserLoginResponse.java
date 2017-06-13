package cn.superman.web.vo.response;

public class UserLoginResponse {
    private String account;
    private String nickname;
    private String email;
    private Integer rightProblemCount;
    private Integer haveDoneProblem;
    // 改用户一共解决的问题总价值
    private Integer totalSolveValue;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Integer getTotalSolveValue() {
        return totalSolveValue;
    }

    public void setTotalSolveValue(Integer totalSolveValue) {
        this.totalSolveValue = totalSolveValue;
    }

}
