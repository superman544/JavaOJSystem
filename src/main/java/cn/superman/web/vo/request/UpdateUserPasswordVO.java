package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateUserPasswordVO {
	@NotNull(message = "不能为空")
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "账号只允许包含数字和字母，而且长度在6-20个字符以内")
	private String account;
	@NotNull(message = "不能为空")
	@Size(min = 6, max = 20, message = "密码长度限制在6到20个字符以内")
	private String newPassword;
	@NotNull(message = "不能为空")
	@Size(min = 6, max = 20, message = "密码长度限制在6到20个字符以内")
	private String confirmNewPassword;
	@NotBlank(message = "邮件验证码不能为空")
	private String emailVerificationCode;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getEmailVerificationCode() {
		return emailVerificationCode;
	}

	public void setEmailVerificationCode(String emailVerificationCode) {
		this.emailVerificationCode = emailVerificationCode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

}
