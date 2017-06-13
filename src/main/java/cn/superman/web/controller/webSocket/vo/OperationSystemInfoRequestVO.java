package cn.superman.web.controller.webSocket.vo;

public class OperationSystemInfoRequestVO extends WebSocketRequestVO {
	private boolean isReceiveOperationSystemInfo;

	public boolean isReceiveOperationSystemInfo() {
		return isReceiveOperationSystemInfo;
	}

	public void setReceiveOperationSystemInfo(
			boolean isReceiveOperationSystemInfo) {
		this.isReceiveOperationSystemInfo = isReceiveOperationSystemInfo;
	}

}
