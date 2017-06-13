package cn.superman.web.vo.request;

public class ProblemStandardFileVO {
	// 采用加密的方式，加密后，把这两个路径，带到页面那里去
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

	@Override
	public String toString() {
		return "ProblemStandardFileVO [inputFilePath=" + inputFilePath
				+ ", outputFilePath=" + outputFilePath
				+ ", inputFilePageShowName=" + inputFilePageShowName
				+ ", outputFilePageShowName=" + outputFilePageShowName + "]";
	}

}
