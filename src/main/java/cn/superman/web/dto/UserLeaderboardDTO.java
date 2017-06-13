package cn.superman.web.dto;

public class UserLeaderboardDTO {
    private String nickname;
    private String email;
    private Integer rightProblemCount;
    private Integer haveDoneProblem;
    private Integer totalSolveValue;

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
