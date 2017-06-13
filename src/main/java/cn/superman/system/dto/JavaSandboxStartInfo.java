package cn.superman.system.dto;

public class JavaSandboxStartInfo {
	private String jarFilePath;
	private String ip;
	private int port;
	private String problemClassFileRootPath;

	public String getJarFilePath() {
		return jarFilePath;
	}

	public void setJarFilePath(String jarFilePath) {
		this.jarFilePath = jarFilePath;
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

	public String getProblemClassFileRootPath() {
		return problemClassFileRootPath;
	}

	public void setProblemClassFileRootPath(String problemClassFileRootPath) {
		this.problemClassFileRootPath = problemClassFileRootPath;
	}

}
