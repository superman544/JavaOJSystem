package cn.superman.web.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateManagerVO {
	@NotNull(message = "管理员编码不能为空")
	private Integer managerId;
	private String password;
	@Size(max = 20)
	private String nickname;
	private Integer roleId;

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
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
