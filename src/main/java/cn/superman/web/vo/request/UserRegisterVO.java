package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserRegisterVO {
    @NotNull(message = "不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "账号只允许包含数字和字母，而且长度在6-20个字符以内")
    private String account;
    @NotNull(message = "不能为空")
    @Size(min = 6, max = 20, message = "密码长度限制在6到20个字符以内")
    private String password;
    @NotNull(message = "不能为空")
    @Size(min = 6, max = 20, message = "密码长度限制在6到20个字符以内")
    private String confirmPassword;
    @NotNull(message = "不能为空")
    @Email(message = "邮箱格式不对", regexp = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+")
    private String email;
    private String nickname;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

}
