package cn.superman.web.dto;

public class ProblemStandardFileDTO {
	// 采用加密的方式，加密后，把这两个路径，带到页面那里去，主要是为了不想让别人看到服务器存放文件的真正路径，但是又想前端带过来，可以直接解密后提供下载
	private String inputFilePath;
	private String outputFilePath;
	// 标准文件，在页面里显示的文件名字
	private String inputFilePageShowName;
	private String outputFilePageShowName;

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

	public String getInputFilePageShowName() {
		return inputFilePageShowName;
	}

	public void setInputFilePageShowName(String inputFilePageShowName) {
		this.inputFilePageShowName = inputFilePageShowName;
	}

	public String getOutputFilePageShowName() {
		return outputFilePageShowName;
	}

	public void setOutputFilePageShowName(String outputFilePageShowName) {
		this.outputFilePageShowName = outputFilePageShowName;
	}

}
