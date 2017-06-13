package cn.superman.system.sandbox.dto;

public class Response {
	private String requestCommand;
	private String responseCommand;
	private String data;
	private String signalId;

	public String getResponseCommand() {
		return responseCommand;
	}

	public void setResponseCommand(String responseCommand) {
		this.responseCommand = responseCommand;
	}

	public String getRequestCommand() {
		return requestCommand;
	}

	public void setRequestCommand(String requestCommand) {
		this.requestCommand = requestCommand;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSignalId() {
		return signalId;
	}

	public void setSignalId(String signalId) {
		this.signalId = signalId;
	}

	@Override
	public String toString() {
		return "Response [requestCommand=" + requestCommand
				+ ", responseCommand=" + responseCommand + ", data=" + data
				+ ", signalId=" + signalId + "]";
	}

}
