package cn.superman.web.controller.webSocket.vo;

public class WebSocketRequestVO {
	private RequestType requestType;

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public static enum RequestType {
		PendingHandleProblemCount, JavaSandboxStatus, OperationSystemInfo;
	}

}
