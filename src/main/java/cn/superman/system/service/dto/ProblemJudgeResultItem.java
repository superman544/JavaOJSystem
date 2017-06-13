package cn.superman.system.service.dto;

public class ProblemJudgeResultItem {
	private long useTime;
	private long useMemory;
	private String message;
	private String inputFilePath;
	private String outputFilePath;
	private boolean isRight;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	@Override
	public String toString() {
		return "ProblemJudgeResultItem [useTime=" + useTime + ", useMemory="
				+ useMemory + ", message=" + message + ", inputFilePath="
				+ inputFilePath + ", outputFilePath=" + outputFilePath
				+ ", isRight=" + isRight + "]";
	}

}
