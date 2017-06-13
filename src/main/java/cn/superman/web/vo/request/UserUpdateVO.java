package cn.superman.web.vo.request;

import org.hibernate.validator.constraints.NotBlank;

public class UserUpdateVO {
    private String password;
    private String nickname;
    private String email;
    @NotBlank(message = "邮件的验证码不能为空")
    private String emailVerificationCode;

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

    public String getEmailVerificationCode() {
        return emailVerificationCode;
    }

    public void setEmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }

}
