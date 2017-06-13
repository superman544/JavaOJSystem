package cn.superman.web.controller.webSocket.vo;

public class JavaSandboxStatusRequestVO extends WebSocketRequestVO {
	private boolean isReceiveJavaSandboxStatus;

	public boolean isReceiveJavaSandboxStatus() {
		return isReceiveJavaSandboxStatus;
	}

	public void setReceiveJavaSandboxStatus(boolean isReceiveJavaSandboxStatus) {
		this.isReceiveJavaSandboxStatus = isReceiveJavaSandboxStatus;
	}

}
