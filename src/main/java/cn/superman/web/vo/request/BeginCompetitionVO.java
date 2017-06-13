package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class BeginCompetitionVO {
    @NotBlank(message = "账号不能为空")
    private String account;
    @NotBlank(message = "令牌不能为空")
    private String password;
    @NotNull(message = "缺乏比赛信息")
    private Integer competitionId;
    @NotBlank(message = "缺乏比赛令牌")
    private String token;

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

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
