package cn.superman.system.dto;

public class CommunicatorStatus {
	private boolean isJudgeing;
	private boolean isWantStop;
	private boolean isStop;
	private boolean isWantClose;

	public boolean isJudgeing() {
		return isJudgeing;
	}

	public void setJudgeing(boolean isJudgeing) {
		this.isJudgeing = isJudgeing;
	}

	public boolean isWantStop() {
		return isWantStop;
	}

	public void setWantStop(boolean isWantStop) {
		this.isWantStop = isWantStop;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public boolean isWantClose() {
		return isWantClose;
	}

	public void setWantClose(boolean isWantClose) {
		this.isWantClose = isWantClose;
	}

}
