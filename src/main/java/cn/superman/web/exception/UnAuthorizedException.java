package cn.superman.web.exception;

public class UnAuthorizedException extends RuntimeException {
	private String redirectUrl;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5655523165098528556L;

	public UnAuthorizedException(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

}
