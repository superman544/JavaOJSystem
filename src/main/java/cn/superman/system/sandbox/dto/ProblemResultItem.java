package cn.superman.system.sandbox.dto;

public class ProblemResultItem {
	private long useTime;
	private long useMemory;
	private String result;
	private String message;
	private boolean isNormal;
	private String inputFilePath;

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public long getUseMemory() {
		return useMemory;
	}

	public void setUseMemory(long useMemory) {
		this.useMemory = useMemory;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isNormal() {
		return isNormal;
	}

	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	@Override
	public String toString() {
		return "ProblemResultItem [useTime=" + useTime + ", useMemory="
				+ useMemory + ", result=" + result + ", message=" + message
				+ ", isNormal=" + isNormal + ", inputFilePath=" + inputFilePath
				+ "]";
	}

}
