package cn.superman.system.service.bean;

public class SandboxStatus {
	private String idCard;
	private String ip;
	private int port;
	private String pid;
	private long beginTime;
	private long useMemory;
	private boolean isJudgeing;
	private boolean isRunning;
	private boolean isWantStop;
	private boolean isWantClose;
	private static final long SECOND = 1000;
	private static final long MINUTE = 1000 * 60;
	private static final long HOUR = 1000 * 60 * 60;
	private static final long DAY = 1000 * 60 * 60 * 24;
	private static final long MB = 1024 * 1024;

	public SandboxStatus() {
		super();
	}

	public SandboxStatus(String idCard, String ip, int port) {
		super();
		this.idCard = idCard;
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getUseMemory() {
		return useMemory;
	}

	public void setUseMemory(long useMemory) {
		this.useMemory = useMemory;
	}

	public boolean isJudgeing() {
		return isJudgeing;
	}

	public void setJudgeing(boolean isJudgeing) {
		this.isJudgeing = isJudgeing;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isWantStop() {
		return isWantStop;
	}

	public void setWantStop(boolean isWantStop) {
		this.isWantStop = isWantStop;
	}

	public boolean isWantClose() {
		return isWantClose;
	}

	public void setWantClose(boolean isWantClose) {
		this.isWantClose = isWantClose;
	}

	/**
	 * 返回的时间格式： xx天xx小时xx分钟xx秒
	 * 
	 * @return
	 */
	public String getFormatRunningTime() {
		long now = System.currentTimeMillis();
		long gap = now - beginTime;
		StringBuilder builder = new StringBuilder();
		long temp = 0;
		boolean flag = false;

		if (gap > DAY) {
			temp = gap / DAY;
			builder.append(temp + "天");
			gap -= (DAY * temp);
			flag = true;
		}

		if (gap > HOUR) {
			temp = gap / HOUR;
			builder.append(temp + "小时");
			gap -= (HOUR * temp);
			flag = true;
		}

		if (gap > MINUTE) {
			temp = gap / MINUTE;
			builder.append(temp + "分钟");
			gap -= (MINUTE * temp);
			flag = true;
		}

		if (gap > SECOND) {
			temp = gap / SECOND;
			builder.append(temp + "秒");
			gap -= (SECOND * temp);
			flag = true;
		}

		if (!flag) {
			builder.append(gap + "毫秒");
		}

		return builder.toString();
	}

	/**
	 * 返回格式xxxMB
	 * 
	 * @return
	 */
	public String getFormatUseMemory() {
		StringBuilder builder = new StringBuilder();
		long temp = 0;
		long memory = useMemory;

		if (memory > MB) {
			temp = memory / MB;
			builder.append(temp + "MB");
			memory -= temp * MB;
		} else {
			builder.append("小于1M");
		}

		return builder.toString();
	}

	@Override
	public String toString() {
		return "SandboxStatus [idCard=" + idCard + ", ip=" + ip + ", port="
				+ port + ", pid=" + pid + ", beginTime=" + beginTime
				+ ", useMemory=" + useMemory + ", isJudgeing=" + isJudgeing
				+ ", isRunning=" + isRunning + ", isWantStop=" + isWantStop
				+ ", isWantClose=" + isWantClose + "]";
	}

}
