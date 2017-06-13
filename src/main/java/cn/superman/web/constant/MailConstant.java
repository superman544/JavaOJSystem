package cn.superman.web.constant;

public enum MailConstant {
	userName("865907949@qq.com"), password("bqyttkceualkbcea"), host(
			"smtp.qq.com");
	private String value;

	private MailConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
