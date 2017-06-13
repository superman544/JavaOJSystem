package cn.superman.web.controller.webSocket.vo;

public class WebSocketResponseVO {
	private ResponseType responseType;
	private Object data;

	public WebSocketResponseVO() {
	}

	public WebSocketResponseVO(ResponseType responseType, Object data) {
		this.responseType = responseType;
		this.data = data;
	}

	public ResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "WebSocketResponseVO [responseType=" + responseType + ", data="
				+ data + "]";
	}

	public static enum ResponseType {
		PendingHandleProblemCount, JavaSandboxStatus, OperationSystemInfo, AppSystemInfo;
	}

}
