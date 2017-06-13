package cn.superman.system.dto;

import cn.superman.system.commandExecutor.ResponseExecutor;
import cn.superman.system.sandbox.dto.Request;

public class CommonRequest {
	private ResponseExecutor executor;
	private Request request;

	public ResponseExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ResponseExecutor executor) {
		this.executor = executor;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
}
