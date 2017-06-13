package cn.superman.web.vo.request;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AddRoleVO {
	@NotBlank(message = "角色名字不能为空")
	@Size(max = 20)
	private String roleName;
	private String permissions;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

}
