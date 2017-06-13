package cn.superman.web.po;

import java.io.Serializable;

public class Role implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6588463114269891282L;
    private Integer roleId;
    private String roleName;
    private String permissions;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

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

    @Override
    public String toString() {
        return "Role [roleId=" + roleId + ", roleName=" + roleName + "]";
    }

}
