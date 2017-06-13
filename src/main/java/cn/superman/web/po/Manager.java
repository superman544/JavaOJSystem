package cn.superman.web.po;

import java.io.Serializable;

public class Manager implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7180085126092613827L;
    private Integer managerId;
    private String account;
    private String password;
    private String nickname;
    private Role role;

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Manager [managerId=" + managerId + ", account=" + account + ", password=" + password + ", nickname=" + nickname + ", role=" + role + "]";
    }

}
