package cn.superman.system.sandbox.dto;

public class SandBoxStatus {
	private String pid;
	private long useMemory;
	private long maxMemory;
	private long beginStartTime;
	private boolean isBusy;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public long getUseMemory() {
		return useMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public void setUseMemory(long useMemory) {
		this.useMemory = useMemory;
	}

	public long getBeginStartTime() {
		return beginStartTime;
	}

	public void setBeginStartTime(long beginStartTime) {
		this.beginStartTime = beginStartTime;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

}
