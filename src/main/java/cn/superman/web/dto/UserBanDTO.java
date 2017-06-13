package cn.superman.web.dto;

import java.io.Serializable;

public class UserBanDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 253560234577457440L;
	private Integer userId;
	// 是否已经被封禁，被封了的话就无法登陆了这个账号
	private Boolean isBan;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsBan() {
		return isBan;
	}

	public void setIsBan(Boolean isBan) {
		this.isBan = isBan;
	}

}
