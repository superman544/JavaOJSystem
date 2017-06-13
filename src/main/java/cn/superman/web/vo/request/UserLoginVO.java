package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserLoginVO {
    @NotNull(message = "不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "账号只允许包含数字和字母，而且长度在6-20个字符以内")
    private String account;
    @NotNull(message = "不能为空")
    @Size(min = 6, max = 20, message = "密码长度限制在6到20个字符以内")
    private String password;

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

}
