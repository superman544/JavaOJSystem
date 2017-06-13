package cn.superman.web.bean;

import java.util.HashMap;

public class ResponseMap extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6157761208141308653L;

	public ResponseMap buildSucessResponse() {
		this.put("success", true);
		return this;
	}

	public ResponseMap buildNotSucessResponse() {
		this.put("success", false);
		return this;
	}

	public ResponseMap append(String key, Object value) {
		this.put(key, value);
		return this;
	}
}
