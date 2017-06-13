package cn.superman.web.vo.request;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AddManagerVO {
	@NotBlank(message = "账号不能为空")
	@Size(min = 6, max = 20, message = "账号长度限制在6到20个字符以内")
	private String account;
	@NotBlank(message = "密码不能为空")
	@Size(min = 6, max = 20, message = "密码长度限制在6到20个字符以内")
	private String password;
	@Size(max = 20, message = "昵称长度限制在6到20个字符以内")
	private String nickname;
	private Integer roleId;

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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
